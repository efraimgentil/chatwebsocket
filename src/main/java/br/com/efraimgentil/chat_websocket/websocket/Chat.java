package br.com.efraimgentil.chat_websocket.websocket;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{user_name}")
public class Chat {
    
    private final String NO_USERNAME = "";
    private static Map<String, Session> usernameSession = new LinkedHashMap<>();
    
    @OnOpen
    public void open(@PathParam("user_name") String username,  Session session){
        session.getUserProperties().put("username", username );
        if(!usernameSession.containsKey(username)){
            usernameSession.put(username, session);
            Set<Session> sessions = session.getOpenSessions();
            sessions.remove(session);
            String mensagem = username + " conectou";
            try {
                session.getBasicRemote().sendText( formataMensagemToJson("Conectou com sucesso" , NO_USERNAME , TiposMensagem.SISTEMA) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            broadcastMessage( formataMensagemToJson( mensagem, NO_USERNAME , TiposMensagem.SISTEMA ) , sessions );
        }else{
            try {
                session.close( new CloseReason( CloseCodes.VIOLATED_POLICY  , "Desculpe mas o usuario informado já está em uso") );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @OnMessage
    public void mensagem(String mensagem , Session userSession){
        String username = (String) userSession.getUserProperties().get("username");
        if(isMensagemPrivada(mensagem)){
            enviaMensagemPrivada( mensagem , userSession );
        }else{
            mensagem = formataMensagemToJson(mensagem, username, TiposMensagem.PUBLICA );
            broadcastMessage( mensagem , userSession.getOpenSessions() );
        }
    }
    
    private void enviaMensagemPrivada(String mensagem, Session userSession) {
        String username = getUsername(userSession);
        String usuarioDestino = getDestinatarioMensagemPrivada(mensagem);
        Session sessionDestino = usernameSession.get(usuarioDestino);
        try {
            if(sessionDestino != null){
                mensagem = mensagem.replace("\\"+usuarioDestino , "" );
                mensagem = formataMensagemToJson(mensagem, username, TiposMensagem.PRIVADA );
                sessionDestino.getBasicRemote().sendText( mensagem );
                userSession.getBasicRemote().sendText(mensagem );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void close(Session userSession){
        String username = getUsername(userSession);
        usernameSession.remove(username);
        String mensagem = formataMensagemToJson( username + " saiu", NO_USERNAME , TiposMensagem.SISTEMA );
        broadcastMessage( mensagem , userSession.getOpenSessions() );
    }
    
    public String getUsername(Session session){
        return (String) session.getUserProperties().get("username");
    }
    
    public String formataMensagemToJson(String mensagem , String usuario , TiposMensagem tipo){
        return "{ \"mensagem\" : \"" + mensagem + "\", \"usuario\" : \"" + usuario + "\", \"tipo\" : \"" + tipo + "\" }";
    }
    
    public boolean isMensagemPrivada(String mensagem){
        Matcher m = Pattern.compile("^\\\\.[\\w\\.]+").matcher(mensagem);
        return m.find();
    }
    
    public String getDestinatarioMensagemPrivada(String mensagem){
        Matcher m = Pattern.compile("^\\\\.[\\w\\.]+").matcher(mensagem);
        if(m.find()){
            String destinatario = m.group();
            return destinatario.substring(1 , destinatario.length() );
        }
        return  "";
    }
    
    public void broadcastMessage( String mensagem , Set<Session> sessions){
        for (Session session : sessions ) {
            try {
                session.getBasicRemote().sendText( mensagem );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}