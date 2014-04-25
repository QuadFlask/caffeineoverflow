package main.game.graphics.effect.factory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import main.flask.net.SimpleLogger;
import main.flask.utils.Vec2D;
import main.game.common.D;
import main.game.graphics.GraphicsDrawer;
import main.game.graphics.effect.particle.BalloonBox;
import main.game.graphics.effect.particle.CircleParticle;
import main.game.graphics.effect.particle.ColorParticle;
import main.game.graphics.effect.particle.Particle;
import main.game.obj.factory.special.BlackHole;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class ParticleEmitter {
	private static List<Particle> particles = new ArrayList<>();
	private static TweenManager tweenManager = new TweenManager();
	private static Rectangle2D.Float stage = new Rectangle2D.Float(0, 0, D.SCREEN_WIDTH, D.SCREEN_HEIGHT);
	private static BlackHole blackHole;
	private static final int PARTICLE_LIMITS = 800;

	public static void draw(Graphics2D g) {
		synchronized (particles) {
			for (Particle p : particles)
				p.draw(g);
		}
	}

	private static Runnable forceAccum = new Runnable() {
		@Override
		public void run() {
			if (blackHole.isAnimating()) synchronized (particles) {
				for (Particle p : particles)
					p.addForce(blackHole.getForce(p.getPosition()));
			}
		}
	};

	public static void setBlackHole(BlackHole blackHole) {
		ParticleEmitter.blackHole = blackHole;
	}

	public static Runnable getForceAccumRunner() {
		return forceAccum;
	}

	private static List<Particle> deleteList = new ArrayList<>();

	public static synchronized void updateParticles(float duration) {
		tweenManager.update(duration);
		duration *= 20;

		synchronized (particles) {
			for (Particle p : particles) {
				if (p.checkInStage(stage)) p.integrate(duration);
				else deleteList.add(p);
			}
			particles.removeAll(deleteList);
		}

		deleteList.clear();
	}

	public static synchronized void make(float x, float y, int count, float duration) {
		if ((count = getAvailableCount(count)) == 0) return;
		Particle particle;
		Timeline timeline = Timeline.createParallel();

		timeline.beginParallel();
		synchronized (particles) {
			for (int i = 0; i < count; i++) {
				particle = new Builder(x, y) //
						.size(Jitter.fromto(2, 5)) //
						.force(Jitter.fromto(4, 16), Jitter.range((float) Math.PI / 2, 0.3f)) //
						.build();

				particles.add(particle);
				timeline.push(Tween.to(particle, 0, duration).target(0));
			}
		}
		timeline.end();
		timeline.start(tweenManager);
	}

	private static int getAvailableCount(int count) {
		return Math.min(count, PARTICLE_LIMITS - particles.size());
	}

	public static synchronized void makeFlame(float x, float y, int count, float duration, float powerMin, float powerMax) {
		if ((count = getAvailableCount(count)) == 0) return;

		Particle particle;
		Timeline timeline = Timeline.createParallel();
		Vec2D force = new Vec2D();

		timeline.beginParallel();
		synchronized (particles) {
			for (int i = 0; i < count; i++) {
				particle = new ColorParticle(x, y, Jitter.fromto(6, 8), null);

				force.setValues(Jitter.fromto(powerMin, powerMax), 0);
				force.rotate(Jitter.range((float) Math.PI / 2, 0.3f));

				particle.addForce(force);

				particles.add(particle);
				timeline.push(Tween.to(particle, 0, duration).target(0));
			}
		}
		timeline.end();
		timeline.start(tweenManager);
	}

	public static synchronized void makeExplosion(float x, float y, int count, float duration, float exRadius) {
		if ((count = getAvailableCount(count)) == 0) return;

		Particle particle;
		Timeline timeline = Timeline.createParallel();
		Vec2D force = new Vec2D();

		timeline.beginParallel();
		synchronized (particles) {
			for (int i = 0; i < count; i++) {
				particle = new ColorParticle(x, y, Jitter.fromto(exRadius / 10, exRadius / 6), null);

				force.setValues(Jitter.fromto(exRadius, exRadius * 2), 0);
				force.rotate(Jitter.fromto(0, (float) Math.PI * 2));

				particle.addForce(force);

				particles.add(particle);
				timeline.push(Tween.to(particle, 0, duration).target(0));
			}
		}
		timeline.end();
		timeline.start(tweenManager);
	}

	public static synchronized void makeAsPolar(float x, float y, float r, float force, int count, float duration) {
		if ((count = getAvailableCount(count)) == 0) return;
		Particle particle;
		Timeline timeline = Timeline.createParallel();
		float[] temp = new float[2];

		timeline.beginParallel();
		synchronized (particles) {
			for (int i = 0; i < count; i++) {
				Jitter.circularRange(x, y, r, temp);
				particle = new Builder(temp[0], temp[1]) //
						.size(Jitter.fromto(1, 3)) //
						.force(Jitter.fromto(-force, force), Jitter.fromto(0, (float) Math.PI * 2)) //
						.build();

				particles.add(particle);
				timeline.push(Tween.to(particle, 0, duration).target(0));
			}
		}
		timeline.end();
		timeline.start(tweenManager);
	}

	public static synchronized void makeCircle(float x, float y, float size, float duration) {
		if (getAvailableCount(1) == 0) return;
		synchronized (particles) {
			CircleParticle particle = new CircleParticle(x, y, size, 1);
			particles.add(particle);
			Tween.to(particle, 0, duration).target(0).ease(CircleParticle.tweenEquation).start(tweenManager);
		}
	}

	public static synchronized void makeBalloonBox(int x, int y, String text, int textSize, float duration) {
		BalloonBox box = new BalloonBox(new Vec2D(x, y), text, textSize);

		synchronized (particles) {
			particles.add(box);
			Tween.to(box, 0, duration).target(0).ease(CircleParticle.tweenEquation).start(tweenManager);
		}
	}

	public static synchronized void makeBouncingText(int x, int y, String text, int textSize, float duration, float bouncingAmount) {
		BouncingText t = new BouncingText(new Vec2D(x, y), text, textSize, bouncingAmount);

		synchronized (particles) {
			particles.add(t);
			Tween.to(t, 0, duration).target(0).ease(CircleParticle.tweenEquation).start(tweenManager);
		}
	}

	public static synchronized void makeAllertBox(int x, int y, String text, int textSize, float duration) {
		// BalloonBox box = new BalloonBox(new Vec2D(x, y), text, textSize);
		//
		// synchronized (particles) {
		// particles.add(box);
		// Tween.to(box, 0,
		// duration).target(0).ease(CircleParticle.tweenEquation).start(tweenManager);
		// }
	}

	static class BouncingText extends Particle {
		private static final Color DEFAULT_COLOR = new Color(0xccffff00, true);
		private String text;
		private int count;
		private float bouncingAmount;
		private int width = 0;
		private int textSize;
		private int bottomY;

		public BouncingText(Vec2D pos, String text, int textSize, float bouncingAmount) {
			super(pos);
			this.text = text;
			this.textSize = textSize;
			this.bouncingAmount = (float) pos.y + bouncingAmount;
			this.color = DEFAULT_COLOR;
			makeShape();
		}

		protected void makeShape() {
			this.width = GraphicsDrawer.getTextWidth(text, (int) textSize);
			bottomY = (int) size * 2 - textSize * 2;
		}

		@Override
		public void draw(Graphics2D g) {
			position.y -= life * 1.5f;
			g.translate((int) position.x, (int) position.y);

			GraphicsDrawer.drawBitmapText(g, text, (int) textSize, 0, color, -width / 2, (int) (size / 2 - textSize / 2));

			g.translate(-(int) position.x, -(int) position.y);
		}

		// GraphicsDrawer.drawBitmapText(g, )
		@Override
		public boolean integrate() {
			return false;
		}

		@Override
		public boolean integrate(double duration) {
			return false;
		}

		@Override
		protected void updateWithLife() {}

		@Override
		public void addForce(Vec2D f) {}

		@Override
		public void addForce(float fx, float fy) {}

		@Override
		public void clearAccumulator() {}

	}

	public static class Builder {
		private float posx, posy, size = 10, power = 1, rad;
		private Color color;
		private Class particleClass;

		public Builder(Class particleClass, float posx, float posy) {
			this.particleClass = particleClass;
			this.posx = posx;
			this.posy = posy;
		}

		public Builder(float posx, float posy) {
			this.posx = posx;
			this.posy = posy;
		}

		public Builder size(float size) {
			this.size = size;
			return this;
		}

		public Builder force(float power, float rad) {
			this.power = power;
			this.rad = rad;
			return this;
		}

		public Builder color(Color color) {
			this.color = color;
			return this;
		}

		public Particle build() {
			Vec2D force = new Vec2D(power);
			force.rotate(rad);
			Particle particle = null;

			if (particleClass == null) {
				particle = new Particle(posx, posy);
			} else {
				try {
					Constructor constructor = (Constructor) particleClass.getConstructor();
					particle = (Particle) constructor.newInstance();
				} catch (Exception e) {
					particle = new Particle(posx, posy);
				}
			}

			particle.size = size;
			particle.addForce(force);
			particle.setColor(color);

			return particle;
		}
	}
}

