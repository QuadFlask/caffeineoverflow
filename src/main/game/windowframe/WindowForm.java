package main.game.windowframe;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class WindowForm extends JFrame {
	private Display stage;
	private Timer timer = new Timer();
	private int widht, height;
	private int frameRate;

	public WindowForm(String title, int w, int h, int fps) {
		System.out.println("initializing...");
		setTitle(title);
		setSize(w, h);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setIgnoreRepaint(true);

		frameRate = (int) (1000.0f / fps + .5f);
		System.out.println("fps : " + fps + "[" + frameRate + "ms]");
	}

	public void start() {
		stage = new Display(this);

		Date now = new Date(System.nanoTime() / 1000);
		timer.schedule(new Task(), now, frameRate);
		getContentPane().add(stage);

		setResizable(false);
		setVisible(true);

		createBufferStrategy(2);

		System.out.println("isDoubleBuffered : " + isDoubleBuffered());
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width + 6, height + 28);
		this.widht = width;
		this.height = height;
	}

	public int getStageWidth() {
		return widht;
	}

	public int getStageHieght() {
		return height;
	}

	public void close() {
		WindowEvent closingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
	}

	class Task extends TimerTask {
		@Override
		public void run() {
			stage.loop();
		}
	}
}
