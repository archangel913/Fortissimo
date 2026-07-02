package tokyo.archangel.fortissimo.servicies;

import org.springframework.stereotype.Service;

/**
 * 音声データのエンコードを担当
 */
@Service
public class MusicEncorder {
	public ProcessBuilder getEncordProcess() {
		return new ProcessBuilder("ffmpeg", "-i", "pipe:0", "-vn", "-f", "s16le", "-ar", "48000",
				"-ac", "2", "pipe:1");
	}
}
