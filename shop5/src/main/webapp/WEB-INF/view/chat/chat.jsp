<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /webapp/WEB-INF/view/chat/chat.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket client</title>
<c:set var="port" value="${pageContext.request.localPort }" />		<%--8080 --%>
<c:set var="server" value="${pageContext.request.serverName }" />	<%--localhost --%>
<c:set var="path" value="${pageContext.request.contextPath }" />	<%--shop3 --%>
<script>
	$(function() {
		var ws = new WebSocket("ws://${server}:${port}${path}/chatting.shop");
		ws.onopen = function() {
			$("#chatStatus").text("info.connection opened")
			$("input[name=chatInput]").on("keydown",function(evt) {
				if(evt.keyCode == 13) {
					var msg = $("input[name=chatInput]").val();
					ws.send(msg);	//서버로 입력된 내용을 전송
					$("input[name=chatInput]").val("");
				}
			})
		}
		//서버에서 메세지를 수신한 경우(prepend : 제일 윗단에 작성)
		ws.onmessage = function(event) {
			//event.data : 서버에서 보내준 message
			$("textarea").eq(0).prepend(event.data + "\n");
		}
		//서버와 연결이 끊어진 경우
		ws.onclose = function(event) {
			$("#chatStatus").text("info:connection close");
		}
	})
</script>
</head>
<body>
	<p>
	<div id="chatStatus"></div>
	<textarea name="chatMsg" rows="15" cols="40"></textarea><br>
	메세지 입력 : <input type="text" name="chatInput">
</body>
</html>