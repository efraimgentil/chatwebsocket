package br.com.efraimgentil.chat_websocket.persistence;

import br.com.efraimgentil.chat_websocket.model.User;

public interface UserDAO {
  
  
  public User userByEmailAndPassword(String email , String password);
  
}
