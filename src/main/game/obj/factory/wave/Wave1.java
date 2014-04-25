package main.game.obj.factory.wave;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import main.game.obj.factory.unit.mob.Mob;

public class Wave1 implements Wave {

	private Timer timer;
	private List<Mob> generatePattern;

	private long start, end;
	private final long duration = 20;

	public static final long NS2SEC = 1000 * 1000;
	public static final long MS2SEC = 1000;

	public Timer getTimer() {
		return timer;
	}

	public Wave1() {

	}

	public void start() {
		start = System.currentTimeMillis();
		end = start + duration * MS2SEC;
		
		timer = new Timer();
		timer.schedule(task, 0, 100);
	}
	
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			
		}
	}; 

}
