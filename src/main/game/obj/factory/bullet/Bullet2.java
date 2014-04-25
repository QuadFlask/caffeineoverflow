package main.game.obj.factory.bullet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import main.flask.utils.Vec2D;
import main.game.common.D;
import main.game.graphics.effect.factory.Jitter;
import main.game.graphics.effect.factory.ParticleEmitter;
import main.game.obj.factory.unit.Unit;
import main.game.obj.property.Shootable;
import main.game.resource.ImageAsset;
import main.game.resource.ImageAsset.IMAGE_ID;

public class Bullet2 implements Shootable {
	public Vec2D position;
	protected Vec2D velocity;
	protected float angle = 0;
	protected boolean homming = false;
	protected Unit target;
	protected static int[][] shape = ImageAsset.image2Int(IMAGE_ID.BULLET3);
	protected static Color[][] colors = ImageAsset.makeColorArray(shape);
	protected static int boxSize = D.DEFAULT_BOX_SIZE;
	protected static BufferedImage img = new BufferedImage(boxSize * shape[0].length, boxSize * shape.length, BufferedImage.TYPE_4BYTE_ABGR);
	protected boolean isConsumed;
	
	static {
		Graphics2D g = (Graphics2D) img.getGraphics();
		for (int y = 0; y < shape.length; y++) {
			for (int x = 0; x < shape[0].length; x++) {
				if (shape[y][x] != 0) {
					g.setColor(colors[y][x]);
					g.fillRect(boxSize * x, boxSize * y, boxSize, boxSize);
				}
			}
		}
	}
	
	public Bullet2() {
		position = new Vec2D();
		velocity = new Vec2D();
	}
	
	public void setPosition(Vec2D pos) {
		position.setXY(pos);
	}
	
	public void setVelocity(Vec2D v) {
		velocity.setXY(v);
		angle = (float) (velocity.rotation() + Math.PI / 2);
	}
	
	public void setShape(int[][] shape) {
		this.shape = shape;
	}
	
	@Override
	public boolean isHomming() {
		return homming;
	}
	
	@Override
	public boolean isSpread() {
		return false;
	}
	
	public void setTargets(List<Unit> units) {
		
	}
	
	public void addForce(Vec2D v) {
		velocity.add(v);
	}
	
	@Override
	public boolean update(float duration) {
		position.add(velocity.getMultiply(duration));
		angle = (float) (velocity.rotation() + Math.PI / 2);
		return !isConsumed;
	}
	
	@Override
	public boolean isConsumed() {
		return isConsumed;
	}
	
	@Override
	public void setConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}
	
	public void selectCloserTarget(List<Unit> units) {}
	
	public void draw(Graphics2D g) {
		float posx = (float) position.x;
		float posy = (float) position.y;
		
		g.translate(posx, posy);
		g.rotate(angle);
		
		g.drawImage(img, 0, 0, null);
		
		g.rotate(-angle);
		g.translate(-posx, -posy);
	}
	
	@Override
	public Vec2D getPosition() {
		return position;
	}
	
	@Override
	public Vec2D getVelocity() {
		return velocity;
	}
	
	@Override
	public boolean checkInStage(Rectangle2D stage) {
		if (position.x > stage.getMaxX() || position.x < stage.getMinX()) return false;
		if (position.y > stage.getMaxY() || position.y < stage.getMinY()) return false;
		return true;
	}
	
	@Override
	public void setTarget(Unit unit) {
		this.target = unit;
	}
	
	@Override
	public int getDamage() {
		return 4;
	}
	
	@Override
	public void setConsumed() {
		setConsumed(true);
		ParticleEmitter.makeCircle((float) position.x, (float) position.y, Jitter.fromto(20, 60), 1);
		ParticleEmitter.makeExplosion((float) position.x, (float) position.y, 4, 1, 16);
	}
	
	@Override
	public List<Shootable> activateFunction() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Bullet2 [position=" + position + ", velocity=" + velocity + ", angle=" + angle + ", homming=" + homming + ", target=" + target + ", isConsumed=" + isConsumed + "]";
	}
}
