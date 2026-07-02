package tokyo.archangel.fortissimo.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class MusicInformation {
	@JsonProperty("now")
	private NowPlayingItem nowPlayingItem;
	
	@JsonProperty("queue")
	private QueueItem[] queueItems;
	
	@JsonProperty("status")
	private PlayingStatus playingStatus;
}
