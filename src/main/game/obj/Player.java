package main.game.obj;

import main.game.obj.factory.unit.Hero;
import main.game.obj.factory.unit.Unit;

import java.util.ArrayList;

public class Player {
	private int money;
	private long score;
	private String nickname;
	private Hero hero;
	private ArrayList<Unit> unitList; // enemy <-> ally(allies)
}
