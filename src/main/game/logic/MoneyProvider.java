package main.game.logic;

import java.util.List;

import main.game.obj.factory.unit.Unit;

public class MoneyProvider {
	private int money = 0;
	private float caffeine = 0;
	
	public synchronized int getMoney() {
		int temp = money;
		money = 0;
		return temp;
	}
	
	public synchronized float getCaffeine() {
		float temp = caffeine;
		caffeine = 0;
		return temp;
	}
	
	public void setProvided(List<Unit> unitList) {
		int sum = 0;
		for (Unit unit : unitList) {
			sum += unit.getRewardPrice();
		}
		addMoney(sum);
	}
	
	public void addMoney(int amount) {
		money += amount;
	}
	
	public void addCaffeine(float amount) {
		caffeine += amount;
	}
}
