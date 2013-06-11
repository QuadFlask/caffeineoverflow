package main.flask.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
	// only java7
	public static void mp3Play(String dir) {
		Media hit = new Media(dir);
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}

	public static void wavPlay(File file, float gain) { // gain is 0.0~1.0
		try {
			gain = Math.min(Math.max(gain, 0.0f), 1.0f);
			gain -= 1.0f;
			gain *= 30;

			final AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
			final Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(gain); // Reduce volume by n decibels.
			clip.addLineListener(new LineListener() {
				public void update(LineEvent e) {
					if (e.getType() == LineEvent.Type.STOP) {
						e.getLine().close();
						clip.close();
						try {
							audioIn.close();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				}
			});

			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void wavPlay(File file, float gain, boolean forever) { // gain is 0.0~1.0
		try {
			gain = Math.min(Math.max(gain, 0.0f), 1.0f);
			gain -= 1.0f;
			gain *= 30;

			final AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
			final Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(gain);
			if (forever) {
				clip.loop(Clip.LOOP_CONTINUOUSLY); // repeat forever
			} else {
				clip.addLineListener(new LineListener() {
					public void update(LineEvent e) {
						if (e.getType() == LineEvent.Type.STOP) {
							e.getLine().close();
							clip.close();
							try {
								audioIn.close();
							} catch (IOException ioe) {
								ioe.printStackTrace();
							}
						}
					}
				});
			}
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
