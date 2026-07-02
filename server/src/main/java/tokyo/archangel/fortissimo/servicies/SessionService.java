package tokyo.archangel.fortissimo.servicies;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tokyo.archangel.fortissimo.dto.Session;

@Service
@Slf4j
public class SessionService {
	private ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	private static final SecureRandom secureRandom = new SecureRandom();

	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

	private static final int byteLength = 256;

	public String generateSession(Session newSession) {
		String token = generateToken();
		sessions.put(token, newSession);
		return token;
	}

	public void generateSession(String sessionId, Session newSession) {
		sessions.put(sessionId, newSession);
	}

	public Session getSession(String token) {
		log.debug("現在有効なセッションは: " + sessions.size() + "個です");
		return sessions.get(token);
	}

	public void removeSession(String token) {
		sessions.remove(token);
		log.debug("現在有効なセッションは: " + sessions.size() + "個です");
	}

	private String generateToken() {
		byte[] randomBytes = new byte[byteLength];
		secureRandom.nextBytes(randomBytes);
		String token = base64Encoder.encodeToString(randomBytes);
		log.debug("トークンを発行します: " + token);
		return token;
	}
}
