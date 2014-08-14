package br.com.efraimgentil.chat_websocket.speakwithme;

import java.io.IOException;

import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import br.com.efraimgentil.chat_websocket.model.User;
import br.com.efraimgentil.chat_websocket.model.constant.UserType;

public class Chat {
  
  private UserSession owner;
  
  public void connectUser( User user , Session session ){
    UserSession userSession = new UserSession(user, session);
    if( isOwner(user) ){
      owner = userSession;
    }else{
      
    }
  }
  
  public void receiveMessage(String message , Session session ) throws IOException{
    String userId = (String) session.getUserProperties().get("USER_ID");
    UserSession userSession = getUserSession(userId);
    User user = userSession.getUser();
    if( isOwner( userSession.getUser() ) ){
      //OWNER SEND MESSAGE TO A SPECIFIC USER
    }else{
      Basic basicRemote = owner.getSession().getBasicRemote();
      if( owner.getSession().isOpen() ){
        basicRemote.sendText( createMessage(message, user) );
      }else{
        //MSG ERROR OWNER IS NOT LOGGED IN
      }
    }
  }
  
  
  
  private String createMessage(String message , User user ){
    return null;
  }

  private UserSession getUserSession(String userId) {
    return null;
  }
  
  private boolean isOwner(User user){
    return UserType.OWNER.equals( user.getUserType() );
  }

}