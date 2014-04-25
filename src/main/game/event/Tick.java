package main.game.event;

public class Tick {
	public long fps;
	public float elaspedTime;
	public Object data;

	public Tick(long fps) {
		this(fps, 0);
	}

//	public Tick(long fps, Object data) {
//		this.fps = fps;
//		this.data = data;
//	}

	public Tick(long fps, float elaspedTime) {
		this.fps = fps;
		this.elaspedTime = elaspedTime;
	}
}
