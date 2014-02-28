package br.com.efraimgentil.chat_websocket.chat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.Session;

import br.com.efraimgentil.chat_websocket.chat.exception.UsuarioEmUsoException;

/**
 * 
 * @author Efraim Gentil (efraim.gentil@gmail.com)
 */
public class Chat {
    
    public static final String ATTR_USUARIO = "usuario";
    public static Map<String, Session> usernameSession = new LinkedHashMap<>();
    
    private final Pattern patternUsuarioDestino = Pattern
            .compile("^\\\\.[\\w\\.]+");
    private final String NO_USERNAME = "";
    private final UsuarioPool usuarioPool;
    private FormatadorMensagem formatadorMensagem;

    public Chat() {
        usuarioPool = UsuarioPool.getInstance();
        formatadorMensagem = new JsonFormatadorMensagem();
    }

    public Chat(UsuarioPool pool, FormatadorMensagem formatadorMensagem) {
        this.usuarioPool = pool;
        this.formatadorMensagem = formatadorMensagem;
    }

    public void conectarUsuario(String usuario, Session session)
            throws UsuarioEmUsoException, IOException {
        if (usuarioPool.usuarioLivre(usuario)) {
            session.getUserProperties().put(ATTR_USUARIO, usuario);
            usuarioPool.adicionar(usuario, session);
            enviarMensagemBoasVindas(usuario, session);
        } else {
            throw new UsuarioEmUsoException();
        }
    }

    protected void enviarMensagemBoasVindas(String usuario, Session session)
            throws IOException {
        Set<Session> sessions = session.getOpenSessions();
        sessions.remove(session);
        String mensagem = usuario + " conectou";
        session.getBasicRemote().sendText(
                formataMensagemToJson("Conectou com sucesso", NO_USERNAME,
                        TiposMensagem.SISTEMA));
        broadcastMessage(
                formataMensagemToJson(mensagem, NO_USERNAME,
                        TiposMensagem.SISTEMA), sessions);
    }

    public void trataRecebimentoMensagem(String mensagem, Session session) {
        String username = getNomeUsuario(session);
        if (isMensagemPrivada(mensagem)) {
            enviaMensagemPrivada(mensagem, session);
        } else {
            mensagem = formataMensagemToJson(mensagem, username,
                    TiposMensagem.PUBLICA);
            broadcastMessage(mensagem, session.getOpenSessions());
        }
    }

    protected boolean isMensagemPrivada(String mensagem) {
        return patternUsuarioDestino.matcher(mensagem).find();
    }

    protected void enviaMensagemPrivada(String mensagem, Session session) {
        String username = getNomeUsuario(session);
        String usuarioDestino = getDestinatarioMensagemPrivada(mensagem);
        Session sessionDestino = usernameSession.get(usuarioDestino);
        try {
            if (sessionDestino != null) {
                mensagem = mensagem.replace("\\" + usuarioDestino, "");
                mensagem = formataMensagemToJson(mensagem, username,
                        TiposMensagem.PRIVADA);
                sessionDestino.getBasicRemote().sendText(mensagem);
                session.getBasicRemote().sendText(mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getDestinatarioMensagemPrivada(String mensagem) {
        Matcher m = patternUsuarioDestino.matcher(mensagem);
        if (m.find()) {
            String destinatario = m.group();
            return destinatario.substring(1, destinatario.length());
        }
        return "";
    }

    public void desconectarUsuario(Session session) {
        String username = getNomeUsuario(session);
        usernameSession.remove(username);
        String mensagem = formataMensagemToJson(username + " saiu",
                NO_USERNAME, TiposMensagem.SISTEMA);
        broadcastMessage(mensagem, session.getOpenSessions());
    }

    protected void broadcastMessage(String mensagem, Set<Session> sessions) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(mensagem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected String formataMensagemToJson(String mensagem, String usuario,
            TiposMensagem tipo) {
        return formatadorMensagem.formatar(mensagem, usuario, tipo.toString());
    }

    protected String getNomeUsuario(Session session) {
        return (String) session.getUserProperties().get(ATTR_USUARIO);
    }

}