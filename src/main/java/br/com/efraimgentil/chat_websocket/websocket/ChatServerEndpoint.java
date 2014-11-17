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
 * @author Efraim Gentil (efraim.gentil@gmail.com)
 * 
 * Anotação @ServerEndpoint sinaliza a rota de entrada no websocket
 * no momento do startup da aplicação a anotação sera identificada e o
 * endpoint validado e preparado para uso
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

  /**
   * No momento em que um usuário se conecta ao enpoint, o metodo anotado
   * com a anotação @OnOpen será chamado, sinalizando o inicio de uma nova conexão com 
   * um cliente
   * @param usuario
   * @param session
   */
  @OnOpen
  public void abrir(@PathParam("user_name") String usuario, Session session) {
    try {
      try {
        chat.conectarUsuario(usuario, session);
      } catch (UsuarioEmUsoException e) {
        session.close(new CloseReason(CloseCodes.VIOLATED_POLICY,
            "Desculpe mas o usuario informado já está em uso"));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  
  /**
   * Toda mensagem enviada por um cliente passara por esse metodo, obrigatóriamente
   * o metodo deve ter um parametro do tipo byte[] ou string que recebera a mensagem
   * o Session é opcional, mas serve para ter a referencia de quem enviou a mensagem 
   * @param mensagem
   * @param userSession
   */
  @OnMessage
  public void mensagem(String mensagem, Session userSession) {
    try {
      chat.trataRecebimentoMensagem(mensagem, userSession);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * No momento em que um cliente fecha a sessão o metodo anotado com @OnClose sera
   * chamado
   * @param userSession
   */
  @OnClose
  public void fechar(Session userSession) {
    chat.desconectarUsuario(userSession);
  }

}
