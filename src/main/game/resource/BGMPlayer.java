package main.game.resource;

import java.util.ArrayList;
import java.util.Random;

import kuusisto.tinysound.OnStopListener;
import main.game.resource.SoundAsset.SOUND_ID;

public class BGMPlayer {
	private static int initCount = 64;
	private static Random rand = new Random();
	private static ArrayList<Integer> randoms = new ArrayList<Integer>();
	
	static SOUND_ID[] BGMS = new SOUND_ID[] {//
	SOUND_ID.BGM_LOWPASS, //
			SOUND_ID.BGM_BEGINNING_THE_FLIGHT, //
			SOUND_ID.BGM_HALFCF, //
			SOUND_ID.BGM_OBSERVED_SKY, //
			SOUND_ID.BGM_RAINY_DAY, //
			SOUND_ID.BGM_SPRINGWIND, //
			SOUND_ID.BGM_WET_STREET_COLOR_SIDE, //
			SOUND_ID.BGM_WHITEWIND, //
			SOUND_ID.BGM_THE_TOWER_OF_SHADOW, //
	};
	
	private static int n = BGMS.length;
	
	static {
		Integer ii = 0;
		randoms.add(rand.nextInt(n));
		
		for (int i = 1; i < initCount; i++) {
			do {
				ii = rand.nextInt(n);
			} while (randoms.get(i - 1).equals(ii));
			randoms.add(ii);
		}
	}
	
	private static int cursor = 0;
	
	private static Integer getNext() {
		if (cursor >= randoms.size()) cursor = 0;
		return randoms.get(cursor++);
	}
	
	public static void playRandomBGM(final float volume) {
		GameSoundPlayer.play(getNextRandomBGM(), volume, new OnStopListener() {
			@Override
			public void onStop() {
				if (!isStop) GameSoundPlayer.play(getNextRandomBGM(), volume, this);
			}
		});
		isStop = false;
	}
	
	static boolean isStop = false;
	
	public static void stop() {
		GameSoundPlayer.stop();
		isStop = true;
	}
	
	private static SOUND_ID getNextRandomBGM() {
		return BGMS[getNext()];
	}
	//
	// @Deprecated
	// private static File getNextRandomBGMFile() {
	// final SOUND_ID key = getNextRandomBGMSound_ID();
	// System.out.println(key.toString());
	// return SoundAsset.SOUND_LIST.get(key);
	// }
}
