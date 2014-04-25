package main.game.graphics.effects;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import main.game.event.TickEvent;
import main.game.event.TickEventListener;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

public class CoolDownEffector implements TickEventListener, TweenCallback {
	static {
		Tween.registerAccessor(CoolDownEffector.class, new CoolDownEffectorAccessor());
	}

	private BufferedImage bufferedImage;
	private Graphics2D graphics2D;
	private int size;
	private float t = 1;
	private float duration = 1;
	//
	private float half;
	private float[] edges;
	private GeneralPath path;
	private Color color = new Color(0x88ffffff, true);
	private boolean visible = true;
	private boolean isAnimating = false;

	public CoolDownEffector(int size, float duration, Color color) {
		this.size = size;
		this.duration = duration;
		if (color != null) this.color = color;
		half = size / 2f;

		bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		graphics2D = (Graphics2D) bufferedImage.getGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setBackground(new Color(0, true));
		path = new GeneralPath();
		edges = new float[] { size, 0, size, size, 0, size, 0, 0 };
		init();
	}

	@Override
	public void onTick(TickEvent tickEvent) {
		clear();
		updatePath();

		graphics2D.setColor(color);
		graphics2D.fill(path);
	}

	private void updatePath() {
		float rad = (float) (2 * Math.PI * t - Math.PI / 2);
		int count = 8;

		if (t < 1f / 8f) count = 0;
		else if (t < 7f / 8f) count = (1 + (int) ((t - 1f / 8f) * 4)) * 2;

		float tx = half + (float) (Math.cos(rad)) * half * 1.5f;
		float ty = half + (float) (Math.sin(rad)) * half * 1.5f;

		path.reset();

		path.moveTo(half, 0);
		path.lineTo(half, half);
		path.lineTo(tx, ty);

		for (int i = count; i < 8; i += 2) {
			path.lineTo(edges[i], edges[i + 1]);
		}

		path.closePath();
	}

	public boolean startEffect(TweenManager manager) { // tween!
		if (!isAnimating) {
			t = 0;
			isAnimating = true;
			Tween.to(this, 0, duration).target(1).ease(Linear.INOUT).setCallback(this).start(manager);
			return true;
		}
		return false;
	}

	public void init() {
		t = 1;
		clear();
	}

	public void clear() {
		graphics2D.clearRect(0, 0, size, size);
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public float getDuration() {
		return duration;
	}

	public float getInterpolateFactor() {
		return t;
	}

	public void setInterpolateFactor(float factor) {
		this.t = factor;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void onEvent(int type, BaseTween<?> source) {
		isAnimating = false;
	}

	public boolean isAnimating() {
		return isAnimating;
	}
}

class CoolDownEffectorAccessor implements TweenAccessor<CoolDownEffector> {
	@Override
	public int getValues(CoolDownEffector target, int tweenType, float[] returnValues) {
		returnValues[0] = target.getInterpolateFactor();
		return 1;
	}

	@Override
	public void setValues(CoolDownEffector target, int tweenType, float[] newValues) {
		target.setInterpolateFactor(newValues[0]);
	}
}
