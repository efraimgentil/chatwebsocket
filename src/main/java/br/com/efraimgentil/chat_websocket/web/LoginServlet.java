package br.com.efraimgentil.chat_websocket.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.efraimgentil.chat_websocket.model.User;
import br.com.efraimgentil.chat_websocket.web.util.RequestToObject;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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
    System.out.println( user );
    
    super.doPost(req, resp);
  }
  
}
