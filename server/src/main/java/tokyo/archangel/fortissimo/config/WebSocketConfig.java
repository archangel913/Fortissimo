package tokyo.archangel.fortissimo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import tokyo.archangel.fortissimo.interceptor.FortiwssimoAuthInterceptor;

@Configuration
@EnableWebSocketMessageBroker // STOMPメッセージングを有効化
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private FortiwssimoAuthInterceptor authInterceptor;

	public WebSocketConfig(FortiwssimoAuthInterceptor authInterceptor) {
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(authInterceptor);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// Vueなどのクライアントが最初に「接続」しにくるURLを設定
		registry.addEndpoint("/connect")
				.setAllowedOriginPatterns("*")
				.withSockJS(); // CORS対策：すべてのオリジンからの接続を許可
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// サーバーからクライアントへメッセージを「配信」する際のルート前半分（トピック）
		config.enableSimpleBroker("/topic");

		// クライアントからサーバーへメッセージを「送信」する際のルート前半分
		config.setApplicationDestinationPrefixes("/app");
	}
}
