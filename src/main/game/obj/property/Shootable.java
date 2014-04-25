package main.game.obj.property;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;

import main.flask.utils.Vec2D;
import main.game.obj.factory.unit.Unit;

public interface Shootable extends Serializable {
	public void setPosition(Vec2D pos);

	public void setVelocity(Vec2D v);

	public void setShape(int[][] shape);

	public boolean isHomming();

	public void setTargets(List<Unit> units);

	public void setTarget(Unit unit);

	public void addForce(Vec2D v);

	public boolean update(float duration);

	public void selectCloserTarget(List<Unit> units);

	public void draw(Graphics2D g);

	public Vec2D getPosition();

	public Vec2D getVelocity();

	public boolean checkInStage(Rectangle2D stage);

	public boolean isSpread();

	public List<Shootable> activateFunction();

	public int getDamage();

	public boolean isConsumed();

	public void setConsumed(boolean isConsumed);

	public void setConsumed();
}
