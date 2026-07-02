package tokyo.archangel.fortissimo.dto;

import lombok.Value;

@Value
public class MusicMetaData {
	private long id;
	
	private String title;

	private String url;

	private byte[] thumbnail;
}
