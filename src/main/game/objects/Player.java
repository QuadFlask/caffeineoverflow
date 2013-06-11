package main.game.objects;

import main.game.objects.units.Hero;
import main.game.objects.units.Unit;

import java.util.ArrayList;

public class Player {
	private int money;
	private long score;
	private String nickname;
	private Hero hero;
	private ArrayList<Unit> unitList; // enemy <-> ally(allies)
}
