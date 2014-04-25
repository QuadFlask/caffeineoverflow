package main.game.graphics.particles_deprecated;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import main.flask.utils.Vec2D;

public class ResizableParticleDPRECATED {
	Vec2D p;
	Vec2D v;
	Color color;
	double velocityReduction = 0.9;
	double sizeReduction = 0.89;
	double sizeX = 10;
	double sizeY = 10;
	double theta = 0;
	Shape s;
	
	public ResizableParticleDPRECATED(Vec2D p, double initVelocity, double initSizeX, double initSizeY, Color color, Shape shape) {
		this.p = p.clone();
		this.v = new Vec2D(initVelocity);
		this.color = color;
		
		theta = Math.random() * Math.PI * 2;
		v.rotate(theta);
		
		sizeX = initSizeX;
		sizeY = initSizeY;
		
		s = shape;
	}
	
	public ResizableParticleDPRECATED(Vec2D p, double initVelocity, double initSize, Color color) {
		this(p, initVelocity, initSize, initSize, color, new Rectangle2D.Double(-initSize / 2, -initSize / 2, initSize, initSize));
	}
	
	public ResizableParticleDPRECATED(Vec2D p, double initVelocity, double initSizeX, double initSizeY, Color color) {
		this(p, initVelocity, initSizeX, initSizeY, color, new Ellipse2D.Double(-initSizeX / 2, -initSizeY / 2, initSizeX, initSizeY));
	}
	
	public void setReduction(float reduction) {
		this.velocityReduction = reduction;
	}
	
	public boolean integrate(double t) {
		p.add(v.getMultiply(t));
		v.multiply(velocityReduction * t * (1 - t));
		sizeX *= sizeReduction;
		sizeY *= sizeReduction;
		modifyShapeSize(sizeX, sizeY);
		
		if (sizeX < 0.25f) return false;
		return true;
	}
	
	public boolean integrate() {
		p.add(v);
		v.multiply(velocityReduction);
		sizeX *= sizeReduction;
		sizeY *= sizeReduction;
		modifyShapeSize(sizeX, sizeY);
		
		if (sizeX < 0.25f) return false;
		return true;
	}
	
	private void modifyShapeSize(double sizeX, double sizeY) {
		((RectangularShape) s).setFrame(-sizeX / 2, -sizeY / 2, sizeX, sizeY);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.translate(p.x, p.y);
		g.rotate(theta + Math.PI / 2);
		
		g.fill(s);
		
		g.rotate(-(theta + Math.PI / 2));
		g.translate(-p.x, -p.y);
	}
	
	public void draw_strokeOnly(Graphics2D g) {
		g.setColor(color);
		g.draw(s);
	}
}