package br.com.efraimgentil.chat_websocket.websocket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.websocket.Session;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChatTest {
    
    private Chat chat;
    
    @Before
    public void setup(){
        chat = new Chat();
    }
    
    @Test
    public void dadoUmaMensagemDeveRetornarTrueCasoSejaPrivada(){
        
        boolean isMensagemPrivada = chat.isMensagemPrivada("\\nick isso é uma mensagem privada");
        
        assertTrue("Deveria retornar que é uma mensagem privada" , isMensagemPrivada );
    }
    
    @Test
    public void dadoUmaMensagemDeveRetornarFalseCasoNaoSejaPrivada(){
        boolean isMensagemPrivada = chat.isMensagemPrivada("mensagem publica");
        
        assertFalse("Deveria retornar que é uma mensagem publica" , isMensagemPrivada );
    }
    
    
}
