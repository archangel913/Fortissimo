package tokyo.archangel.fortissimo.controllers;

import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import tokyo.archangel.fortissimo.form.AuthInformation;
import tokyo.archangel.fortissimo.servicies.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {
	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/auth")
	public ResponseEntity<?> auth(@RequestBody AuthInformation form) {
		return authService.auth(form);
	}

	/**
	 * クライアント（SockJS）の接続が切れたときに自動的に呼び出されるメソッド
	 */
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		authService.removeSession(event);
	}
}
