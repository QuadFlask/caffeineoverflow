package main.game.graphics.effects;

import main.flask.utils.Vec2DF;
import main.flask.utils.Vec3D;

import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class Layer3D {
	private Vec2DF position;
	private Vec3D[] points;
	private Vec3D[] pointsForPath;
	private int length;
	private Path2D path;
	private float factor = 250;
	private Vec2DF center;
	private float[] zIndex;

	public Layer3D(Vec3D[] points, Vec2DF center, float factor, float[] zIndex) {
		this.points = points;
		this.center = center;
		this.factor = factor;
		this.zIndex = zIndex;

		length = points.length;
		path = new GeneralPath();

		position = new Vec2DF();
		pointsForPath = new Vec3D[length];
		for (int i = 0; i < length; i++)
			pointsForPath[i] = new Vec3D();
		moveTo(0, 0);
	}

	public void moveTo(Vec2DF v) {
		moveTo(v.x, v.y);
	}

	public void moveTo(float x, float y) {
		position.setValues(x, y);
		updatePosition();
	}

	private void updatePosition() {
		for (int i = 0; i < length; i++) {
			pointsForPath[i].x = points[i].x + position.x;
			pointsForPath[i].y = points[i].y + position.y;
			pointsForPath[i].z = getRatio(pointsForPath[i].y);
		}
	}

	/**
	 * float realRange = zIndex[0] - zIndex[1]; // -400
	 * float zRange = zIndex[2] - zIndex[3]; // 100
	 * float ratio = (y - zIndex[0]) / realRange;
	 * y = zIndex[2] + zRange * ratio;
	 */
	private double getRatio(double y) {
		double ratio = (y - zIndex[0]) / (zIndex[0] - zIndex[1]);
		return zIndex[2] + (zIndex[2] - zIndex[3]) * ratio;
	}

	private void projection() {
		Vec2DF v = new Vec2DF();

		path.reset();

		pointsForPath[0].getPerspectiveProjectionIn2DF(v, factor, center);
		path.moveTo(v.x, v.y);

		for (int i = 1; i < length; i++) {
			pointsForPath[i].getPerspectiveProjectionIn2DF(v, factor, center);
			path.lineTo(v.x, v.y);
		}

		path.closePath();
	}

	public Path2D getProjectedPath() {
		projection();
		return path;
	}
}
