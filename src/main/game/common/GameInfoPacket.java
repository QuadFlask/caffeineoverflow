package main.game.common;

import java.io.Serializable;

public class GameInfoPacket implements Serializable {
	private static final long serialVersionUID = 3865944982211604078L;

	private final int hero_hp;
	private final MobInfo mobInfo;

	public GameInfoPacket(int hero_hp, MobInfo mobInfo) {
		this.hero_hp = hero_hp;
		this.mobInfo = mobInfo;
	}

	public int getHero_hp() {
		return hero_hp;
	}

	public MobInfo getMobInfo() {
		return mobInfo;
	}

	public boolean hasMobInfo() {
		return mobInfo != null;
	}

	@Override
	public String toString() {
		return "GameInfoPacket [hero_hp=" + hero_hp + ", mobInfo=" + mobInfo + "]";
	}

	
}
