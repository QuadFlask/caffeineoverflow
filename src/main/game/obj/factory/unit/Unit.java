package main.game.obj.factory.unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;

import main.flask.utils.Vec2D;
import main.game.common.D;
import main.game.obj.factory.BulletFactory;
import main.game.obj.property.Shootable;

public abstract class Unit implements Serializable {
	private static final long serialVersionUID = 7423490765578037652L;

	public int boxSize = D.DEFAULT_BOX_SIZE;

	protected Vec2D position;
	protected Vec2D velocity = new Vec2D();
	protected Vec2D accel = new Vec2D();

	protected float firction = 0.750f;

	protected int[][] shape_normal;
	protected Color[][] colors_normal;

	protected Rectangle2D stage;
	protected BulletFactory bulletFactory;

	protected double unitSizeRange = 20;
	protected double unitSizeRange2 = unitSizeRange * unitSizeRange;

	protected int hpMax = 100;
	protected int hp = 100;
	protected int rewardPrice;

	public Unit(Vec2D pos) {
		position = pos;
	}

	public abstract boolean draw(Graphics2D g);

	protected void drawIntArray(Graphics2D g, int[][] shape) {
		int baseX = (int) position.x - boxSize * shape[0].length / 2;
		int baseY = (int) position.y - boxSize * shape.length / 2;

		for (int y = 0; y < shape.length; y++) {
			for (int x = 0; x < shape[0].length; x++) {
				if (shape[y][x] != 0) {
					g.setColor(new Color(shape[y][x], true));
					g.fillRect(baseX + boxSize * x, baseY + boxSize * y, boxSize, boxSize);
				}
			}
		}
	}

	protected void drawIntArray(Graphics2D g, int[][] shape, Color color) {
		int baseX = (int) position.x - boxSize * shape[0].length / 2;
		int baseY = (int) position.y - boxSize * shape.length / 2;

		g.setColor(color);
		for (int y = 0; y < shape.length; y++) {
			for (int x = 0; x < shape[0].length; x++) {
				if (shape[y][x] != 0) {
					g.fillRect(baseX + boxSize * x, baseY + boxSize * y, boxSize, boxSize);
				}
			}
		}
	}

	protected void drawIntArray(Graphics2D g, int[][] shape, Color[][] colors) {
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
	}

	private int drawTestSize = 40;

	public void drawTest(Graphics2D g) {
		drawRange(g);
		drawHP(g);
	}

	protected void drawRange(Graphics2D g) {
		g.setColor(Color.orange);
		g.drawOval((int) position.x - drawTestSize / 2, (int) position.y - drawTestSize / 2, drawTestSize, drawTestSize);
		g.setColor(Color.red);
		g.drawOval((int) (position.x - unitSizeRange / 2), (int) (position.y - unitSizeRange / 2), (int) unitSizeRange, (int) unitSizeRange);
	}

	protected final Color HP_BAR_BACKGROUND_COLOR = new Color(0x88000000, true);
	protected final Color HP_BAR_COLOR = new Color(0xaaccff00, true);

	public void drawHP(Graphics2D g) { // call in view. because it bluring...
		g.setColor(HP_BAR_BACKGROUND_COLOR);
		g.fillRoundRect((int) position.x - drawTestSize / 2, (int) position.y - drawTestSize / 2 - 10, drawTestSize, 4, 4, 4);
		g.setColor(HP_BAR_COLOR);
		g.fillRoundRect((int) position.x - drawTestSize / 2, (int) position.y - drawTestSize / 2 - 10, (int) (drawTestSize * hp / hpMax), 4, 4, 4);
	}

	public void updatePosition(float duration) {
		velocity.add(accel);
		position.add(velocity.getMultiply(duration));
		if (velocity.magnitude() < 0.2) velocity.clear();
		else velocity.multiply(firction);
		accel.clear();

		if (position.x > stage.getMaxX()) {
			position.x = stage.getMaxX();
			velocity.x = 0;
		}
		if (position.x < stage.getMinX()) {
			position.x = stage.getMinX();
			velocity.x = 0;
		}
		if (position.y > stage.getMaxY()) {
			position.y = stage.getMaxY();
			velocity.y = 0;
		}
		if (position.y < stage.getMinY()) {
			position.y = stage.getMinY();
			velocity.y = 0;
		}
	}

	public void addForce(Vec2D force) {
		accel.add(force);
	}

	public void setBulletFactory(BulletFactory bulletFactory) {
		this.bulletFactory = bulletFactory;
	}

	public List<Shootable> attack() {
		return bulletFactory.create(position.getSub(1, boxSize * shape_normal.length * 0.75));
	}

	public void setShapes(int[][] shape) {
		this.shape_normal = shape;
		this.unitSizeRange = (shape.length + shape[0].length) / 2 * boxSize + 1;
		this.unitSizeRange2 = this.unitSizeRange * this.unitSizeRange;
	}

	public void setColor(Color[][] color) {
		this.colors_normal = color;
	}

	public void setShapes(List<int[][]> shapes) {
		setShapes(shapes.get(0));
	}

	public void setStage(Rectangle2D stage) {
		this.stage = stage;
	}

	public double getX() {
		return position.x;
	}

	public double getY() {
		return position.y;
	}

	public double getUnitSizeRange() {
		return unitSizeRange;
	}

	public double getUnitSizeRange2() {
		return unitSizeRange2;
	}

	public Vec2D getPosition() {
		return position;
	}

	public int getHP() {
		return hp;
	}

	public void setHP(int hp) {
		this.hp = hp;
		this.hpMax = hp;
	}

	public void damaging(int damage) {
		hp -= damage;
		if (isDead()) {
			hp = 0;
			kill();
		}
	}

	public boolean isDead() {
		return hp <= 0;
	}

	protected abstract void kill();

	public void setRewardPrice(int price) {
		this.rewardPrice = price;
	}

	public int getRewardPrice() {
		return rewardPrice;
	}
}
