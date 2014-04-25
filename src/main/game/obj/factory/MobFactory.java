package main.game.obj.factory;

import java.util.ArrayList;
import java.util.List;

import main.game.common.MobInfo;
import main.game.event.BulletGeneratedListener;
import main.game.obj.behavior.MovingPattern.SIDE;
import main.game.obj.factory.unit.mob.Mob;
import main.game.obj.factory.unit.mob.WaveMob;

public class MobFactory {

	public static List<Mob> createFromMobInfo(MobInfo mobInfo, BulletGeneratedListener listener) {
		List<Mob> result = new ArrayList<Mob>();
		WaveMob m = new WaveMob(SIDE.LEFT, listener);
		result.add(m);
		if (mobInfo.getMobType() == 2) {
			m = new WaveMob(SIDE.RIGHT, listener);
			result.add(m);
		}
		return result;
	}

}

// WaveMob m = new WaveMob(SIDE.LEFT, mobBulletResolver);
// unitList.add(m);
//
// m = new WaveMob(SIDE.RIGHT, mobBulletResolver);
// unitList.add(m);
//
// log("sending Mob!!");