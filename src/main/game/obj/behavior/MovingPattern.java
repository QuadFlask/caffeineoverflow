package main.game.obj.behavior;

import java.io.Serializable;

import main.flask.utils.Vec2D;

public class MovingPattern implements Serializable {
	private static final long serialVersionUID = 173631782545887972L;
	protected static int CENTER = 250;

	public static enum SIDE implements Serializable {
		LEFT(-1), RIGHT(1);

		private int s;

		SIDE(int s) {
			this.s = s;
		}

		public int getSign() {
			return this.s;
		}
	}

	protected Vec2D[] points;
	protected int cursor = 0;
	protected float range = 10;
	protected boolean loop = true;

	public Vec2D getNextRellyPoint(Vec2D position) {
		if (points[cursor].getSub(position).magnitude() < range) next();
		return points[cursor];
	}

	protected void next() {
		cursor++;
		if (cursor >= points.length) cursor = (loop) ? 0 : points.length - 1;
	}

	public boolean isFinalPosition() {
		return cursor == points.length - 1;
	}

	public Vec2D getFirstRelly() {
		return points[0];
	}

	public Vec2D getLastRelly() {
		return points[points.length - 1];
	}

}
