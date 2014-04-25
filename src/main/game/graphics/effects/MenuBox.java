package main.game.graphics.effects;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Cubic;
import main.flask.utils.Vec2DF;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class MenuBox {

	private String name;
	private Vec2DF[] originPoints;
	private Vec2DF[] targetPoints;
	private Vec2DF[] currentPoints;
	private GeneralPath path;
	private Color backgroundColor;
	//
	private TweenEquation tweenEquation = Cubic.OUT;
	private float duration = 1.0f;
	private float delayJitter = duration / 16f;

	public MenuBox(String name, Color backgroundColor, Rectangle2D originRect, Rectangle2D targetRect) {
		this.name = name;
		this.backgroundColor = backgroundColor;

		originPoints = new Vec2DF[]{
				new Vec2DF(originRect.getX(), originRect.getY()),
				new Vec2DF(originRect.getX() + originRect.getWidth(), originRect.getY()),
				new Vec2DF(originRect.getX() + originRect.getWidth(), originRect.getY() + originRect.getHeight()),
				new Vec2DF(originRect.getX(), originRect.getY() + originRect.getHeight()),
		};
		targetPoints = new Vec2DF[]{
				new Vec2DF(targetRect.getX(), targetRect.getY()),
				new Vec2DF(targetRect.getX() + targetRect.getWidth(), targetRect.getY()),
				new Vec2DF(targetRect.getX() + targetRect.getWidth(), targetRect.getY() + targetRect.getHeight()),
				new Vec2DF(targetRect.getX(), targetRect.getY() + targetRect.getHeight()),
		};
		currentPoints = new Vec2DF[]{
				originPoints[0].clone(),
				originPoints[1].clone(),
				originPoints[2].clone(),
				originPoints[3].clone()};

		path = new GeneralPath();
	}

	public void setAnimationProperty(TweenEquation tweenEquation, float delayJitter, float duration) {
		if (tweenEquation != null)
			this.tweenEquation = tweenEquation;
		if (delayJitter >= 0)
			this.delayJitter = delayJitter;
		if (duration >= 0)
			this.duration = duration;
	}

	// Tween.registerAccessor(Particle.class, new ParticleAccessor());
	// manager.update(ellapsedSeconds);
	public Path2D getPath() {
		path.reset();
		path.moveTo(currentPoints[0].x, currentPoints[0].y);
		path.lineTo(currentPoints[1].x, currentPoints[1].y);
		path.lineTo(currentPoints[2].x, currentPoints[2].y);
		path.lineTo(currentPoints[3].x, currentPoints[3].y);
		path.closePath();
		return path;
	}

	public String getName() {
		return name;
	}

	public Vec2DF[] getPosition() {
		return currentPoints;
	}

	public void minimize(TweenManager manager, TweenCallback callback) {
		Timeline timeline = Timeline.createParallel().beginParallel();

		for (int i = 0; i < 4; i++) {
			timeline.push(
					Tween.to(currentPoints[i], i, duration)
							.target(originPoints[i].x, originPoints[i].y)
							.ease(tweenEquation)
							.delay(delayJitter * i)
			);
		}

		timeline.setCallback(callback);
		timeline.end().start(manager);
	}

	public void maximize(TweenManager manager, TweenCallback callback) {
		Timeline timeline = Timeline.createParallel().beginParallel();

		for (int i = 0; i < 4; i++) {
			timeline.push(
					Tween.to(currentPoints[i], i, duration)
							.target(targetPoints[i].x, targetPoints[i].y)
							.ease(tweenEquation)
							.delay(delayJitter * i)
			);
		}

		timeline.setCallback(callback);
		timeline.end().start(manager);
	}

	public Color getColor() {
		return backgroundColor;
	}
}

