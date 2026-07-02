package tokyo.archangel.fortissimo.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import tokyo.archangel.fortissimo.dto.Session;
import tokyo.archangel.fortissimo.servicies.SessionService;

@Component
public class FortiwssimoAuthInterceptor implements ChannelInterceptor {
	private SessionService sessionService;

	public FortiwssimoAuthInterceptor(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// STOMPのヘッダー情報にアクセスするためのアクセサーを取得
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor == null) {
			return message;
		}

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String authToken = accessor.getFirstNativeHeader("Authorization");
			if (authToken != null && authToken.startsWith("FFtoken ")) {
				String token = authToken.substring(8);

				// ここでトークンの有効性チェックを行う（本来はJWT検証ロジックなどを入れる）
				Session session = sessionService.getSession(token);
				if (session == null) {
					// 認証失敗：例外を投げて接続を拒否する
					throw new IllegalArgumentException("認証トークンが無効です");
				} else {
					sessionService.removeSession(token);
					sessionService.generateSession(accessor.getSessionId(), session);
				}
			} else {
				throw new IllegalArgumentException("認証ヘッダーがありません");
			}
		}

		if (StompCommand.SEND.equals(accessor.getCommand())) {
			Session session = sessionService.getSession(accessor.getSessionId());
			if (session == null) {
				// 認証失敗：例外を投げて接続を拒否する
				throw new IllegalArgumentException("認証トークンが無効です");
			} else {
				accessor.setHeader("guildId", session.getGuildId());
			}
		}

		return message;
	}
}
