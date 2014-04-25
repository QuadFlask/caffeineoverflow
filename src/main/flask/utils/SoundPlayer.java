package main.flask.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.flask.net.SimpleLogger;
import kuusisto.tinysound.OnStopListener;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundPlayer {
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(16);
	private static List<SoundPlayRunner> runners = new ArrayList<SoundPlayRunner>();
	
	static {
		TinySound.init();
	}
	
	public static void play(File file, float gain, OnStopListener listener) { // gain
		final SoundPlayRunner runner = new SoundPlayRunner(file, gain, listener);
		runners.add(runner);
		threadPool.execute(runner);
	}
	
	public static class SoundPlayRunner implements Runnable {
		private Sound sound;
		private File file;
		private float gain;
		private OnStopListener listener;
		
		public SoundPlayRunner(File file, float gain, OnStopListener listener) {
			this.file = file;
			this.gain = gain;
			this.listener = listener;
		}
		
		@Override
		public void run() {
			sound = TinySound.loadSound(file);
			sound.play(gain, new OnStopListener() {
				@Override
				public void onStop() {
					if (listener != null) listener.onStop();
					runners.remove(this);
					sound.unload();
				}
			});
		}
		
		public void stop() {
			if (sound != null) sound.stop();
			else SimpleLogger.log("Null Sound!!");
		}
		
		public void free() {
			if (sound != null) sound.unload();
			sound = null;
			file = null;
			listener = null;
		}
	}
	
	public static void stop() {
		synchronized (runners) {
			for (SoundPlayRunner r : new ArrayList<>(runners)) {
				r.stop();
				r.free();
			}
		}
		runners.clear();
		
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(16);
	}
	
	public static void shutdown() {
		TinySound.shutdown();
	}
	
	// public static void setGain(Clip clip, float gain) {
	// FloatControl gainControl = (FloatControl)
	// clip.getControl(FloatControl.Type.MASTER_GAIN);
	// gainControl.setValue(gain);
	// }
	//
	// public static void setGain(LineEvent e, float gain) {
	// FloatControl gainControl = (FloatControl)
	// e.getLine().getControl(FloatControl.Type.MASTER_GAIN);
	// gainControl.setValue(gain);
	// }
	//
	
	//
	// public static float linearGain(float gainDB) {
	// return (float) Math.pow(10.0, gainDB / 20.0);
	// }
	//
	// public static float dB(float gain) {
	// gain = Math.min(Math.max(gain, 0.0f), 1.0f);
	// return 10f * (float) (Math.log10(gain * 4.0));
	// }
	//
	
	// // only java7
	// public static void mp3Play(String dir) {
	// throw new UnsupportedOperationException();
	// // Media hit = new Media(dir);
	// // MediaPlayer mediaPlayer = new MediaPlayer(hit);
	// // mediaPlayer.play();
	// }
	
	// try {
	// gain = dB(gain);
	//
	// final AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
	// final Clip clip = AudioSystem.getClip();
	// clip.open(audioIn);
	// FloatControl gainControl = (FloatControl)
	// clip.getControl(FloatControl.Type.MASTER_GAIN);
	// gainControl.setValue(gain); // Reduce volume by n decibels.
	// clip.addLineListener(new LineListener() {
	// public void update(LineEvent e) {
	// if (e.getType() == LineEvent.Type.STOP) {
	// e.getLine().close();
	// clip.close();
	// try {
	// audioIn.close();
	// } catch (IOException ioe) {
	// ioe.printStackTrace();
	// }
	// }
	// }
	// });
	// if (lineListener != null) clip.addLineListener(lineListener);
	//
	// clip.start();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	
	//
	// private static float calcGain(float gain) {
	// gain = Math.min(Math.max(gain, 0.0f), 1.0f);
	// gain -= 1.0f;
	// gain *= 30;
	// return gain;
	// }
	
	// public static void wavPlay(File file, float gain, boolean forever) { //
	// gain is 0.0~1.0
	// try {
	// gain = calcGain(gain);
	//
	// final AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
	// final Clip clip = AudioSystem.getClip();
	// clip.open(audioIn);
	// FloatControl gainControl = (FloatControl)
	// clip.getControl(FloatControl.Type.MASTER_GAIN);
	// gainControl.setValue(gain);
	// if (forever) {
	// clip.loop(Clip.LOOP_CONTINUOUSLY); // repeat forever
	// } else {
	// clip.addLineListener(new LineListener() {
	// public void update(LineEvent e) {
	// if (e.getType() == LineEvent.Type.STOP) {
	// e.getLine().close();
	// clip.close();
	// try {
	// audioIn.close();
	// } catch (IOException ioe) {
	// ioe.printStackTrace();
	// }
	// }
	// }
	// });
	// }
	// clip.start();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
