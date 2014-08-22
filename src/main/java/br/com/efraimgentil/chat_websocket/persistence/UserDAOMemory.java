package br.com.efraimgentil.chat_websocket.persistence;

import br.com.efraimgentil.chat_websocket.model.User;
import br.com.efraimgentil.chat_websocket.model.constant.UserType;

public class UserDAOMemory implements UserDAO {

  @Override
  public User userByEmailAndPassword(String email, String password) {
    if("efraim.gentil@gmail.com".equals( email ) && "secretlol".equals(password) ){
      return owner();
    }else if("123654".equals(password)){
      return guest(email);
    }
    return null;
  }
  
  private User owner(){
    User u = new User();
    u.setEmail("efraim.gentil@gmail.com");
    u.setUsername("efraimgentil");
    u.setUserType( UserType.OWNER );
    return u;
  }
  
  private User guest(String email){
    User u = new User();
    u.setEmail(email);
    u.setUsername(email);
    u.setUserType( UserType.GUEST );
    return u;
  }
  

}
