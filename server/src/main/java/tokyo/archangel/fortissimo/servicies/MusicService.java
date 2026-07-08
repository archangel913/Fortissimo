package tokyo.archangel.fortissimo.servicies;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tokyo.archangel.fortissimo.dto.MusicMetaData;
import tokyo.archangel.fortissimo.form.MusicInformation;
import tokyo.archangel.fortissimo.form.NowPlayingItem;
import tokyo.archangel.fortissimo.form.PlayingStatus;
import tokyo.archangel.fortissimo.form.QueueItem;
import tokyo.archangel.sdb.voice.VoiceSender;

@Service
@Scope("prototype")
@Slf4j
public class MusicService {
	private MusicQueue queue;

	private MusicDownloader downloader;

	private MusicEncorder encorder;

	private ProcessExecuter processExecuter;

	private VoiceSender sender;

	private SimpMessagingTemplate messagingTemplate;

	private MusicMetaData nowPlaying;

	private volatile boolean isLooping = false;

	private volatile boolean isSkip = false;

	private String channelId = "";

	private MusicBuffer musicBuffer = new MusicBuffer();

	private CompletableFuture<Void> playingFurture = CompletableFuture.completedFuture(null);

	private CompletableFuture<Void> pausingFurture = CompletableFuture.completedFuture(null);

	public MusicService(MusicQueue queue, MusicDownloader downloader, MusicEncorder encorder,
			ProcessExecuter processExecuter, VoiceSender sender, SimpMessagingTemplate messagingTemplate) {
		this.queue = queue;
		this.downloader = downloader;
		this.encorder = encorder;
		this.processExecuter = processExecuter;
		this.sender = sender;
		this.messagingTemplate = messagingTemplate;
	}

	public void init() {
		sender.addDisconnectEvent(() -> {
			stop();
		});
	}

	public synchronized MusicInformation getAllMusic() {
		PlayingStatus playingStatus = new PlayingStatus(pausingFurture.isDone(), isLooping);
		List<QueueItem> queueItem = new ArrayList<>();

		NowPlayingItem nowPlayingItem;
		if (nowPlaying == null) {
			nowPlayingItem = new NowPlayingItem(0, "何も再生していません", "", new byte[0]);
		} else {
			nowPlayingItem = new NowPlayingItem(nowPlaying.getId(), nowPlaying.getTitle(), nowPlaying.getUrl(),
					nowPlaying.getThumbnail());
		}

		int order = 1;
		for (MusicMetaData matadata : queue.getAll()) {
			queueItem.add(new QueueItem(matadata.getId(), matadata.getTitle(), matadata.getUrl(), order));
			order++;
		}

		return new MusicInformation(nowPlayingItem, queueItem.toArray(new QueueItem[queueItem.size()]), playingStatus);
	}

	public synchronized void addMusic(String url, String guildId, String channelId) {
		this.channelId = channelId;
		// urlから曲を取得。
		List<MusicMetaData> metadataList = downloader.getMetaData(url);
		queue.pushAll(metadataList);

		// 再生中でなければ再生開始
		if (playingFurture.isDone()) {
			sender.connect(guildId, channelId);
			Thread thread = new Thread(() -> {
				play();
			});
			thread.start();
		}
	}

	/**
	 * 
	 * @param isLooping
	 */
	public synchronized void setLooping(boolean isLooping) {
		this.isLooping = isLooping;
	}

	public synchronized void doPause() {
		if (playingFurture.isDone()) {
			log.warn("再生中でないため、操作を実行しません");
			return;
		}
		pausingFurture = new CompletableFuture<Void>();
	}

	public synchronized void doResume() {
		if (playingFurture.isDone()) {
			log.warn("再生中でないため、操作を実行しません");
			return;
		}
		if (pausingFurture == null) {
			log.warn("ポーズ中ではありません");
		} else {
			pausingFurture.complete(null);
		}
	}

	public synchronized void doSkip() {
		if (playingFurture.isDone()) {
			log.warn("再生中でないため、操作を実行しません");
			return;
		}
		doResume();
		isSkip = true;
	}

	/**
	 * シャッフルを行う
	 */
	public synchronized void doShuffle() {
		queue.shuffle();
	}

	/**
	 * 再生停止する
	 */
	public synchronized void stop() {
		if (playingFurture.isDone()) {
			log.warn("再生中でないため、操作を実行しません");
			return;
		}
		isLooping = false;
		doResume();
		queue.clear();
		playingFurture.complete(null);
		clientDataUpdate();
	}

	public void close() {
		stop();
	}

	/**
	 * 音声再生ロジック本体
	 */
	private CompletableFuture<Void> play() {
		playingFurture = new CompletableFuture<Void>();

		while ((nowPlaying = queue.pull()) != null && !playingFurture.isDone()) {
			try {
				nowPlaying = downloader.getMetaData(nowPlaying.getUrl()).get(0);
				clientDataUpdate();

				List<ProcessBuilder> processes = new ArrayList<>();
				// 曲をダウンロード
				processes.add(downloader.getBinaryDownloadProcess(nowPlaying.getUrl()));

				// エンコード
				processes.add(encorder.getEncordProcess());

				// プロセスの実行
				processExecuter.setBuilders(processes);
				processExecuter.setBuffer(musicBuffer, 3840);
				processExecuter.run();

				// 送信
				byte[] buffer;
				while ((buffer = musicBuffer.poll()) != null
						&& !playingFurture.isDone() && !isSkip) {
					if (!pausingFurture.isDone()) {
						// ポーズ中の時は曲を一時停止する
						sender.pause();
						pausingFurture.join();
						sender.resume();
					}
					sender.send(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (isLooping) {
					// loop再生中だった場合、曲を再度追加する
					queue.push(nowPlaying);
				}
				musicBuffer.clear();
				nowPlaying = null;
				isSkip = false;
				processExecuter.kill();
			}
		}

		sender.disconnect();
		playingFurture.complete(null);
		clientDataUpdate();
		return CompletableFuture.completedFuture(null);
	}

	private void clientDataUpdate() {
		MusicInformation info = getAllMusic();
		String destination = "/topic/" + channelId + "/allInformation";
		messagingTemplate.convertAndSend(destination, info);
	}
}
