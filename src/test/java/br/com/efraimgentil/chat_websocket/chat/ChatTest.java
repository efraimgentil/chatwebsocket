package br.com.efraimgentil.chat_websocket.chat;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.efraimgentil.chat_websocket.chat.exception.SemDestinatarioException;
import br.com.efraimgentil.chat_websocket.chat.exception.UsuarioEmUsoException;

/**
 * 
 * @author Efraim Gentil (efraim.gentil@gmail.com)
 */
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
    
    @SuppressWarnings("unchecked")
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
    
    @Test
    public void dadoUmUsuarioEASessaoDeveEnviarMensagemDeBoasVindasParaOMesmo () throws IOException{
        final String USUARIO = "USUARIO_CHAT";
        Basic remote = mock( Basic.class ); 
        when( session.getOpenSessions() ).thenReturn( new LinkedHashSet<Session>() );
        when( session.getBasicRemote() ).thenReturn( remote );
        
        chat.enviarMensagemBoasVindas( USUARIO , session);
        
        verify( formatador , atLeastOnce() ).formatar( "Conectou com sucesso" , Chat.NO_USERNAME , TiposMensagem.SISTEMA.toString() );
        verify( remote , times(1) ).sendText( anyString() );
    }
    
    @Test
    public void dadoUmUsuarioEASessaoDeveEnviarAvisoQueUsuarioSeConectouParaAsOutrasSessoesAbertas() throws IOException{
        final String USUARIO = "USUARIO_CHAT";
        Basic remote = mock( Basic.class ); 
        Set<Session> sessoes = new LinkedHashSet<Session>();
        Session otherUserSession = mock(Session.class);
        sessoes.add( otherUserSession );
        when( session.getOpenSessions() ).thenReturn( sessoes );
        when( session.getBasicRemote() ).thenReturn( remote );
        when( otherUserSession.getBasicRemote() ).thenReturn( remote );
        
        chat.enviarMensagemBoasVindas( USUARIO , session);
        
        verify( formatador , atLeastOnce() ).formatar( USUARIO + " conectou" , Chat.NO_USERNAME , TiposMensagem.SISTEMA.toString() );
        verify( remote , times(2) ).sendText( anyString() );
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void dadoUmaSessaoDeveRetornarOutrasSessoesAbertasExluindoASessaoDada() throws IOException{
        Set<Session> sessoes = mock(Set.class);
        when( session.getOpenSessions() ).thenReturn( sessoes );
        
        chat.sessoesParaAvisoDeConexao(session);
        
        verify( sessoes , times(1) ).remove(session);
    }
    
    @Test
    public void dadoUmaMensagemPrivadaDeveRetornarTrue(){
        String mensagemPrivada = "\\usuarioQualquer mensagem privada";
        
        boolean eMensagemPrivada = chat.isMensagemPrivada(mensagemPrivada);
        
        assertTrue("Deveria retornar confirmacao que é uma mensagem privada", eMensagemPrivada);
    }
    
    @Test
    public void dadoUmaMensagemPublicaDeveRetornarFalse(){
        String mensagemPrivada = "mensagem privada";
        
        boolean eMensagemPrivada = chat.isMensagemPrivada(mensagemPrivada);
        
        assertFalse("Deveria retornar que não é uma mensagem privada", eMensagemPrivada);
    }
    
    @Test
    public void dadoUmaMensagemPrivadaDeveRetornarONomeDoUsuarioDeDestinatario() throws SemDestinatarioException{
        String USUARIO  = "usuarioQualquer";
        String mensagemPrivada = "\\" + USUARIO + " mensagem privada";
        
        String usuarioDestinatario = chat.getDestinatarioMensagemPrivada(mensagemPrivada);
        
        assertEquals("Deveria retornar o usuário destinatario da mensagem", USUARIO , usuarioDestinatario);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = SemDestinatarioException.class)
    public void dadoUmMensagemPrivadaQueNaoSejaPossivelExtrairOUsuarioDestinoDeveLancarExecao() throws SemDestinatarioException{
        String USUARIO  = "";
        String mensagemPrivada = "\\" + USUARIO + " mensagem privada";
        
        String usuarioDestinatario = chat.getDestinatarioMensagemPrivada(mensagemPrivada);
        //nao deve passar desse ponto
    }
}