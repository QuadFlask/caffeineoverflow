package main.game.obj.behavior;

import main.flask.utils.Vec2D;

public class TestMovingB extends MovingPattern {
	private Vec2D center = new Vec2D(250, 400);
	private double range = 100;
	
	public TestMovingB() {
		this.points = new Vec2D[] {//  
				new Vec2D(center.x - range / 2, center.y - range / 2),//
				new Vec2D(center.x + range / 2, center.y - range / 2),//
				new Vec2D(center.x + range / 2, center.y + range / 2),//
				new Vec2D(center.x - range / 2, center.y + range / 2),//
		};
	}
}
