package main.game.graphics.particles_deprecated;

import java.awt.Graphics2D;

import main.flask.utils.Vec2D;

public class Particle {
	public static final int SAME = Integer.MIN_VALUE;
	public static final double HALF_RAD = Math.PI / 2;
	
	public Vec2D position;
	public Vec2D velocity;
	public Vec2D accelaration;
	public Vec2D forceAccum;
	public double inverseMass;
	public double damping;
	
	public Particle() {
		init();
	}
	
	public Particle(double $x, double $y) {
		init();
		position.x = $x;
		position.y = $y;
	}
	
	public Particle(Vec2D p) {
		this(p.x, p.y);
	}
	
	private void init() {
		position = new Vec2D();
		velocity = new Vec2D();
		accelaration = new Vec2D();
		forceAccum = new Vec2D();
		
		inverseMass = 1;
		damping = 1;
	}
	
	public void addForce(Vec2D f) {
		forceAccum.add(f);
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
	
	public boolean integrate() {
		if (inverseMass <= 0) return false;
		accelaration.add(forceAccum);
		velocity.add(accelaration);
		velocity.multiply(damping);
		position.add(velocity);
		
		clearAccumulator();
		return true;
	}
	
	public boolean integrate(double duration) {
		if (inverseMass <= 0) return false;
		accelaration.addScaledVector(forceAccum, duration);
		velocity.addScaledVector(accelaration, duration);
		velocity.multiply(Math.pow(damping, duration));
		position.addScaledVector(velocity, duration);
		
		clearAccumulator();
		return true;
	}
	
	@Override
	public String toString() {
		return "Particle [position=" + position + ", velocity=" + velocity + ", accelaration=" + accelaration + ", forceAccum=" + forceAccum + ", inverseMass=" + inverseMass + ", damping=" + damping + "]";
	}
	
	public void draw(Graphics2D g) {}
}
