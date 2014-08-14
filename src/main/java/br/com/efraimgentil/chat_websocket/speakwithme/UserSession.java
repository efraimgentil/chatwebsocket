package br.com.efraimgentil.chat_websocket.speakwithme;

import javax.websocket.Session;

import br.com.efraimgentil.chat_websocket.model.User;

public class UserSession {
  
  private User user;
  private Session session;

  public UserSession(User user, Session session) {
    super();
    this.user = user;
    this.session = session;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Session getSession() {
    return session;
  }

  public void setSession(Session session) {
    this.session = session;
  }

}