package br.com.efraimgentil.chat_websocket.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import br.com.efraimgentil.chat_websocket.chat.Chat;
import br.com.efraimgentil.chat_websocket.chat.exception.UsuarioEmUsoException;

/**
 * 
 * @author Efraim Gentil (efraim.gentil@gmail.com)
 */
@ServerEndpoint("/chat/{user_name}")
public class ChatServerEndpoint {
    
    private Chat chat;
    
    public ChatServerEndpoint() {
        chat = new Chat();
    }
    
    public ChatServerEndpoint(Chat chat) {
        this.chat = chat;
    }
    
    @OnOpen
    public void abrir(@PathParam("user_name") String usuario,  Session session){
        try{
            try {
                chat.conectarUsuario(usuario, session);
            } catch (UsuarioEmUsoException e) {
                session.close( new CloseReason( CloseCodes.VIOLATED_POLICY  , "Desculpe mas o usuario informado já está em uso") );
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @OnMessage
    public void mensagem(String mensagem , Session userSession){
        chat.trataRecebimentoMensagem(mensagem, userSession );
    }
    
    @OnClose
    public void fechar(Session userSession){
        chat.desconectarUsuario(userSession);
    }
    
}