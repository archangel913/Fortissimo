package tokyo.archangel.fortissimo.servicies;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import tokyo.archangel.fortissimo.config.ApplicationProperties;
import tokyo.archangel.fortissimo.dto.Session;
import tokyo.archangel.fortissimo.form.AuthInformation;

@Service
public class AuthService {
	private ApplicationProperties properties;

	private SessionService sessionService;

	private final RestTemplate restTemplate = new RestTemplate();

	public AuthService(ApplicationProperties properties, SessionService sessionService) {
		this.properties = properties;
		this.sessionService = sessionService;
	}

	public ResponseEntity<?> auth(AuthInformation form) {
		if (form == null || form.getCode() == null) {
			return ResponseEntity.badRequest().body("Code is required");
		}

		try {
			// 1. Discordにcodeを送信して、access_tokenを取得
			String tokenUrl = "https://discord.com/api/v10/oauth2/token";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("client_id", properties.getClientId());
			body.add("client_secret", properties.getClientSecret());
			body.add("grant_type", "authorization_code");
			body.add("code", form.getCode());
			body.add("redirect_uri", properties.getRedirectUri());

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
			Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUrl, request, Map.class);

			String accessToken = (String) tokenResponse.get("access_token");

			// 2. access_tokenを使ってDiscordのユーザー情報を取得
			String userUrl = "https://discord.com/api/v10/users/@me";
			HttpHeaders userHeaders = new HttpHeaders();
			userHeaders.setBearerAuth(accessToken);
			HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

			Map<String, Object> discordUser = restTemplate.exchange(userUrl, HttpMethod.GET, userRequest, Map.class)
					.getBody();

			// 3. 取得したユーザー情報（IDなど）を元に、アプリ独自のJWTを生成してフロントに返す
			String discordUserId = (String) discordUser.get("id");
			String username = (String) discordUser.get("username");

			String appToken = sessionService
					.generateSession(new Session(discordUserId, username, form.getGuildId(), null));

			return ResponseEntity.ok(appToken);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Authentication failed: " + e.getMessage());
		}
	}

	public void removeSession(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = (String) accessor.getSessionId();
		sessionService.removeSession(sessionId);
	}
}