// private void generatePolar(ArrayList<NormalBullet> bulletList, int total,
// float power, IMAGE_ID shape) {
// NormalBullet bullet;
// float theta = 0;
// final float thetaIter = (float) (2.0 * Math.PI / total);
// Vec2D force;
//
// for (int i = 0; i < total; i++, theta += thetaIter) {
// bullet = new NormalBullet((int) body.position.x, (int) body.position.y,
// shape);
// force = new Vec2D(power);
// force.rotate(theta);
// bullet.body.addForce(force);
// bullet.body.integrate_lite();
// bulletList.add(bullet);
// }
// }
//
// private void generatePolarSequence(ArrayList<NormalBullet> bulletList, int
// count, int time, Point ahead, float range, float addVelocity, IMAGE_ID shape)
// {
// float aheadTheta;
// for (int i = 0; i < time; i++) {
// aheadTheta = (float) Math.atan2(ahead.y, ahead.x) - range / 2 * count;
// generatePolarDiffVelocity(bulletList, count, aheadTheta, range, 1.8f +
// addVelocity / time * i, shape);
// }
// }
//
// private void generatePolar(ArrayList<NormalBullet> bulletList, int total,
// Point ahead, float range, IMAGE_ID shape) {
// float aheadTheta = (float) Math.atan2(ahead.y, ahead.x) - range / 2 * total;
// generatePolar(bulletList, total, aheadTheta, range, shape);
// }
//
// private void generatePolar(ArrayList<NormalBullet> bulletList, int total,
// float aheadTheta, float range, IMAGE_ID shape) {
// generatePolarDiffVelocity(bulletList, total, aheadTheta, range, 1.8f, shape);
// }
//
// private void generatePolarDiffVelocity(ArrayList<NormalBullet> bulletList,
// int total, float aheadTheta, float range, float additionalPower, IMAGE_ID
// shape) {
// NormalBullet bullet;
// float theta = aheadTheta;
// final float thetaIter = (float) (2.0 * Math.PI / total * range);
// Vec2D force;
//
// for (int i = 0; i < total; i++, theta += thetaIter) {
// bullet = new NormalBullet((int) body.position.x, (int) body.position.y,
// shape);
// force = new Vec2D(additionalPower);
// force.rotate(theta);
// bullet.body.addForce(force);
// bullet.body.integrate_lite();
// bulletList.add(bullet);
// }
// }
//
// float t;
//
// private void generatePolarStepByStep(ArrayList<NormalBullet> bulletList, int
// total, float power, IMAGE_ID shape) {
// NormalBullet bullet;
// float theta = (float) (2.0 * Math.PI / total) * t;
// t += 1f;
// Vec2D force;
//
// bullet = new NormalBullet((int) body.position.x, (int) body.position.y,
// shape);
// force = new Vec2D(power);
// force.rotate(theta);
// bullet.body.addForce(force);
// bullet.body.integrate_lite();
// bulletList.add(bullet);
// }
//
// private void generateRandom(ArrayList<NormalBullet> bulletList, int total,
// float power, IMAGE_ID shape) {
// NormalBullet bullet;
// float theta;
// Vec2D force;
//
// for (int i = 0; i < total; i++) {
// theta = (float) (2.0f * Math.PI * rand.nextFloat());
// bullet = new NormalBullet((int) body.position.x, (int) body.position.y,
// shape);
// force = new Vec2D(power);
// force.rotate(theta);
// bullet.body.addForce(force);
// bulletList.add(bullet);
// }
// }
//
// private void generateToPoint(ArrayList<NormalBullet> bulletList, Point ahead,
// float power, IMAGE_ID shape) {
// float aheadTheta;
// NormalBullet bullet;
// Vec2D force;
//
// aheadTheta = (float) Math.atan2(ahead.y, ahead.x);
// bullet = new NormalBullet((int) body.position.x, (int) body.position.y,
// shape);
// force = new Vec2D(power);
// force.rotate(aheadTheta);
// bullet.body.addForce(force);
// bulletList.add(bullet);
// }
