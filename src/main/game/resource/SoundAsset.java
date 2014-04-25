package main.game.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SoundAsset {
	private static HashMap<SOUND_ID, String> SOUND_DIRECTORY = new HashMap<SOUND_ID, String>();
	private static HashMap<SOUND_ID, File> SOUND_LIST = new HashMap<SOUND_ID, File>();
	private static List<SOUND_ID> BGM_LIST = new ArrayList<SOUND_ID>();
	
	public static enum SOUND_ID {
		// BGM_
		BGM_BEGINNING_THE_FLIGHT,
		BGM_HALFCF,
		BGM_LOWPASS,
		BGM_OBSERVED_SKY,
		BGM_RAINY_DAY,
		BGM_SPRINGWIND,
		BGM_WET_STREET_COLOR_SIDE,
		BGM_WHITEWIND,
		BGM_THE_TOWER_OF_SHADOW,
		
		// QUAKE_
		QUAKE_FIRSTBLOOD,
		QUAKE_DOUBLEKILL,
		QUAKE_TRIPLEKILL,
		QUAKE_MONSTERKILL,
		QUAKE_ULTRAKILL,
		QUAKE_LUDICROUSKILL,
		QUAKE_GODLIKE,
		QUAKE_UNSTOPPABLE,
		QUAKE_WICKEDSICK,
		QUAKE_RAMPAGE,
		QUAKE_HOLYSHIT,
		QUAKE_KILLINGSPREE,
		QUAKE_ENDOFVOTE,
		QUAKE_RESTRICTEDWEAPON,
		QUAKE_IMPRESSIVE,
		QUAKE_WARNING,
		QUAKE_ROCK_HIT4,
		
		// EFFECTS_
		EFFECTS_UP,
		EFFECTS_BLACKHOLE,
		EFFECTS_BLACKHOLE2,
		EFFECTS_BOUGHT,
		EFFECTS_BOUGHT2,
		EFFECTS_CANCEL,
		EFFECTS_ELECTOR_LASER,
		EFFECTS_ELECTRO,
		EFFECTS_ELEXCTRO,
		EFFECTS_ENDTIME,
		EFFECTS_ENEMY_SHOT,
		EFFECTS_ENTER,
		EFFECTS_EXPLOSION,
		EFFECTS_EXPLOSION2,
		EFFECTS_EXPLOSION3,
		EFFECTS_EXPLOSION4,
		EFFECTS_FREEZE,
		EFFECTS_GRAZE,
		EFFECTS_HEALING,
		EFFECTS_HEALING2,
		EFFECTS_HEALING3,
		EFFECTS_LASER,
		EFFECTS_LASER2,
		EFFECTS_SELECT,
		EFFECTS_SELECT2,
		EFFECTS_SELECT3,
		EFFECTS_SELECT4,
		EFFECTS_SE_BONUS,
		EFFECTS_SE_BONUS2,
		EFFECTS_SE_BONUS4,
		EFFECTS_SE_CHANGEITEM,
		EFFECTS_SE_LGODS1,
		EFFECTS_SE_LGODS2,
		EFFECTS_SE_LGODS3,
		EFFECTS_SE_POWER0,
		EFFECTS_SE_POWER1,
		EFFECTS_SE_POWERUP,
		EFFECTS_SE_TIMEOUT,
		EFFECTS_SE_TIMEOUT2,
		EFFECTS_SE_TIMESTOP0,
		EFFECTS_SE_UFO,
		EFFECTS_SHOT,
		EFFECTS_SHOT_SHORT,
		EFFECTS_SHOT00,
		EFFECTS_SHOT06,
		EFFECTS_SHOT08,
		EFFECTS_SHOT_LASER,
		EFFECTS_SHOT_LASER3,
		EFFECTS_SHOT_PHOTON,
		EFFECTS_SHOT_PHOTON2,
		EFFECTS_SPEL00,
		EFFECTS_SPEL02,
		EFFECTS_SPEL04,
		EFFECTS_STRANGE,
		EFFECTS_STRONG_LASER,
		
		EFFECTS_BUTTON14, // select sound!
		EFFECTS_ALERT_CLINK,
		EFFECTS_BEEPCLEAR,
		EFFECTS_BIGREWARD,
		EFFECTS_BOULDER_IMPACT_HARD2,
		EFFECTS_CONCRETE_IMPACT_BULLET1,
		EFFECTS_GLASS_SHEET_BREAK3,
		EFFECTS_HELPFUL_EVENT_1,
		EFFECTS_SUIT_DENYDEVICE,
	}
	
	static {
		for (SOUND_ID id : SOUND_ID.values())
			SOUND_DIRECTORY.put(id, soundDirectoryResolver(id));
		
		SOUND_ID item = null;
		File soundFile;
		Iterator<SOUND_ID> iter = SOUND_DIRECTORY.keySet().iterator();
		
		while (iter.hasNext()) {
			item = iter.next();
			System.out.println("loading Files... " + item.toString());
			soundFile = new File(SOUND_DIRECTORY.get(item));
			
			if (soundFile.exists()) {
				SOUND_LIST.put(item, soundFile);
			} else System.err.println("Sound file not found: " + SOUND_DIRECTORY.get(item));
		}
	}
	
	private static String soundDirectoryResolver(SOUND_ID id) {
		String name = id.name().toLowerCase();
		int firstUnderScore = name.indexOf("_");
		
		String dir = name.split("_")[0];
		String fileName = name.substring(firstUnderScore + 1);
		
		if (dir.equals("BGM")) BGM_LIST.add(id);
		
		return String.format("%s/%s/%s/%s.wav", AssetConfigs.ASSET_ROOT, AssetConfigs.SOUND_ROOT, dir, fileName);
	}
	
	public static boolean isBGM(SOUND_ID id) {
		return BGM_LIST.contains(id);
	}
	
	public static File get(SOUND_ID id) {
		return SoundAsset.SOUND_LIST.get(id);
	}
}
