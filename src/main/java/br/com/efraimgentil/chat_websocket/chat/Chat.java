package br.com.efraimgentil.chat_websocket.chat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.Session;

import br.com.efraimgentil.chat_websocket.chat.exception.SemDestinatarioException;
import br.com.efraimgentil.chat_websocket.chat.exception.SessaoDestinoNaoEncontrada;
import br.com.efraimgentil.chat_websocket.chat.exception.UsuarioEmUsoException;

/**
 * 
 * @author Efraim Gentil (efraim.gentil@gmail.com)
 */
public class Chat {

    public static final String ATTR_USUARIO = "usuario";
    public static final String NO_USERNAME = "";
//    public static Map<String, Session> usernameSession = new LinkedHashMap<>();

    private final Pattern patternUsuarioDestino = Pattern
            .compile("^\\\\[\\w\\.]+");

    private final UsuarioPool usuarioPool;
    private FormatadorMensagem formatadorMensagem;

    public Chat() {
        usuarioPool = UsuarioPool.getInstance();
        formatadorMensagem = new JsonFormatadorMensagem();
    }

    public Chat(UsuarioPool pool, FormatadorMensagem formatadorMensagem) {
        this.usuarioPool = pool;
        this.formatadorMensagem = formatadorMensagem;
    }

    public void conectarUsuario(String usuario, Session session)
            throws UsuarioEmUsoException, IOException {
        if (usuarioPool.usuarioLivre(usuario)) {
            session.getUserProperties().put(ATTR_USUARIO, usuario);
            usuarioPool.adicionar(usuario, session);
            enviarMensagemBoasVindas(usuario, session);
        } else {
            throw new UsuarioEmUsoException();
        }
    }

    protected void enviarMensagemBoasVindas(String usuario, Session session)
            throws IOException {
        session.getBasicRemote().sendText(
                formataMensagemToJson("Conectou com sucesso", NO_USERNAME,
                        TiposMensagem.SISTEMA));
        Set<Session> sessions = sessoesParaAvisoDeConexao(session);
        String mensagem = usuario + " conectou";
        broadcastMessage(
                formataMensagemToJson(mensagem, NO_USERNAME,
                        TiposMensagem.SISTEMA), sessions);
    }

    protected Set<Session> sessoesParaAvisoDeConexao(Session session) {
        Set<Session> sessions = session.getOpenSessions();
        sessions.remove(session);
        return sessions;
    }

    public void trataRecebimentoMensagem(String mensagem, Session session) throws IOException {
        String username = getNomeUsuario(session);
        if (isMensagemPrivada(mensagem)) {
            enviaMensagemPrivada(mensagem, session);
        } else {
            mensagem = formataMensagemToJson(mensagem, username,
                    TiposMensagem.PUBLICA);
            broadcastMessage(mensagem, session.getOpenSessions());
        }
    }

    protected boolean isMensagemPrivada(String mensagem) {
        return patternUsuarioDestino.matcher(mensagem).find();
    }

    protected void enviaMensagemPrivada(String mensagem, Session session)
            throws IOException {
        String username = getNomeUsuario(session);
        try {
            String usuarioDestino = getDestinatarioMensagemPrivada(mensagem);
            Session sessionDestino = usuarioPool.sessaoDoUsuario( usuarioDestino );
            if (sessionDestino != null) {
                mensagem = mensagem.replace("\\" + usuarioDestino, "");
                mensagem = formataMensagemToJson(mensagem, username,
                        TiposMensagem.PRIVADA);
                sessionDestino.getBasicRemote().sendText(mensagem);
                session.getBasicRemote().sendText(mensagem);
            }else{
                throw new SessaoDestinoNaoEncontrada();
            }
        } catch (SemDestinatarioException sde) {
            session.getBasicRemote().sendText(
                    formataMensagemToJson( "Usuário de destino não encontrado" , username,
                            TiposMensagem.SISTEMA));
        } catch (SessaoDestinoNaoEncontrada e) {
            session.getBasicRemote().sendText(
                    formataMensagemToJson( "Desculpe mas é provavel que o usuario esteja offline" , username,
                            TiposMensagem.SISTEMA));
        }
    }

    protected String getDestinatarioMensagemPrivada(String mensagem)
            throws SemDestinatarioException {
        Matcher m = patternUsuarioDestino.matcher(mensagem);
        if (m.find()) {
            String destinatario = m.group();
            return destinatario.substring(1, destinatario.length());
        }
        throw new SemDestinatarioException();
    }

    public void desconectarUsuario(Session session) {
        String usuario = getNomeUsuario(session);
        usuarioPool.remover(usuario);
        String mensagem = formataMensagemToJson(usuario + " saiu",
                NO_USERNAME, TiposMensagem.SISTEMA);
        broadcastMessage(mensagem, session.getOpenSessions());
    }

    protected void broadcastMessage(String mensagem, Set<Session> sessions) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(mensagem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected String formataMensagemToJson(String mensagem, String usuario,
            TiposMensagem tipo) {
        return formatadorMensagem.formatar(mensagem, usuario, tipo.toString());
    }

    protected String getNomeUsuario(Session session) {
        return (String) session.getUserProperties().get(ATTR_USUARIO);
    }

}