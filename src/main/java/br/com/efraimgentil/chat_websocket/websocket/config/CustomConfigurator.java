package br.com.efraimgentil.chat_websocket.websocket.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import br.com.efraimgentil.chat_websocket.web.AuthenticatedUser;
import br.com.efraimgentil.chat_websocket.websocket.SpeakWithMe;

public class CustomConfigurator extends ServerEndpointConfig.Configurator {
  
  @Override
  public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    System.out.println("HANDSHAKE");
    HttpSession httpSession = (HttpSession) request.getHttpSession();
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(httpSession);
    if( authenticatedUser.isAuthenticated() ){
      sec.getUserProperties().put("HTTP_SESSION",  httpSession );
      sec.getUserProperties().put("hey", "SUB NIGA");
    }else{
//      response.getHeaders().put( response.SEC_WEBSOCKET_ACCEPT , new ArrayList<String>() );
      throw new RuntimeException(" NIH ");
    }
  }
  
  
}
