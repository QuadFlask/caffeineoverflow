package main.game.resource;

import kuusisto.tinysound.OnStopListener;
import main.flask.utils.SoundPlayer;
import main.game.resource.SoundAsset.SOUND_ID;

public class GameSoundPlayer {
	public static float GLOBAL_SOUND_VOLUME = 1.0f;
	public static float EFFECT_VOLUME = 1.0f;
	public static float BGM_VOLUME = 1.0f;
	
	public static void play(SOUND_ID id, float volume) {
		volume = getVolume(id, volume);
		SoundPlayer.play(SoundAsset.get(id), volume, null);
	}
	
	public static void play(SOUND_ID id, float volume, OnStopListener listener) {
		volume = getVolume(id, volume);
		SoundPlayer.play(SoundAsset.get(id), volume, listener);
	}
	
	public static void stop() {
		SoundPlayer.stop();
	}
	
	private static float getVolume(SOUND_ID id, float volume) {
		if (SoundAsset.isBGM(id)) volume = getBGMVolume(volume);
		else volume = getEffectVolume(volume);
		return volume;
	}
	
	public static float getBGMVolume(float volume) {
		return volume * BGM_VOLUME * GLOBAL_SOUND_VOLUME;
	}
	
	public static float getEffectVolume(float volume) {
		return volume * EFFECT_VOLUME * GLOBAL_SOUND_VOLUME;
	}
	
}
