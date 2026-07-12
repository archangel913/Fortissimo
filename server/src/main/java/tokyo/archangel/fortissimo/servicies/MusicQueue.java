package tokyo.archangel.fortissimo.servicies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tokyo.archangel.fortissimo.dto.MusicMetaData;

@Service
@Scope("prototype")
public class MusicQueue {
	private List<MusicMetaData> queue = new ArrayList<>();

	public synchronized void push(MusicMetaData metadata) {
		queue.add(metadata);
	}

	public synchronized void pushAll(List<MusicMetaData> metadata) {
		queue.addAll(metadata);
	}

	public synchronized void addFirst(MusicMetaData metadata) {
		queue.add(0, metadata);
	}

	public synchronized MusicMetaData pull() {
		MusicMetaData data = queue.getFirst();
		queue.remove(0);
		return data;
	}

	public synchronized List<MusicMetaData> getAll() {
		return List.copyOf(queue);
	}

	public synchronized void shuffle() {
		List<MusicMetaData> shuffled = new ArrayList<MusicMetaData>(queue);
		Collections.shuffle(shuffled);
		clear();
		pushAll(shuffled);
	}

	public synchronized void clear() {
		queue.clear();
	}
}
