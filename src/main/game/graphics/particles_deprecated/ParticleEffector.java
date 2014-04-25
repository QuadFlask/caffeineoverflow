package main.game.graphics.particles_deprecated;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.flask.utils.FlaskUtil;
import main.flask.utils.Vec2D;
import main.game.graphics.Integratable;

public class ParticleEffector extends Integratable {
	public enum SHAPE_TYPE {
		RECT,
		OVAL
	}
	
	private static List<ResizableParticle> resizableParticles = new ArrayList<ResizableParticle>();
	
	private ParticleEffector() {}
	
	public static void makeEffect(Vec2D to, int count, double initV, double initSize, Color color, SHAPE_TYPE type) {
		switch (type) {
			case OVAL :
				for (int i = 0; i < count; i++) {
					resizableParticles.add(new ResizableParticle(to, initV, initSize, initSize * 2, color));
				}
				break;
			default :
			case RECT :
				for (int i = 0; i < count; i++) {
					resizableParticles.add(new ResizableParticle(to, initV, initSize, color));
				}
				break;
		}
	}
	
	public static void makeEffect(Vec2D to, int count, double initV, double initSize, Jitter jitter, SHAPE_TYPE type) {
		switch (type) {
			case OVAL :
				double size;
				for (int i = 0; i < count; i++) {
					size = jitter.getInitSize(initSize);
					resizableParticles.add(new ResizableParticle(jitter.getPosition(to), jitter.getInitV(initV), size, size * 2, jitter.getColor()));
				}
				break;
			default :
			case RECT :
				for (int i = 0; i < count; i++) {
					resizableParticles.add(new ResizableParticle(jitter.getPosition(to), jitter.getInitV(initV), jitter.getInitSize(initSize), jitter.getColor()));
				}
				break;
		}
	}
	
	public static synchronized void draw(Graphics2D g) {
		Iterator<ResizableParticle> i = new ArrayList<ResizableParticle>(resizableParticles).iterator();
		while (i.hasNext()) {
			i.next().draw(g);
		}
	}
	
	public static void integrate() {
		Iterator<ResizableParticle> i = new ArrayList<ResizableParticle>(resizableParticles).iterator();
		ResizableParticle p;
		while (i.hasNext()) {
			p = i.next();
			if (!p.integrate()) resizableParticles.remove(p);
		}
	}
	
	public static void integrate(double t) {
		Iterator<ResizableParticle> i = new ArrayList<ResizableParticle>(resizableParticles).iterator();
		ResizableParticle p;
		while (i.hasNext()) {
			p = i.next();
			if (!p.integrate(t)) resizableParticles.remove(p);
		}
	}
	
	public static Jitter jitter() {
		return new Jitter();
	}
	
	public static class Jitter {
		double x, y, initV, initSize, velocityReduction, sizeReduction;
		int count;
		Color[] colors;
		
		public Jitter() {
			x = y = 0;
			initSize = 10;
			initV = 10;
			velocityReduction = sizeReduction = 0.9;
		}
		
		public Jitter pos(double x, double y) {
			this.x = x;
			this.y = y;
			return this;
		}
		
		public Jitter initV(double initV) {
			this.initV = initV;
			return this;
		}
		
		public Jitter initSize(double initSize) {
			this.initSize = initSize;
			return this;
		}
		
		public Jitter velocityReduction(double velocityReduction) {
			this.velocityReduction = velocityReduction;
			return this;
		}
		
		public Jitter sizeReduction(double sizeReduction) {
			this.sizeReduction = sizeReduction;
			return this;
		}
		
		public Jitter count(int count) {
			this.count = count;
			return this;
		}
		
		public Jitter colors(Color[] colors) {
			this.colors = colors;
			return this;
		};
		
		double getX(double v) {
			return v + x * (Math.random() - 0.5) * 2;
		}
		
		double getY(double v) {
			return v + y * (Math.random() - 0.5) * 2;
		}
		
		Vec2D getPosition(Vec2D p) {
			return p.getAdd(x, y);
		}
		
		double getInitV(double v) {
			return v + initV * (Math.random() - 0.5) * 2;
		}
		
		double getInitSize(double v) {
			return v + initSize * (Math.random() - 0.5) * 2;
		}
		
		double getVelocityReduction(double v) {
			return v + velocityReduction * (Math.random() - 0.5) * 2;
		}
		
		double getSizeReduction(double v) {
			return v + sizeReduction * (Math.random() - 0.5) * 2;
		}
		
		int getCount(double v) {
			return (int) (v + count * (Math.random() - 0.5)) * 2;
		}
		
		Color getColor() {
			return FlaskUtil.colorInterpolate(colors, Math.random());
		}
	}
}
