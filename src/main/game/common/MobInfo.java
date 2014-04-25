package main.game.common;

import java.io.Serializable;

public class MobInfo implements Serializable {
	private static final long serialVersionUID = 6377103285582948405L;

	private final int mobType;
	private final int mob_x;
	private final int mob_hp;
	private final int mob_price;

	public MobInfo(int mobType, int mob_x, int mob_hp, int mob_price) {
		this.mobType = mobType;
		this.mob_x = mob_x;
		this.mob_hp = mob_hp;
		this.mob_price = mob_price;
	}

	public int getMobType() {
		return mobType;
	}

	public int getMob_x() {
		return mob_x;
	}

	public int getMob_hp() {
		return mob_hp;
	}

	public int getMob_price() {
		return mob_price;
	}

	@Override
	public String toString() {
		return "MobInfo [mobType=" + mobType + ", mob_x=" + mob_x + ", mob_hp=" + mob_hp + ", mob_price=" + mob_price + "]";
	}
}