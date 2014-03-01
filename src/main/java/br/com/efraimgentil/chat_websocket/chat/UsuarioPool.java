package br.com.efraimgentil.chat_websocket.chat;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.websocket.Session;

public class UsuarioPool {
    
    private static UsuarioPool pool;
    public static Map<String, Session> usernameSession = new LinkedHashMap<>();
    
    private UsuarioPool(){
        
    }
    
    public static UsuarioPool getInstance(){
        if(pool == null){
            pool = new UsuarioPool();
        }
        return pool;
    }
    
    public boolean usuarioLivre(String usuario){
        return !usernameSession.containsKey(usuario);
    }

    public void adicionar(String usuario, Session session) {
        usernameSession.put(usuario, session);
    }
    
    public void remover(String usuario){
        usernameSession.remove(usuario);
    }
    
    public Session sessaoDoUsuario(String usuario){
        return usernameSession.get(usuario);
    }
    
    
}
