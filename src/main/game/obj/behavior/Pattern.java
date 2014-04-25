package main.game.obj.behavior;

import java.util.ArrayList;
import java.util.List;

import main.game.obj.factory.bullet.Bullet1;
import main.game.obj.property.Shootable;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;

public class Pattern implements TweenCallback {
	private AttackPattern parent;
	
	@Override
	public void onEvent(int type, BaseTween<?> tween) {
		List<Shootable> shootables = new ArrayList<Shootable>();
		
		//		shootables.addAll(Attack.simpleStraigh(Bullet1.class, parent.getPosition(), 10));
		shootables.addAll(Attack.polar(Bullet1.class, 32, parent.getPosition(), 2));
		
		parent.addShootables(shootables);
	}
	
	public void setParent(AttackPattern attackPattern) {
		this.parent = attackPattern;
	}
	
	public float getPreDelay() {
		return 0;
	}
	
	public float getPostDelay() {
		return 2;
	}
	
}
