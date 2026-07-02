package tokyo.archangel.fortissimo.servicies;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class MusicServiceProvider {
	private ObjectProvider<MusicService> serviceProvider;

	private ConcurrentMap<String, MusicService> musicServices = new ConcurrentHashMap<>();

	public MusicServiceProvider(ObjectProvider<MusicService> serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	/**
	 * サービスを取得する
	 * @param session
	 * @return
	 */
	public MusicService getService(String channelId) {
		if (channelId == null || channelId.isEmpty()) {
			throw new IllegalStateException("初期化に必要な情報がありません");
		}

		MusicService voiceSession = musicServices.get(channelId);
		if (voiceSession != null) {
			return voiceSession;
		}

		// インスタンス生成と初期化処理
		MusicService newService = serviceProvider.getObject();

		// 新規登録
		MusicService existingService = musicServices.computeIfAbsent(channelId, k -> newService);

		// もし他スレッドと同時に動いていて、登録できなかった場合
		if (existingService != newService) {
			newService.close();
		}

		return existingService;
	}

	/**
	 * サービスを削除する。
	 * @param session
	 */
	public void removeService(String channelId) {
		MusicService musicService = musicServices.remove(channelId);
		musicService.close();

	}

	@PreDestroy
	public void close() {
		musicServices.forEach((id, service) -> {
			if (service != null) {
				service.close();
			}
		});
		musicServices.clear();
	}
}
