package main.flask.threadpool;

public abstract class DurationRunnable implements Runnable {
	protected float duration = 0;

	public void setDuration(float duration) {
		this.duration = duration;
	}

}
