package br.com.efraimgentil.chat_websocket.web;

import javax.servlet.http.HttpSession;

public class AuthenticatedUser {

  private final String AUTHENTICATED_KEY = "authenticated";
  
  private HttpSession session;
  
  public AuthenticatedUser(HttpSession httpSession) {
    this.session = httpSession;
  }
  
  public boolean isAuthenticated(){
    return session.getAttribute(AUTHENTICATED_KEY) != null;
  }

}
