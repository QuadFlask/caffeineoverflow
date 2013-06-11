package main.flask.threadpool;

public class ThreadPoolService {
	private static final int THREAD_COUNT = 16;
	private static ThreadPool threadPool = new ThreadPool(THREAD_COUNT);

	public static synchronized void addWork(Runnable runnable) {
		threadPool.addWork(runnable);
	}

	public static synchronized void execute() {
		threadPool.execute();
	}

	public static synchronized void exectueAll() {
		threadPool.executeAll();
	}
}
