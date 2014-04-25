package main.game.windows;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import main.flask.utils.FixedQueue;
import main.flask.utils.GetOSEnviroment;
import main.game.common.D;
import main.game.event.Tick;
import main.game.event.TickEvent;
import main.game.event.TickEventListener;
import main.game.view.View;

public class Display extends JPanel {
	private static final long serialVersionUID = 4048721739394179649L;
	private long sum = 0;
	private Double t;
	private long before_time;
	private double currentTick;
	private double currentFPS;
	private FixedQueue<Double> fps_Queue = new FixedQueue<>(16);
	//
	private WindowForm masterWindow;
	private KeyManager keyManager;
	//
	private EventListenerList tickEventListenerList = new EventListenerList();
	private ArrayList<View> views = new ArrayList<>();

	private static int window_gap_w = 2;
	private static int window_gap_h = 24;

	public Display(WindowForm masterWindow) {
		this.masterWindow = masterWindow;
		setSize(masterWindow.getStageWidth(), masterWindow.getStageWidth());
		setBackground(Color.white);
		setIgnoreRepaint(true);

		if (!GetOSEnviroment.isWindows()) {
			window_gap_w = 0;
			window_gap_h = 22;
		}

		keyManager = new KeyManager();
		masterWindow.addKeyListener(keyManager);
		masterWindow.setFocusable(true);

		fps_Queue.initializeWith((double) D.FPS);
		accumFPS();
	}

	private void accumFPS() {
		currentTick = ((double) (System.nanoTime() - before_time)) / 1000000000;
		currentFPS = 1 / currentTick;
		fps_Queue.enQueue(currentFPS);
		before_time = System.nanoTime();

		int size = fps_Queue.size();
		for (int i = 0; i < size; i++) {
			t = fps_Queue.get();
			if (null != t) sum += t;
		}
		sum /= size;
	}

	public long getFPS() {
		return sum;
	}

	public void addView(View view) {
		views.add(view);
		view.setKeyManager(keyManager);
		view.init();
		addTickListener(view);
	}

	public void removeView(View view) {
		views.remove(view);
		view.removeKeyManager();
		removeTickListener(view);
	}

	public void addTickListener(TickEventListener listener) {
		tickEventListenerList.add(TickEventListener.class, listener);
	}

	public void removeTickListener(TickEventListener listener) {
		tickEventListenerList.remove(TickEventListener.class, listener);
		// SimpleLogger.log("remove listener!" + listener.toString());
	}

	protected void fireTickEvent(TickEvent e) {
		Object[] listeners = tickEventListenerList.getListenerList();
		// SimpleLogger.log("listeners.length : " + listeners.length / 2);
		for (int i = 0; i < listeners.length; i = i + 2) {
			if (listeners[i] == TickEventListener.class) {
				((TickEventListener) listeners[i + 1]).onTick(e);
				// SimpleLogger.log(((TickEventListener) listeners[i +
				// 1]).toString());
			}
		}
	}

	public void onEnterFrame() {
		try {
			Graphics2D g = (Graphics2D) masterWindow.getBufferStrategy().getDrawGraphics();
			g.clearRect(0, 0, D.SCREEN_WIDTH + window_gap_w, D.SCREEN_HEIGHT + window_gap_h);

			for (View l : views) {
				if (l.isVisible()) g.drawImage(l.getBufferedImage(), window_gap_w, window_gap_h, null);
			}

			g.dispose();
			masterWindow.getBufferStrategy().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loop() {
		keyManager.refresh();
		fireTickEvent(new TickEvent(new Tick(sum, 1f / sum)));
		onEnterFrame();
		accumFPS();
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}
}
