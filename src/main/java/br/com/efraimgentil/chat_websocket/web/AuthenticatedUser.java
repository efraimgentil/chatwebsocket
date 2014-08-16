package br.com.efraimgentil.chat_websocket.web;

import javax.servlet.http.HttpSession;

public class AuthenticatedUser {

  public static final String AUTHENTICATED_KEY = "authenticated";
  public static final String USER_KEY = "user";
  
  private HttpSession session;
  
  public AuthenticatedUser(HttpSession httpSession) {
    this.session = httpSession;
  }
  
  public boolean isAuthenticated(){
    return session.getAttribute(AUTHENTICATED_KEY) != null;
  }

}
