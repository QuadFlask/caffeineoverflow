package main.game.graphics.particles_deprecated;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import main.flask.utils.FlaskUtil;
import main.flask.utils.Vec2D;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;

public class TweenParticle implements TweenAccessor<TweenParticle> {
	public static final int POSITION = 0;
	public static final int SIZE = 1;
	public static final int LIFE_TIME = 2;
	public static final int ALL = 3;

	public Vec2D position;
	public Color[] colors;
	public Shape s;
	public TweenEquation tweenEquation = Cubic.OUT;

	public float life = 0;
	public float duration = 1, delay = 0;
	public double theta = 0, length = 0;
	public float width, height;

	public TweenParticle(Vec2D from, float theta, float length, float duration, float width, float height, Color[] colors, Shape s) {
		this.position = from.clone();

		this.theta = theta;
		this.duration = duration;
		this.length = length;

		this.width = width;
		this.height = height;

		this.colors = colors;
		this.s = s;
	}

	public Tween getTween() {
		Vec2D d = new Vec2D(length);
		d.rotate(theta);
		d.add(position);

		Tween.setCombinedAttributesLimit(5);
		return Tween.to(this, ALL, this.duration) //
				.target((float)d.x,(float) d.y, 0, 0, 0)
				.ease(tweenEquation) //
				.delay(delay);
	}

	public void draw(Graphics2D g) {
		g.setColor(FlaskUtil.colorInterpolate(colors, life));
		g.translate(position.x, position.y);
		g.rotate(theta + Math.PI / 2);

		g.fill(s);

		g.rotate(-(theta + Math.PI / 2));
		g.translate(-position.x, -position.y);
	}

	public void start(TweenManager manager) {
		getTween().start(manager);
	}

	@Override
	public int getValues(TweenParticle target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ALL: {
			returnValues[0] = (float) position.x;
			returnValues[1] = (float) position.y;
			returnValues[2] = width;
			returnValues[3] = height;
			returnValues[4] = life;
			return 5;
		}
		case POSITION: {
			returnValues[0] = (float) position.x;
			returnValues[1] = (float) position.y;
			return 2;
		}
		case SIZE: {
			returnValues[0] = width;
			returnValues[1] = height;
			return 2;
		}
		case LIFE_TIME: {
			returnValues[0] = life;
			return 1;
		}
		default: {
			return 0;
		}
		}
	}

	@Override
	public void setValues(TweenParticle target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ALL: {
			position.setValues(returnValues[0], returnValues[1]);
			width = returnValues[2];
			height = returnValues[3];
			life = returnValues[4];
			return;
		}
		case POSITION: {
			position.setValues(returnValues[0], returnValues[1]);
			return;
		}
		case SIZE: {
			width = returnValues[0];
			height = returnValues[1];
			return;
		}
		case LIFE_TIME: {
			life = returnValues[0];
			return;
		}
		}
	}

}
