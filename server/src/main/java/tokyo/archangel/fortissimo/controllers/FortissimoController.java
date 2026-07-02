package tokyo.archangel.fortissimo.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import tokyo.archangel.fortissimo.form.MusicInformation;
import tokyo.archangel.fortissimo.servicies.MusicService;
import tokyo.archangel.fortissimo.servicies.MusicServiceProvider;

@Controller
public class FortissimoController {
	private MusicServiceProvider musicServiceProvider;

	public FortissimoController(MusicServiceProvider musicServiceProvider) {
		this.musicServiceProvider = musicServiceProvider;
	}

	/**
	 * すべての情報を取得する
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/getAllInformation")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation getAllInfomation(@DestinationVariable String roomId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		return service.getAllMusic();
	}

	/**
	 * 楽曲を追加する
	 * @param musicUrl 追加する曲のURL
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/addMusic")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation addMusic(@Payload String musicUrl, @DestinationVariable String roomId,
			@Header("guildId") String guildId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		// セッションから所属ギルドチャンネルを特定する
		service.addMusic(musicUrl, guildId, roomId);
		return service.getAllMusic();
	}

	/**
	 * 楽曲をスキップする
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/skip")
	public void skip(@DestinationVariable String roomId) throws Exception {
		// スキップを行った際にキューの更新が行われるので、クライアントの更新はそこに任せる
		MusicService service = musicServiceProvider.getService(roomId);
		service.doSkip();
	}

	/**
	 * 楽曲の再生を一時停止する
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/pause")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation pause(@DestinationVariable String roomId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		service.doPause();
		return service.getAllMusic();
	}

	/**
	 * 一時停止から再開する
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/resume")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation resume(@DestinationVariable String roomId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		service.doResume();
		return service.getAllMusic();
	}

	/**
	 * 再生を停止し、リストをクリアする
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/stop")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation stop(@DestinationVariable String roomId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		service.stop();
		return service.getAllMusic();
	}

	/**
	 * リストをシャッフルする
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/shuffle")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation shuffle(@DestinationVariable String roomId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		service.doShuffle();
		return service.getAllMusic();
	}

	/**
	 * ループ再生をオンにする
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/loopOn")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation loopOn(@DestinationVariable String roomId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		service.setLooping(true);
		return service.getAllMusic();
	}

	/**
	 * ループ再生をオフにする
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/player/{roomId}/loopOff")
	@SendTo("/topic/{roomId}/allInformation")
	public MusicInformation loopOff(@DestinationVariable String roomId) throws Exception {
		MusicService service = musicServiceProvider.getService(roomId);
		service.setLooping(false);
		return service.getAllMusic();
	}
}
