<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chat WebSocket</title>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/chat_websocket.css" />
</head>
<body>
    <fmt:setBundle basename="messages"/>
    <h1>Chat WebSocket - <fmt:message key="app.version" /></h1>
    <p>Essa é uma aplicação de exemplo da implementação da especificação JSR 356</p>
    
    <div id="chat-container">
        <div id="chat-area">
        </div>
    </div>
    <div >
        <button id="btn-conectar">Conectar ao Chat</button>
        <button id="btn-desconectar" style="display:none"  >Desconectar</button>
        <form action="" onsubmit="return false;">
            <div>
                <input type="text"  id="textarea" />
            </div>
            <div>        
                <button id="btn-enviar" >Enviar</button>
                <button id="btn-limpar" type="reset">Limpar</button>
            </div>
        </form>
    </div>
    <script type="text/javascript">
         window.onload = function(){
        	 var chat = null; 
        	 
        	 var btnConectar = document.getElementById("btn-conectar");
        	 btnConectar.onclick = function(){
        		var usuario = prompt("Conectar com qual nome ?",null);
        		chat = new Chat();
        		chat.conectar(usuario);
        		habilitaDesconectar();
        	 };
        	 
        	 var btnDesconectar = document.getElementById("btn-desconectar");
        	 btnDesconectar.onclick = function(){
        		 var confirmacao = confirm("Tem certeza que deseja desconectar?");
        		 if(confirmacao){
        		  chat.fecharConexao();
        		  habilitaBotaoConectar();
        		 }
        	 };
        	 
        	 var btnEnviar = document.getElementById("btn-enviar");
        	 btnEnviar.onclick = function(){
        		 var textarea = document.getElementById("textarea");
        		 chat.enviarMensagem( textarea.value );
        		 textarea.value = "";
        	 };
        	 
        	 window.onbeforeunload = function(){
        		 console.log(" THERE SOMETHING WRONG ");
        		 return confirm;
        	 };
         };
         
         function Chat(){
        	 this.wsUri = "ws://" + location.host + "${pageContext.request.contextPath}/chat"; 

        	 this.conectar = function(usuario){
        		 this.websocket = new WebSocket(this.wsUri + "/" + usuario);
        		 this.websocket.onopen = this.abrindoConexao;
        		 this.websocket.onclose = this.fechandoConexao;
        		 this.websocket.onmessage = this.receberMensagem;
        	 };
        	 
        	 this.abrindoConexao = function(evt){
        		 adicionaTextoAoChat( criarMensagemSistema( "Tentando conectar...") );
        	 };
        	 
        	 this.enviarMensagem = function(mensagem){
        		 this.websocket.send(mensagem);
        	 };
        	 
        	 this.fecharConexao = function(){
        		 this.websocket.close();
        	 };
        	 
        	 this.receberMensagem = function(evt){
        		 adicionaTextoAoChat( trataMensagem( evt.data )  );
        	 };
        	 
        	 this.fechandoConexao = function(evt){
        		 console.log(evt);
        		 if(evt.code == '1006'){
                     adicionaTextoAoChat( criarMensagemSistema("### Erro inesperado, conexão fechada") );
        		 }else{
        			 if(evt.code == '1008'){
        				 adicionaTextoAoChat( criarMensagemSistema("### " + evt.reason) );
        			 }else{
        				 adicionaTextoAoChat( criarMensagemSistema("### Conexão encerrada") );
        			 }
        		 }
        		 habilitaBotaoConectar();
        	 };        	 
         };
         
         function trataMensagem(mensagem){
        	 var jsonMensagem = JSON.parse(mensagem);
        	 var tipo = jsonMensagem.tipo.toLowerCase();
             var p = document.createElement("p");
             if(tipo != "sistema"){
                p.innerHTML = jsonMensagem.usuario + ": " + jsonMensagem.mensagem ;
             }else{
                p.innerHTML = "### " + jsonMensagem.mensagem;
             }
             p.className = tipo;
             return p;
         }
         
         function criarMensagemSistema(mensagem){
        	 var p = document.createElement("p");
        	 p.className = "sistema";
        	 p.innerHTML = "### " + mensagem;
        	 return p;
         }
         
         function adicionaTextoAoChat( p ){
        	 var chatArea = document.getElementById("chat-area");
             chatArea.appendChild( p );
             rolarParaOFinal();
         }
         
         function rolarParaOFinal(){
        	 var chatContainer = document.getElementById("chat-container");
             chatContainer.scrollTop = chatContainer.scrollHeight;        	 
         }
         
         function habilitaBotaoConectar(){
             var btnDesconectar = document.getElementById("btn-desconectar");
             btnDesconectar.style.display = "none";  
             document.getElementById("btn-conectar").style.display = "block";
         }
         
         function habilitaDesconectar(){
             var btnConectar = document.getElementById("btn-conectar");
             btnConectar.style.display = "none";
             document.getElementById("btn-desconectar").style.display = "block";
         }
         
    </script>
</body>
</html>
