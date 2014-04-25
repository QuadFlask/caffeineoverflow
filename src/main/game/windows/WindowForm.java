package main.game.windows;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import kuusisto.tinysound.TinySound;

import main.flask.utils.GetOSEnviroment;
import main.flask.utils.SoundPlayer;

public class WindowForm extends JFrame {
	private static final long serialVersionUID = -620567974614899959L;
	
	private Display stage;
	private Timer timer = new Timer();
	private int width, height;
	private int frameRate;
	private boolean isAtFixedRate = false;
	private boolean isStarted = false;
	
	public WindowForm(String title, int w, int h, int fps) {
		System.out.println("initializing...");
		setTitle(title);
		setSize(w, h);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setIgnoreRepaint(true);
		
		frameRate = (int) (1000.0f / fps + .5f);
		System.out.println("fps : " + fps + " [" + frameRate + "ms]");
	}
	
	public void start(Display display) {
		stage = display;
		isStarted = true;
		
		setResizable(false);
		setVisible(true);
		createBufferStrategy(2);
		
		if (isAtFixedRate) timer.scheduleAtFixedRate(new Task(), 100, frameRate);
		else timer.schedule(new Task(), 100, frameRate);
		getContentPane().add(stage);
	}
	
	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		height += 22;
		if (GetOSEnviroment.isWindows()) {
			width += 5;
			height += 5;
		}
		super.setSize(width, height);
	}
	
	public int getStageWidth() {
		return width;
	}
	
	public int getStageHieght() {
		return height;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	public void close() {
		WindowEvent closingEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
		
		SoundPlayer.shutdown();
	}
	
	class Task extends TimerTask {
		@Override
		public void run() {
			stage.loop();
		}
	}
}
