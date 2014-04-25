package main.game.graphics.effect.particle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;

import main.flask.utils.FlaskUtil;
import main.flask.utils.Vec2D;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Expo;

public class CircleParticle extends Particle {
	public Stroke basicStroke;
	public static TweenEquation tweenEquation = Expo.OUT;
	public static final Color[] colors = new Color[] { new Color(0x00ffffff, true), new Color(0xffffffff, true) };
	
	public CircleParticle(float x, float y, float size, float thickness) {
		super(x, y, size);
		basicStroke = new BasicStroke(thickness);
		updateWithLife();
	}
	
	@Override
	public void addForce(Vec2D f) {}
	
	@Override
	public boolean integrate() {
		updateWithLife();
		return true;
	}
	
	@Override
	public boolean integrate(double duration) {
		updateWithLife();
		return true;
	}
	
	@Override
	protected void updateWithLife() {
		float tSize = size * (1 - life);
		((RectangularShape) shape).setFrame(-tSize / 2, -tSize / 2, tSize, tSize);
		color = FlaskUtil.colorInterpolate(colors, life);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.setStroke(basicStroke);
		
		g.translate((float) position.x, (float) position.y);
		
		g.draw(shape);
		
		g.translate(-(float) position.x, -(float) position.y);
	}
	
}
