package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import websocket.EchoHandler;

@Configuration
@EnableWebSocket		//websocket 관련 설정
public class WebSocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//chatting.shop 요청이 들어오면 EchoHandler 객체를 실행하도록 설정.
		registry.addHandler(new EchoHandler(), "chatting.shop").setAllowedOrigins("*");
	}
}