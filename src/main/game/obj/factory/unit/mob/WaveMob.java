package main.game.obj.factory.unit.mob;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import main.flask.utils.Vec2D;
import main.game.event.BulletGeneratedListener;
import main.game.obj.behavior.AttackPattern;
import main.game.obj.behavior.MovingPattern;
import main.game.obj.behavior.MovingPattern.SIDE;
import main.game.obj.behavior.Pattern;
import main.game.obj.factory.unit.Unit;
import main.game.resource.ImageAsset;
import main.game.resource.ImageAsset.IMAGE_ID;

public class WaveMob extends Mob {

	private static final long serialVersionUID = -1525413284249799029L;
	protected static final Color TRANSPARENT = new Color(0, true);
	protected static BufferedImage bufferImage;

	public WaveMob(SIDE side, BulletGeneratedListener bulletResolver) {
		super(new Vec2D());

		setShapes(ImageAsset.image2Int(IMAGE_ID.ENEMY10));
		setColor(ImageAsset.makeColorArray(shape_normal));
		makeBufferImage();

		setHP(100);
		setRewardPrice(100);
		setMovingPattern(new WaveMovingPatternA(side));
		setAttackPattern(new WaveAttackPatternA(this));
		setShootableResolver(bulletResolver);

		this.speed = 2f;
		this.position.setXY(movingPattern.getFirstRelly());
		if (attackPattern != null) this.attackPattern.start();
	}

	public void setShootableResolver(BulletGeneratedListener mobBulletResolver) {
		attackPattern.setBulletGeneratedListener(mobBulletResolver);
	}

	protected void makeBufferImage() {
		bufferImage = new BufferedImage(boxSize * shape_normal[0].length, boxSize * shape_normal.length, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bufferImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setBackground(TRANSPARENT);

		for (int y = 0; y < shape_normal.length; y++) {
			for (int x = 0; x < shape_normal[0].length; x++) {
				if (shape_normal[y][x] != 0) {
					g.setColor(colors_normal[y][x]);
					g.fillRect(boxSize * x, boxSize * y, boxSize, boxSize);
				}
			}
		}
	}

	@Override
	public boolean draw(Graphics2D g) {
		int baseX = (int) position.x - boxSize * shape_normal[0].length / 2;
		int baseY = (int) position.y - boxSize * shape_normal.length / 2;
		g.drawImage(bufferImage, baseX, baseY, null);
		drawTest(g);
		return true;
	}

	public static class WaveMovingPatternA extends MovingPattern {
		private static final long serialVersionUID = -2561305090275436316L;

		public static int TERM = 125;

		public WaveMovingPatternA(SIDE side) {
			this.loop = false;

			int sign = side.getSign();
			double x = CENTER + TERM * sign;

			this.points = new Vec2D[] {//
			new Vec2D(x, -20), //
					new Vec2D(x, 400), //
					new Vec2D(x - 50 * sign, 420), //
					new Vec2D(x - 50 * sign, 100), //
			};
		}
	}

	public static class WaveAttackPatternA extends AttackPattern {
		private static final long serialVersionUID = 1986483163286801167L;

		public WaveAttackPatternA(Unit unit) {
			super(unit, Arrays.asList(new Pattern()), true);
		}
	}

}
