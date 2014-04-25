package main.game.graphics.particles_deprecated;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Queue;

import main.flask.utils.FixedQueue;
import main.flask.utils.FlaskUtil;
import main.flask.utils.Vec2D;

public class FiberParticle extends Particle {
	public Color[] colors;
	private FixedQueue<Double> posX;
	private FixedQueue<Double> posY;

	private Line2D.Double[] fibers;
	private Stroke stroke[];

	public int fiberLength = 1;
	public float thicknessMax = 4;
	public float thicknessMin = 0;

	public FiberParticle(Vec2D pos, int fiberLength, Color[] colors) {
		super(pos);
		this.fiberLength = fiberLength;
		this.colors = colors;

		posX = new FixedQueue<Double>(fiberLength);
		posY = new FixedQueue<Double>(fiberLength);

		fibers = new Line2D.Double[fiberLength];
		stroke = new BasicStroke[fiberLength];

		posX.initializeWith(pos.x);
		posY.initializeWith(pos.y);
		for (int i = 0; i < fiberLength; i++) {
			fibers[i] = new Line2D.Double();
			stroke[i] = new BasicStroke(FlaskUtil.linearInterpolate(thicknessMax, thicknessMin, (float) i / (fiberLength - 1)), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}
	}

	public FiberParticle(Vec2D pos, int fiberLength, Color startColor, Color endColor) {
		this(pos, fiberLength, new Color[fiberLength]);
		for (int i = 0; i < fiberLength; i++) {
			colors[i] = FlaskUtil.colorInterpolate(startColor, endColor, (float) i / (fiberLength - 1));
		}
	}

	private int k = 0;

	@Override
	public boolean integrate() {
		posX.enQueue(position.x);
		posY.enQueue(position.y);
		k += fiberLength - 1;

		for (int i = 1; i < fiberLength; i++) {
			fibers[i].x1 = posX.get((k + i - 1) % fiberLength);
			fibers[i].y1 = posY.get((k + i - 1) % fiberLength);

			fibers[i].x2 = posX.get((k + i) % fiberLength);
			fibers[i].y2 = posY.get((k + i) % fiberLength);
		}

		fibers[0].x2 = (float) position.x;
		fibers[0].y2 = (float) position.y;

		boolean result = super.integrate();

		fibers[0].x1 = (float) position.x;
		fibers[0].y1 = (float) position.y;

		return result;
	}

	@Override
	public boolean integrate(double duration) {
		posX.enQueue(position.x);
		posY.enQueue(position.y);
		k += fiberLength - 1;

		for (int i = 1; i < fiberLength; i++) {
			fibers[i].x1 = posX.get((k + i - 1) % fiberLength);
			fibers[i].y1 = posY.get((k + i - 1) % fiberLength);

			fibers[i].x2 = posX.get((k + i) % fiberLength);
			fibers[i].y2 = posY.get((k + i) % fiberLength);
		}

		fibers[0].x2 = (float) position.x;
		fibers[0].y2 = (float) position.y;

		boolean result = super.integrate(duration);

		fibers[0].x1 = (float) position.x;
		fibers[0].y1 = (float) position.y;
		return result;
	}

	@Override
	public void draw(Graphics2D g) {
		for (int i = 0; i < fiberLength; i++) {
			g.setColor(colors[i]);
			g.setStroke(stroke[i]);
			g.draw(fibers[i]);
		}
	}
}
