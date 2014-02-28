package br.com.efraimgentil.chat_websocket.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.efraimgentil.chat_websocket.chat.Chat;
import br.com.efraimgentil.chat_websocket.chat.exception.UsuarioEmUsoException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatServerEndpointTest {
    
    private ChatServerEndpoint chatServerEndpoint;
    @Mock private Chat chat;
    
    @Before
    public void setup(){
        chatServerEndpoint = new ChatServerEndpoint(chat);
    }
    
    @Test
    public void dadoUmUsuarioEmUsoDeveFecharSessao() throws UsuarioEmUsoException, IOException{
        String USUARIO_REPETIDO = "";
        Session session = mock(Session.class);
        doThrow( new UsuarioEmUsoException() ).when( chat ).conectarUsuario( anyString() , any(Session.class));
        
        chatServerEndpoint.abrir( USUARIO_REPETIDO , session);
        
        verify( chat, atLeastOnce() ).conectarUsuario(USUARIO_REPETIDO, session);
        verify( session , atLeastOnce() ).close( any( CloseReason.class ) );
    }
    
    
}
