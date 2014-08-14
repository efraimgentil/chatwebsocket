package br.com.efraimgentil.chat_websocket.websocket.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import br.com.efraimgentil.chat_websocket.model.Message;

public class MessageEncoder implements Encoder.Text<Message> {

  @Override
  public void init(EndpointConfig config) {
    // TODO Auto-generated method stub
  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub
  }

  @Override
  public String encode(Message object) throws EncodeException {
    // TODO Auto-generated method stub
    return null;
  }

}
