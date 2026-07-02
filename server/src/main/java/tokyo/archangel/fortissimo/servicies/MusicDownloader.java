package tokyo.archangel.fortissimo.servicies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import tokyo.archangel.fortissimo.dto.MusicMetaData;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * 楽曲本体とメタデータ取得を担当
 */
@Service
@Slf4j
public class MusicDownloader {
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final RestTemplate restTemplate = new RestTemplate();

	private static long id_seq = 1;

	/**
	 * 楽曲のメタデータを取得
	 * @param url 動画URL
	 * @return
	 */
	public List<MusicMetaData> getMetaData(String url) {
		// TODO とりあえずyoutubeから
		List<MusicMetaData> result = new ArrayList<MusicMetaData>();
		try {
			ProcessBuilder builder = new ProcessBuilder("yt-dlp", "--skip-download", "--flat-playlist", "--print",
					"{\"\\\"url\\\"\": %(webpage_url|\\\"\\\")j, \"\\\"title\\\"\": %(title|\\\"\\\")j, \"\\\"thumbnail\\\"\": %(thumbnail|\\\"\\\")j}",
					url);
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				log.trace(line);
				JsonNode jn = objectMapper.readTree(line);
				byte[] thumbnail = new byte[0];
				if (!jn.get("thumbnail").asString().isEmpty()) {
					thumbnail = restTemplate.getForObject(jn.get("thumbnail").asString(), byte[].class);
				}
				long id = id_seq++;

				MusicMetaData data = new MusicMetaData(
						id,
						jn.get("title").asString(),
						jn.get("url").asString(),
						thumbnail);
				result.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 楽曲の音声バイナリを取得するプロセスを取得
	 * @param url
	 * @return
	 */
	public ProcessBuilder getBinaryDownloadProcess(String url) {
		return new ProcessBuilder("yt-dlp", "-x", "-o", "-", url);
	}
}
