package main.game.graphics.effect.particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import main.flask.utils.Vec2D;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;

public class Particle {
	static {
		Tween.registerAccessor(Particle.class, new ParticleAccessor());
	}
	
	public static final float HALF_RAD = (float) (Math.PI / 2);
	
	public Vec2D position;
	public Vec2D velocity;
	public Vec2D accelaration;
	public Vec2D forceAccum;
	public double inverseMass = 1;
	public double damping = 1;
	
	public Color color = Color.white;
	public Shape shape;
	public float life = 1;
	public float theta;
	public float size = 10;
	
	public Particle() {
		init(null);
	}
	
	public Particle(Vec2D p) {
		this(p.x, p.y);
	}
	
	public Particle(double x, double y) {
		init(null);
		position.x = x;
		position.y = y;
	}
	
	public Particle(double x, double y, float size) {
		this.size = size;
		init(null);
		position.x = x;
		position.y = y;
	}
	
	public Particle(double x, double y, float size, Shape shape) {
		this.size = size;
		init(shape);
		position.x = x;
		position.y = y;
	}
	
	protected void init(Shape shape) {
		position = new Vec2D();
		velocity = new Vec2D();
		accelaration = new Vec2D();
		forceAccum = new Vec2D();
		
		setShape(shape);
	}
	
	protected void setShape(Shape shape) {
		if (shape == null) this.shape = new Ellipse2D.Float(-size / 2, -size / 2, size, size);
		else this.shape = shape;
	}
	
	public void addForce(Vec2D f) {
		forceAccum.add(f);
	}
	
	public void addForce(float fx, float fy) {
		forceAccum.add(fx, fy);
	}
	
	public void clearAccumulator() {
		forceAccum.clear();
		accelaration.clear();
	}
	
	public double getMass() {
		return 1 / inverseMass;
	}
	
	public double getInverseMass() {
		return inverseMass;
	}
	
	public void setMass(double m) {
		if (inverseMass < 0) return;
		inverseMass = m;
	}
	
	public Vec2D getPosition() {
		return position;
	}
	
	public void setPosition(double x, double y) {
		position.x = x;
		position.y = y;
	}
	
	public void setPosition(Vec2D v) {
		position = v.clone();
	}
	
	public void setColor(Color color) {
		if (color != null) this.color = color;
	}
	
	public boolean integrate() {
		if (inverseMass <= 0) return false;
		accelaration.add(forceAccum);
		velocity.add(accelaration);
		velocity.multiply(damping);
		position.add(velocity);
		
		updateWithLife();
		clearAccumulator();
		return true;
	}
	
	public boolean integrate(double duration) {
		if (inverseMass <= 0) return false;
		accelaration.addScaledVector(forceAccum, duration);
		velocity.addScaledVector(accelaration, duration);
		velocity.multiply(Math.pow(damping, duration));
		position.addScaledVector(velocity, duration);
		
		updateWithLife();
		clearAccumulator();
		return true;
	}
	
	protected void updateWithLife() {
		float tSize = size * life;
		((RectangularShape) shape).setFrame(-tSize / 2, -tSize / 2, tSize, tSize);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.translate((float) position.x, (float) position.y);
		//		g.rotate(theta + HALF_RAD);
		
		g.fill(shape);
		
		//		g.rotate(-(theta + HALF_RAD));
		g.translate(-(float) position.x, -(float) position.y);
	}
	
	public boolean checkInStage(Rectangle2D stage) {
		if (position.x > stage.getMaxX() || position.x < stage.getMinX()) return false;
		if (position.y > stage.getMaxY() || position.y < stage.getMinY()) return false;
		return life > 0;
	}
	
	@Override
	public String toString() {
		return "Particle [position=" + position + ", velocity=" + velocity + ", accelaration=" + accelaration + ", forceAccum=" + forceAccum + ", inverseMass=" + inverseMass + ", damping=" + damping + "]";
	}
}

class ParticleAccessor implements TweenAccessor<Particle> {
	@Override
	public int getValues(Particle target, int type, float[] result) {
		result[0] = target.life;
		return 1;
	}
	
	@Override
	public void setValues(Particle target, int type, float[] result) {
		target.life = result[0];
	}
}