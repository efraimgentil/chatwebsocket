package br.com.efraimgentil.chat_websocket.websocket;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import br.com.efraimgentil.chat_websocket.model.User;
import br.com.efraimgentil.chat_websocket.speakwithme.Chat;
import br.com.efraimgentil.chat_websocket.web.AuthenticatedUser;
import br.com.efraimgentil.chat_websocket.websocket.config.CustomConfigurator;
import br.com.efraimgentil.chat_websocket.websocket.encoder.MessageEncoder;

@ServerEndpoint(value = "/speak/"
, encoders = { MessageEncoder.class }
, configurator = CustomConfigurator.class)
public class SpeakWithMe {
  
  private static final Chat chat;
  static{
    chat = new Chat();
  }
  
  @OnOpen
  public void open(Session session , EndpointConfig config){
    System.out.println("OPEN");
    session.getUserProperties().put( "HTTP_SESSION" , config.getUserProperties().get( "HTTP_SESSION" ) );
    HttpSession httpSession =  (HttpSession) session.getUserProperties().get( "HTTP_SESSION"  );
    User user = (User) httpSession.getAttribute(AuthenticatedUser.USER_KEY );
    try {
      chat.connectUser(user, session);
    } catch (IOException | EncodeException e) {
      e.printStackTrace();
    }
  }
  
  @OnMessage
  public void receiveMessage(String message){
    System.out.println("MESSAGE");
  }
  
  @OnError
  public void error(Throwable e){
    System.out.println( "Sorry but there is a error, closing!" );
    System.out.println( e.getMessage() );
  }
  
  

}
