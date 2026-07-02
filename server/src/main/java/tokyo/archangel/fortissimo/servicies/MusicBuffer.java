package tokyo.archangel.fortissimo.servicies;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class MusicBuffer {
	private LinkedBlockingQueue<byte[]> musicBuffer = new LinkedBlockingQueue<>();

	private volatile boolean isWriteFinish = false;

	public void add(byte[] bytes, int off, int len) {
		byte[] buf = Arrays.copyOfRange(bytes, off, len);
		musicBuffer.add(buf);
	}

	public byte[] poll() {
		byte[] buf = musicBuffer.poll();

		while (buf == null) {
			if (isWriteFinish) {
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			buf = musicBuffer.poll();
		}
		return buf;
	}

	public void setIsWriteFinish() {
		isWriteFinish = true;
	}

	public void clear() {
		musicBuffer.clear();
		isWriteFinish = false;
	}
}
