package tokyo.archangel.fortissimo.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class AuthInformation {
	@JsonProperty("code")
	private String code;

	@JsonProperty("guildId")
	private String guildId;

}
