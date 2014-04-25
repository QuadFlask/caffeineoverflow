package main.game.obj.factory;

import main.game.obj.factory.special.BlackHole;
import main.game.obj.factory.unit.Unit;
import main.game.obj.factory.unit.mob.Mob;
import main.game.obj.factory.unit.mob.WaveMob;

public class UnitFactory {

	public static final int[] MOB_COOLDOWN_TIME = new int[] { 1, 5, 12, 30, 60, 40, 45, 60, 120, 0 };
	public static final int[] MOB_COST = new int[] { 80, 200, 450, 1600, 1200, 1500, 2250, 3600, 5000 };
	public static final Class[] UNIT_CLASS = new Class[] { //
	Mob.class, //
			Mob.class, //
			BlackHole.class, //
			BlackHole.class, //
			BlackHole.class, //
			BlackHole.class, //
			BlackHole.class, //
			BlackHole.class, //
			BlackHole.class, //
			BlackHole.class, //
	};

	public static Class requestUnit(int i) {
		System.out.println("Mob created! : " + i + " / " + UNIT_CLASS[i].toString());
		return UNIT_CLASS[i];
	}

	public static Unit createUnit(Class unitClass) {
		return null;
	}
}
