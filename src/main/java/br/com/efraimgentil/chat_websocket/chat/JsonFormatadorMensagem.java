package br.com.efraimgentil.chat_websocket.chat;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class JsonFormatadorMensagem implements FormatadorMensagem {

    @Override
    public String formatar(String mensagem, String usuario, String tipo) {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject()
            .write("mensagem" , mensagem )
            .write("usuario" , usuario )
            .write("tipo" , tipo )
            .writeEnd()
        .flush();
        return writer.toString();
    }

}
