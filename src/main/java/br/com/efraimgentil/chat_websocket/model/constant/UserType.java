package br.com.efraimgentil.chat_websocket.model.constant;

public enum UserType {
  
  OWNER("OWNER"),
  GUEST("GUEST");
  
  private String value;
  
  private UserType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
}
