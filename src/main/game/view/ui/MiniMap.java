package main.game.view.ui;

import main.flask.utils.Vec2DF;
import main.flask.utils.Vec3D;
import main.game.event.TickEvent;
import main.game.graphics.Sprite;
import main.game.graphics.effects.Layer3D;
import main.game.view.View;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public class MiniMap extends View {
	private Vec2DF center = new Vec2DF(200, 200);
	private float[] zIndex = new float[]{-200, 200, 100, 0};
	private float factor = 100;
	//
	private Sprite board;
	private ArrayList<Sprite> spriteList;

	public MiniMap(int width, int height) {
		super(width, height);

		float cx = width / 2;
		float cy = height / 2;

		center.setValues(cx, cy);
		factor = width;
		zIndex[0] = -height;
		zIndex[1] = height;

		Layer3D layer = new Layer3D(
				new Vec3D[]{
						new Vec3D(-cx, -cy, 0),
						new Vec3D(cx, -cy, 0),
						new Vec3D(cx, cy, 0),
						new Vec3D(-cx, cy, 0),
				},
				center, factor, zIndex
		);

		board = new Sprite.Builder(layer).fillColor(0x44ccff00).setDrawOpt(false, true).build();
		spriteList = new ArrayList<>();
	}

	@Override
	public void onTick(TickEvent tickEvent) {
		clear();

		drawSprite(board);
		for (Sprite s : spriteList) {
			drawSprite(s);
		}
	}

	private void drawSprite(Sprite s) {
		Path2D path = s.layer.getProjectedPath();

		if (s.isFill) {
			graphics2D.setColor(s.fillColor);
			graphics2D.fill(path);
		}
		if (s.isStroke) {
			graphics2D.setColor(s.strokeColor);
			graphics2D.draw(path);
		}
	}

	public void addSprite(Sprite sprite) {
		spriteList.add(sprite);
	}

	public void toFront(Sprite sprite) {
		if (spriteList.contains(sprite)) {
			spriteList.remove(sprite);
			spriteList.add(sprite);
		}
	}

	public void toBackward(Sprite sprite) {
		if (spriteList.contains(sprite)) {
			spriteList.remove(sprite);
			spriteList.add(0, sprite);
		}
	}

	public void swapIndex(int a, int b) {
		int size = spriteList.size();

		if (0 <= a && a < size && 0 <= b && b < size) {
			if (a > b) {
				a ^= b;
				b ^= a;
				a ^= b;
			}
			Sprite B = spriteList.remove(b);
			Sprite A = spriteList.remove(a);
			spriteList.add(a, B);
			spriteList.add(b, A);
		}
	}

	public Sprite getSpriteAt(int index) {
		return spriteList.get(index);
	}

	public int size() {
		return spriteList.size();
	}
}
