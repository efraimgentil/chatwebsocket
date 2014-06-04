Chat Websocket
==============

A simple chat created using JavaEE7 Websocket Specification

## Why use websockets

After the initial handshake a channel of communication will be open between the server and the client. A communication event ( like a message or status update as example) can be shot by both ways, from the server or from the client, with that the client wont need to ask ( make requests ) if there any modification in the server, the server notify the client when necessary.


## Creating a websocket server endpoint

To create a websocket server endpoint, that is no more than your server side code that will handle the events of communication from the client and the server, you need to annotate your class with the @ServerEndpoint annotation and pass the path that will be the entrypoint to the communication with your server side app.
You also need to create a method with a String as parameter, and this method have to be annotated with @OnMessage, this method will handle te communication events between the client and the server.

```java
@ServerEndpoint("/my-endpoint")
public class MyEndpoint {
  
  @OnMessage
  public void mensagem(String mensagem){
     //Do something with your message      
  }
  
}
```

## Tecnologies in use
- Java 7
- WebSocketAPI - JSR 356
- Servlets/JSP
- Maven
- JavaScript
