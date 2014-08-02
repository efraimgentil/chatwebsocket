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
    <h1>app.name <fmt:message key="app.version" /></h1>
   
    <div >
        <form id="login-form" action="" onsubmit="return false;">
            <div>
                <label for="usuario" >Usuário</label>
                <input type="text" id="usuario" name="usuario" />
            </div>
            <div>
                <label for="senha" >Senha</label>
                <input type="text" id="senha" name="senha" />
            </div>
            <div>        
                <button id="btn-enviar" >Enviar</button>
                <button id="btn-limpar" type="reset">Limpar</button>
            </div>
            <div>
                <span>Entrar com:</span>
                <a href="#">G+</a>
                <a href="#">F</a>
                <a href="#">T</a>
            </div>
        </form>
    </div>
</body>
</html>
