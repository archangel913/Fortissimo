package tokyo.archangel.fortissimo.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class QueueItem {
	@JsonProperty("id")
	private long id;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("order")
	private int order;
}
