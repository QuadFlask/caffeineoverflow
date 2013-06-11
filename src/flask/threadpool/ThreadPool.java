package flask.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private ExecutorService threadPool;
	private WorkQueue queue;

	public ThreadPool(int threadCount) {
		threadPool = Executors.newFixedThreadPool(threadCount);
		queue = new WorkQueue();
	}

	public void addWork(Runnable work) {
		queue.enqueue(work);
	}

	public void execute() {
		if (!queue.isEmpty()) {
			Runnable runnable = queue.dequeue();
			threadPool.execute(runnable);
		}
	}

	public void executeAll() {
		while (!queue.isEmpty()) {
			execute();
		}
	}

	public void destroy() {
		threadPool.shutdown();
		queue.clear();
	}
}