package tokyo.archangel.fortissimo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Session {
	private String userId;
	
	private String username;
	
	private String guildId;
	
	private String sessionId;
}
