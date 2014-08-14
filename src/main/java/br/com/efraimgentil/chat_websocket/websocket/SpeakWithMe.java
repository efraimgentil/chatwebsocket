package br.com.efraimgentil.chat_websocket.websocket;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import br.com.efraimgentil.chat_websocket.websocket.config.CustomConfigurator;

@ServerEndpoint(value = "/speak/"
, configurator = CustomConfigurator.class)
public class SpeakWithMe {
  
  @OnOpen
  public void open(){
    System.out.println("OPEN");
  }
  
  @OnMessage
  public void receiveMessage(String message){
    System.out.println("MESSAGE");
  }
  
  @OnError
  public void error(Throwable e){
    System.out.println( " NA NA NA ");
  }
  
  

}
