package br.com.efraimgentil.chat_websocket.chat;


import java.io.IOException;
import java.util.Map;

import javax.websocket.RemoteEndpoint;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.efraimgentil.chat_websocket.chat.exception.UsuarioEmUsoException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatTest {
    
    private Chat chat;
    @Mock private Session session;
    @Mock private UsuarioPool usuarioPool;
    @Mock private FormatadorMensagem formatador;
    
    @Before
    public void setup(){
        chat = new Chat( usuarioPool , formatador );
    }
    
    @Test(expected = UsuarioEmUsoException.class )
    public void dadoUmUsuarioEmUsoDeveLancarExcecao() throws UsuarioEmUsoException, IOException{
        final String USUARIO_EM_USO = "";
        when( usuarioPool.usuarioLivre( anyString() ) ).thenReturn(false);
        
        chat.conectarUsuario( USUARIO_EM_USO , session);
    }
    
    @Test
    public void dadoUmUsuarioLivreDeveAdicionarAoPool() throws UsuarioEmUsoException, IOException{
        final String USUARIO = "usuario";
        Basic remote = mock( Basic.class ); 
        when( usuarioPool.usuarioLivre( anyString() ) ).thenReturn(true);
        when( formatador.formatar( anyString() , anyString() , anyString() )).thenReturn("");
        when( session.getBasicRemote() ).thenReturn( remote );
        
        chat.conectarUsuario(USUARIO, session);
        
        verify( usuarioPool , times(1) ).adicionar(USUARIO, session);
        verify( remote , atLeastOnce() ).sendText( anyString() );
        
    }
    
    @Test
    public void dadoUmUsuarioLivreDeveEnviarMensagemDeBoasVindas() throws UsuarioEmUsoException, IOException{
        final String USUARIO = "usuario";
        Basic remote = mock( Basic.class ); 
        when( usuarioPool.usuarioLivre( anyString() ) ).thenReturn(true);
        when( formatador.formatar( anyString() , anyString() , anyString() )).thenReturn("");
        when( session.getBasicRemote() ).thenReturn( remote );
        
        chat.conectarUsuario(USUARIO, session);
        
        verify( remote , atLeastOnce() ).sendText( anyString() );
    }
    
    @Test
    public void dadoUmUsuarioLivreDeveSetarONomeDoUsuarioNaSessao() throws UsuarioEmUsoException, IOException{
        final String USUARIO = "usuario";
        Basic remote = mock( Basic.class ); 
        Map<String, Object> userProperties = mock(Map.class);
        when( usuarioPool.usuarioLivre( anyString() ) ).thenReturn(true);
        when( formatador.formatar( anyString() , anyString() , anyString() )).thenReturn("");
        when( session.getBasicRemote() ).thenReturn( remote );
        when( session.getUserProperties() ).thenReturn( userProperties ); 
        
        chat.conectarUsuario(USUARIO, session);
        
        verify( userProperties, times(1) ).put(  Chat.ATTR_USUARIO ,  USUARIO);
    }
    
    
}
