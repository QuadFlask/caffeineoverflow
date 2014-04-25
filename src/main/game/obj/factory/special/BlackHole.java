package main.game.obj.factory.special;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import main.flask.imageprocessing.PinchFilter;
import main.flask.utils.Vec2D;
import main.game.graphics.effect.factory.ParticleEmitter;
import main.game.obj.factory.BulletFactory;
import main.game.obj.factory.unit.Unit;
import main.game.obj.property.Shootable;
import main.game.resource.GameSoundPlayer;
import main.game.resource.SoundAsset.SOUND_ID;
import main.game.view.ui.DrawableComponent;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;

public class BlackHole extends Unit implements TweenCallback, DrawableComponent {
	static {
		Tween.registerAccessor(BlackHole.class, new BlackHoleAccessor());
	}

	public static final double G = -5000;
	private float k = 1;

	private int ix, iy;
	private int srcWidth = 0;
	private int radius = 20;

	private float t = 0;
	private float angle = 8f;
	private float amount = 0.8f;
	private boolean isAnimating = false;
	private float duration = 5;
	private static TweenEquation tweenEquation = Elastic.INOUT;// Expo.INOUT;
	private int width;

	private BufferedImage tempBuffer, src;
	private Runnable blackHoleEffectRunner = new Runnable() {
		@Override
		public void run() {
			synchronized (tempBuffer) {
				PinchFilter.preIndexingFilter(srcWidth, tempBuffer, ix, iy, radius, angle * t, amount * t);
			}
		}
	};

	public BlackHole(Vec2D position, int SRCWidth, int size) {
		super(position);
		setPosition(position.x, position.y);
		this.srcWidth = SRCWidth;
		this.radius = size;

		width = size * 2;
		tempBuffer = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
	}

	public Vec2D getForce(Vec2D target) {
		Vec2D diff = target.getSub(position);
		double d = diff.magnitude();
		if (d <= 0) return null;

		double e = G * k / (d * d * d + 10000);

		Vec2D force = new Vec2D();
		force.x = e * diff.x;
		force.y = e * diff.y;
		return force;
	}

	public void startEffect(TweenManager manager) { // tween!
		if (!isAnimating) {
			t = 0;
			isAnimating = true;
			Tween.to(this, 0, duration).target(1).ease(tweenEquation).setCallback(this).repeatYoyo(1, duration).start(manager);
			GameSoundPlayer.play(SOUND_ID.EFFECTS_BLACKHOLE, 0.75f);
		}
	}

	// on effect finished
	@Override
	public void onEvent(int arg0, BaseTween<?> arg1) {
		isAnimating = false;
	}

	public void testDraw(Graphics2D g) {
		g.setColor(new Color(0xffccff00, true));
		g.fillOval((int) position.x - radius / 2, (int) position.y - radius / 2, radius, radius);
	}

	@Override
	public boolean draw(Graphics2D g) {
		if (isAnimating) {
			synchronized (tempBuffer) {
				PinchFilter.setWithIndex(src, tempBuffer);
			}
			g.drawImage(tempBuffer, ix - radius, iy - radius, width, width, null);
			ParticleEmitter.makeAsPolar((float) position.x, (float) position.y, radius * 1.2f, 32, 1, 10);
		}
		return isAnimating;
	}

	public void setSourceBuffer(BufferedImage src) {
		this.src = src;
	}

	public void setPosition(double x, double y) {
		position.x = x;
		position.y = y;
		this.ix = (int) position.x;
		this.iy = (int) position.y;
	}

	public Runnable getBackgroundRunner() {
		return blackHoleEffectRunner;
	}

	public void setSRCWidth(int sRCWidth) {
		srcWidth = sRCWidth;
	}

	public int getWidth() {
		return tempBuffer.getWidth();
	}

	public int getHeight() {
		return tempBuffer.getHeight();
	}

	public float getT() {
		return t;
	}

	public void setT(float t) {
		this.t = t;
	}

	public boolean isAnimating() {
		return isAnimating;
	}

	public void setDuration(float duration) {
		this.duration = duration / 3f;
	}

	// public int getX() {
	// return ix - radius;
	// }
	//
	// public int getY() {
	// return iy - radius;
	// }
	// public BufferedImage getBufferedImage() {
	// synchronized (tempBuffer) {
	// PinchFilter.setWithIndex(src, tempBuffer);
	// }
	// return tempBuffer;
	// }
	//
	// public BufferedImage getBufferedImage(BufferedImage src) {
	// synchronized (tempBuffer) {
	// PinchFilter.setWithIndex(src, tempBuffer);
	// }
	// return tempBuffer;
	// }

	public void addForce(Vec2D force) {
		throw new UnsupportedOperationException();
	}

	public void setBulletFactory(BulletFactory bulletFactory) {
		throw new UnsupportedOperationException();
	}

	public List<Shootable> attack() {
		throw new UnsupportedOperationException();
	}

	public void setShapes(int[][] images) {
		throw new UnsupportedOperationException();
	}

	public void setShapes(List<int[][]> images) {
		throw new UnsupportedOperationException();
	}

	public void setStage(Rectangle2D stage) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void kill() {
		// TODO Auto-generated method stub

	}

}

class BlackHoleAccessor implements TweenAccessor<BlackHole> {

	@Override
	public int getValues(BlackHole target, int type, float[] returnValue) {
		returnValue[0] = target.getT();
		return 1;
	}

	@Override
	public void setValues(BlackHole target, int type, float[] returnValue) {
		target.setT(returnValue[0]);
	}

}