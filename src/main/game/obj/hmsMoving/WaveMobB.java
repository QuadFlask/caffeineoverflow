package main.game.obj.hmsMoving;

import java.awt.Graphics2D;
import java.util.Arrays;

import main.flask.utils.Vec2D;
import main.game.event.BulletGeneratedListener;
import main.game.obj.behavior.AttackPattern;
import main.game.obj.behavior.MovingPattern;
import main.game.obj.behavior.MovingPattern.SIDE;
import main.game.obj.behavior.Pattern;
import main.game.obj.factory.unit.Unit;
import main.game.obj.factory.unit.mob.Mob;
import main.game.resource.ImageAsset;
import main.game.resource.ImageAsset.IMAGE_ID;

public class WaveMobB extends Mob {
	
	public WaveMobB(SIDE side, BulletGeneratedListener bulletResolver) {
		super(new Vec2D());
		
		setShapes(ImageAsset.image2Int(IMAGE_ID.ENEMY4));
		setColor(ImageAsset.makeColorArray(shape_normal));
		
		setHP(10);
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
	
	@Override
	public boolean draw(Graphics2D g) {
		int baseX = (int) position.x - boxSize * shape_normal[0].length / 2;
		int baseY = (int) position.y - boxSize * shape_normal.length / 2;
		
		for (int y = 0; y < shape_normal.length; y++) {
			for (int x = 0; x < shape_normal[0].length; x++) {
				if (shape_normal[y][x] != 0) {
					g.setColor(colors_normal[y][x]);
					g.fillRect(baseX + boxSize * x, baseY + boxSize * y, boxSize, boxSize);
				}
			}
		}
		
		drawTest(g);
		
		return true;
	}
	
	public static class WaveMovingPatternA extends MovingPattern {
		public static int TERM = 125;
		
		public WaveMovingPatternA(SIDE side) {
			this.loop = false;
			
			int sign = side.getSign();
			double x = CENTER + TERM * sign;
			System.out.println("x : " + x);
			this.points = new Vec2D[] {//
			new Vec2D(200, 200),//
					new Vec2D(50 + x, 400), //
					new Vec2D(x - 30, 280),//
					new Vec2D(200, 280),//
			};
		}
	}
	
	public static class WaveAttackPatternA extends AttackPattern {
		
		public WaveAttackPatternA(Unit unit) {
			super(unit, Arrays.asList(new Pattern()), true);
		}
		
	}
	
}
