package main.game.graphics.particles_deprecated;

import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import main.flask.utils.Vec2D;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class TweenParticleEffector {
	
	private static List<TweenParticle> particles = new ArrayList<TweenParticle>();
	private static TweenManager manager = new TweenManager();
	
	private TweenParticleEffector() {}
	
	public static void makeEffect(Vec2D pos, int count, float theta, float length, float duration, float width, float height, Color[] colors, Shape s) {
		Timeline timeline = Timeline.createParallel().beginParallel();
		ArrayList<TweenParticle> particleList = new ArrayList<TweenParticle>();
		TweenParticle particle;
		
		for (int i = 0; i < count; i++) {
			particle = new TweenParticle(pos, theta, length, duration, width, height, colors, s);
			
			particleList.add(particle);
			timeline.push(particle.getTween());
		}
		
		timeline.setCallback(new Remover(particleList));
		timeline.end().start(manager);
	}
	
	static class Remover implements TweenCallback {
		List<TweenParticle> p;
		
		public Remover(List<TweenParticle> list) {
			p = list;
		}
		
		@Override
		public void onEvent(int tweenType, BaseTween<?> tween) {
			particles.remove(p);
			p.clear();
		}
	}
	
	public static void update(float duration) {
		manager.update(duration);
	}
	
}
