package tokyo.archangel.fortissimo.servicies;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Scope("prototype")
@Slf4j
public class ProcessExecuter {

	private List<ProcessBuilder> builders;

	private List<Process> processes;

	private MusicBuffer buffer;

	private int bufferSize;

	private int failedCount = 0;

	private volatile boolean isNormalExit = true;

	public void setBuilders(List<ProcessBuilder> builders) {
		this.builders = builders;
	}

	public void setBuffer(MusicBuffer buffer, int size) {
		this.buffer = buffer;
		this.bufferSize = size;
	}

	public boolean isNormalExit() {
		if (failedCount > 5) {
			// もし5回以上失敗している場合は正常終了とする。
			failedCount = 0;
			return true;
		}
		return this.isNormalExit;
	}

	public void run() {
		try {
			processes = ProcessBuilder.startPipeline(builders);
			Process lastProcess = processes.get(processes.size() - 1);
			Thread stdoutThread = new Thread(() -> {
				try (BufferedInputStream reader = new BufferedInputStream(lastProcess.getInputStream())) {
					byte[] buf = new byte[bufferSize];
					int readSize;
					while ((readSize = reader.read(buf, 0, buf.length)) > 0) {
						buffer.add(buf, 0, readSize);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					buffer.setIsWriteFinish();
				}
			});
			stdoutThread.start();

			for (Process process : processes) {
				Thread stderrThread = new Thread(() -> {
					try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
						String line = null;
						while ((line = br.readLine()) != null) {
							log.trace(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						isNormalExit = isNormalExit && process.exitValue() == 0;
					}
					log.trace("PID:" + process.pid() + "  exitコード:" + process.exitValue());
				});
				stderrThread.start();
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void kill() {
		if (processes != null) {
			for (Process process : processes) {
				process.destroy();
			}
		}

		builders = new ArrayList<ProcessBuilder>();
		processes = null;
		buffer = null;
		if (!isNormalExit) {
			failedCount++;
		} else {
			failedCount = 0;
		}
		log.debug("" + failedCount);
		isNormalExit = true;
	}
}
