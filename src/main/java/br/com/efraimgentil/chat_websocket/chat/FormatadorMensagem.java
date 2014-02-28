package br.com.efraimgentil.chat_websocket.chat;

public interface FormatadorMensagem {
    
    String formatar(String mensagem , String usuario , String tipo);
    
}
