package main.game.obj.factory.unit.mob;

import java.awt.Graphics2D;

import main.flask.utils.Vec2D;
import main.game.graphics.effect.factory.ParticleEmitter;
import main.game.obj.behavior.AttackPattern;
import main.game.obj.behavior.MovingPattern;
import main.game.obj.factory.unit.Unit;

public class Mob extends Unit {
	private static final long serialVersionUID = -4683038775664727889L;
	protected AttackPattern attackPattern;
	protected MovingPattern movingPattern;
	
	protected float speed = 2.0f;
	
	public Mob(Vec2D pos) {
		super(pos);
	}
	
	@Override
	public boolean draw(Graphics2D g) {
		drawTest(g);
		return true;
	}
	
	@Override
	public void updatePosition(float duration) {
		Vec2D temp = movingPattern.getNextRellyPoint(position).getSub(position);
		if (temp.magnitude() > speed) temp.setMagnitude(speed);
		position.add(temp);
	}
	
	public void setAttackPattern(AttackPattern attackPattern) {
		this.attackPattern = attackPattern;
	}
	
	public void setMovingPattern(MovingPattern movingPattern) {
		this.movingPattern = movingPattern;
	}
	
	@Override
	protected void kill() {
		ParticleEmitter.makeExplosion((float) position.x, (float) position.y, 16, 0.5f, 48);
		
		if (attackPattern != null) attackPattern.clear();
		attackPattern = null;
		
		ParticleEmitter.makeBouncingText((int) position.x, (int) position.y, "+" + rewardPrice, 2, 3, 10);
	}
	
}
