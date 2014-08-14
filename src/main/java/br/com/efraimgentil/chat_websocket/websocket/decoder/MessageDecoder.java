package br.com.efraimgentil.chat_websocket.websocket.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import br.com.efraimgentil.chat_websocket.model.Message;

public class MessageDecoder implements Decoder.Text<Message> {

  @Override
  public void init(EndpointConfig config) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public Message decode(String s) throws DecodeException {
    return null;
  }

  @Override
  public boolean willDecode(String s) {
    
    return false;
  }


}
