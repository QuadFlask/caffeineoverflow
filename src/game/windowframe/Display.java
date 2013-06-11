package game.windowframe;

import flask.utils.*;

import javax.swing.*;
import java.awt.*;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class Display extends JPanel {
	long sum = 0;
	Double t;
	private WindowForm masterWindow;
	private long before_time;
	private double currentTick;
	private double currentFPS;
	private FixedQueue<Double> fps_Queue = new FixedQueue<Double>(16);
	private Font font_consolas = new Font("consolas", Font.PLAIN, 11);
	private Color font_debug = Color.orange;
//	private Logger logger = LogManager.getLogger(getClass());

	public Display(WindowForm masterWindow) {
		this.masterWindow = masterWindow;
		setSize(masterWindow.getStageWidth(), masterWindow.getStageWidth());

		setBackground(Color.white);
		setIgnoreRepaint(true);


	}

	private void accumFPS() {
		currentTick = ((double) (System.nanoTime() - before_time)) / 1000000000;
		currentFPS = 1 / currentTick;
		fps_Queue.enQueue(currentFPS);
		before_time = System.nanoTime();

		int size = fps_Queue.size();
		for (int i = 0; i < size; i++) {
			t = fps_Queue.get();
			if (null != t)
				sum += t;
		}
		sum /= size;
		//System.out.println(sum);
	}

	public void onEnterFrame() {
//		logger.debug("debug.");
//		logger.info("info.");
//		logger.warn("warn.");
//		logger.error("error.");
//
//		System.out.println("isDebugEnabled : " + logger.isDebugEnabled());
		//logger.fatal("fatal.");
//		try {
//			Graphics2D g = (Graphics2D) getMasterWindow().getBufferStrategy().getDrawGraphics();
//			g.setBackground(Color.BLACK);
//			g.clearRect(0, 0, D.SCREEN_WIDTH + 3, D.SCREEN_HEIGHT + 25);
//			g.drawImage(activityManager.getMasterImage(), 3, 25, null);
//
//			showFPS(g);
//
//			g.dispose();
//			getMasterWindow().getBufferStrategy().show();
//		} catch (Exception e) {
//			//	e.printStackTrace();
//		}
	}

	public void loop() {
//		fireOnTickEvent(new OnTickEvent(this));
		onEnterFrame();
		accumFPS();
	}
}
