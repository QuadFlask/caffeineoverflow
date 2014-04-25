package main.game.obj.factory.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import main.flask.utils.Vec2D;
import main.game.graphics.effect.factory.ParticleEmitter;
import main.game.obj.property.Controllerable;
import main.game.resource.ImageAsset;

public class Hero extends Unit implements Controllerable {
	private static final long serialVersionUID = -4247766270977101763L;

	private float[] speeds = new float[] { 1.5f, 0.5f };
	private int speed_cur = 0;
	private int[][] shape_right;
	private int[][] shape_left;

	private Color[][] colors_left, colors_right;
	private int powerMin = 32;
	private int powerMax = 32;

	public Hero(Vec2D postion) {
		super(postion);
		setHP(1000);
	}

	@Override
	public void setShapes(List<int[][]> images) {
		setShapes(images.get(0));
		this.shape_right = images.get(1);
		this.shape_left = images.get(2);

		setColor(ImageAsset.makeColorArray(this.shape_normal));
		colors_left = ImageAsset.makeColorArray(this.shape_left);
		colors_right = ImageAsset.makeColorArray(this.shape_right);

		this.unitSizeRange = 15;
		this.unitSizeRange2 = this.unitSizeRange * this.unitSizeRange;
	}

	private Vec2D v = new Vec2D(0, 0);

	@Override
	public void move(float tx, float ty) {
		v.setValues(tx, ty);
		v.setMagnitude(speeds[speed_cur]);
		velocity.add(v);
	}

	@Override
	public boolean draw(Graphics2D g) {
		int[][] shape = getShape();
		Color[][] colors = getColors();
		int baseX = (int) position.x - boxSize * shape[0].length / 2;
		int baseY = (int) position.y - boxSize * shape.length / 2;

		for (int y = 0; y < shape.length; y++) {
			for (int x = 0; x < shape[0].length; x++) {
				if (shape[y][x] != 0) {
					g.setColor(colors[y][x]);
					g.fillRect(baseX + boxSize * x, baseY + boxSize * y, boxSize, boxSize);
				}
			}
		}

		calcFirePower();
		ParticleEmitter.makeFlame((float) position.x, (float) position.y + 16, powerMin / 4, 0.2f, powerMin, powerMax);
		drawRange(g);
		return true;
	}

	private void calcFirePower() {
		if (velocity.magnitude() > 1) {
			powerMin = 64;
			powerMax = 96;
		} else {
			powerMin = 16;
			powerMax = 64;
		}
	}

	private int shape_side = 0;

	private int[][] getShape() {
		if (Math.abs(velocity.x) > 0.1) {
			if (velocity.x > 0) {
				shape_side = 1;
				return shape_right;
			}
			shape_side = 2;
			return shape_left;
		}
		shape_side = 0;
		return shape_normal;
	}

	private Color[][] getColors() {
		if (shape_side == 0) return colors_normal;
		else if (shape_side == 1) return colors_right;
		return colors_left;
	}

	public void lowSpeed() {
		speed_cur = 1;
	}

	public void restoreSpeed() {
		speed_cur = 0;
	}

	@Override
	protected void kill() {

	}
}
