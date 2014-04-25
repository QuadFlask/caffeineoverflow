package main.game.obj.property;

import main.flask.utils.Vec2D;

public interface ForceSource {
	public static final Vec2D Z_FORCE = new Vec2D(0, 0);
	
	public Vec2D getForce();
	
	public Vec2D getForce(Vec2D target);
	
	public Vec2D getForce(Vec2D a, Vec2D b);
}
