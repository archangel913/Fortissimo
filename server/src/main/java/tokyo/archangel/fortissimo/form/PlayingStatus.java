package tokyo.archangel.fortissimo.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class PlayingStatus {
	@JsonProperty("playing")
	private boolean playing;
	
	@JsonProperty("looping")
	private boolean looping;
}
