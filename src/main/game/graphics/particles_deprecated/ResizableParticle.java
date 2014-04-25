package main.game.graphics.particles_deprecated;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import main.flask.utils.Vec2D;

public class ResizableParticle extends Particle {
	public static final double MINIMUM_SIZE = 0.25;
	
	public double width, height;
	public double sizeDamping = 0.95;
	public double theta = 0;
	
	public Color color;
	public Shape s;
	
	public ResizableParticle(Vec2D p, double initVelocity, double initSizeX, double initSizeY, Color color, Shape shape) {
		super(p);
		
		this.velocity = new Vec2D(initVelocity);
		this.color = color;
		
		theta = Math.random() * Math.PI * 2;
		velocity.rotate(theta);
		
		damping = 0.9;
		
		width = initSizeX;
		height = initSizeY;
		
		s = shape;
	}
	
	public ResizableParticle(Vec2D p, double initVelocity, double initSize, Color color) {
		this(p, initVelocity, initSize, initSize, color, new Rectangle2D.Double(-initSize / 2, -initSize / 2, initSize, initSize));
	}
	
	public ResizableParticle(Vec2D p, double initVelocity, double initSizeX, double initSizeY, Color color) {
		this(p, initVelocity, initSizeX, initSizeY, color, new Ellipse2D.Double(-initSizeX / 2, -initSizeY / 2, initSizeX, initSizeY));
	}
	
	@Override
	public boolean integrate() {
		width *= sizeDamping;
		height *= sizeDamping;
		modifyShapeSize(width, height);
		return super.integrate() && (width > MINIMUM_SIZE && height > MINIMUM_SIZE);
	}
	
	@Override
	public boolean integrate(double duration) {
		width *= Math.pow(sizeDamping, duration);
		height *= Math.pow(sizeDamping, duration);
		modifyShapeSize(width, height);
		return super.integrate(duration) && (width > MINIMUM_SIZE && height > MINIMUM_SIZE);
	}
	
	public void setDamping(double velocity, double size) {
		if (velocity != SAME) this.damping = velocity;
		if (size != SAME) this.sizeDamping = size;
	}
	
	private void modifyShapeSize(double sizeX, double sizeY) {
		((RectangularShape) s).setFrame(-sizeX / 2, -sizeY / 2, sizeX, sizeY);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.translate(position.x, position.y);
		g.rotate(theta + HALF_RAD);
		
		g.fill(s);
		
		g.rotate(-(theta + HALF_RAD));
		g.translate(-position.x, -position.y);
	}
	
	public void draw_strokeOnly(Graphics2D g) {
		g.setColor(color);
		g.draw(s);
	}
}
