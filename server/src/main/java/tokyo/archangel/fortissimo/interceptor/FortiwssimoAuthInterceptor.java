package tokyo.archangel.fortissimo.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tokyo.archangel.fortissimo.dto.Session;
import tokyo.archangel.fortissimo.servicies.SessionService;

@Component
@Slf4j
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

		// 実行時間計測用
		accessor.setHeader("startTime", System.currentTimeMillis());

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

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		// メッセージヘッダーから開始時間を取得
		Long startTime = (Long) message.getHeaders().get("startTime");
		if (startTime != null) {
			long endTime = System.currentTimeMillis();
			long executeTime = endTime - startTime;

			// STOMPのコマンド種別（SEND, SUBSCRIBE, CONNECTなど）や宛先（Destination）を取得
			Object stompCommand = message.getHeaders().get("simpCommand");
			Object destination = message.getHeaders().get("simpDestination");

			log.info("STOMP Command: {} | Destination: {} | 処理時間: {} ms",
					stompCommand, destination, executeTime);
		}
	}
}
