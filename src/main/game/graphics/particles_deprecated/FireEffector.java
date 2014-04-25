package main.game.graphics.particles_deprecated;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.flask.utils.FlaskUtil;
import main.flask.utils.Vec2D;
import aurelienribon.tweenengine.TweenManager;

public class FireEffector {

	private static List<TweenParticle> particles = new ArrayList<TweenParticle>();

	private FireEffector() {
	}

	private static float width = 3;
	private static Rectangle2D rect = new Rectangle2D.Float(0, 0, width, width);

	public static void makeEffect(Vec2D from, int count, float theta, float length, float duration, TweenManager manager) {
		TweenParticle p;
		for (int i = 0; i < count; i++) {
			p = new TweenParticle(from, theta, length, duration, width, width, FlaskUtil.COLOR_FIRE, rect);
			p.start(manager);
			particles.add(p);
		}
	}

	public static synchronized void draw(Graphics2D g) {
		Iterator<TweenParticle> i = new ArrayList<TweenParticle>(particles).iterator();
		while (i.hasNext()) {
			i.next().draw(g);
		}
	}

}
