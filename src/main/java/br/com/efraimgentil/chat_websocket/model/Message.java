package br.com.efraimgentil.chat_websocket.model;

import java.io.Serializable;
import java.util.Date;

import br.com.efraimgentil.chat_websocket.model.constant.MessageType;

public class Message implements Serializable {

  private static final long serialVersionUID = -5717764420030621011L;
  
  private String userWhoSend;
  private Date date;
  private String body;
  private MessageType type;
 
  public Message() {  }
  
  public Message(String userWhoSend, Date date, String body) {
    super();
    this.userWhoSend = userWhoSend;
    this.date = date;
    this.body = body;
  }
  
  public static Message infoMessage(String body){
    Message m = new Message();
    m.userWhoSend = "SYSTEM";
    m.date = new Date();
    m.body = body;
    m.type = MessageType.INFO;
    return m;
  }
  
  
  public String getUserWhoSend() {
    return userWhoSend;
  }
  public void setUserWhoSend(String userWhoSend) {
    this.userWhoSend = userWhoSend;
  }
  public Date getDate() {
    return date;
  }
  public void setDate(Date date) {
    this.date = date;
  }
  public String getBody() {
    return body;
  }
  public void setBody(String body) {
    this.body = body;
  }
  public MessageType getType() {
    return type;
  }
  public void setType(MessageType type) {
    this.type = type;
  }

}