package br.com.efraimgentil.chat_websocket.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.efraimgentil.chat_websocket.model.User;
import br.com.efraimgentil.chat_websocket.persistence.UserDAO;
import br.com.efraimgentil.chat_websocket.web.util.RequestToObject;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  
  @Inject
  private UserDAO userDAO;
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    HttpSession session = req.getSession(true);
    session.setAttribute("authenticated", "YEAH BABY");
    super.doGet(req, resp);
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    
    User user = RequestToObject.readObjectInRequest( User.class , req  , "user");
    User authenticatedUser = userDAO.userByEmailAndPassword( user.getEmail() , user.getPassword() );
    if(authenticatedUser != null){
      HttpSession session = req.getSession(true);
      session.setAttribute(  AuthenticatedUser.AUTHENTICATED_KEY , true );
      session.setAttribute(  AuthenticatedUser.USER_KEY , authenticatedUser );
      resp.sendRedirect("");
      return;
    }else{
      RequestDispatcher rd = req.getRequestDispatcher("/welcome.jsp");
      req.setAttribute("type","singon");
      req.setAttribute("message","Email and/or password invalid");
      req.setAttribute("user", user);
      rd.forward(req, resp);
    }
  }
  
}
