package tokyo.archangel.fortissimo.servicies;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tokyo.archangel.fortissimo.dto.MusicMetaData;

@Service
@Scope("prototype")
public class MusicQueue {
	private Queue<MusicMetaData> queue = new ArrayDeque<>();

	public synchronized void push(MusicMetaData metadata) {
		queue.add(metadata);
	}

	public synchronized void pushAll(List<MusicMetaData> metadata) {
		queue.addAll(metadata);
	}

	public synchronized MusicMetaData pull() {
		return queue.poll();
	}
	
	public synchronized List<MusicMetaData> getAll(){
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
