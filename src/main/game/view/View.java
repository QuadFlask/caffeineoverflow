package main.game.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.flask.utils.Vec2DF;
import main.game.common.D;
import main.game.event.TickEvent;
import main.game.event.TickEventListener;
import main.game.view.ui.DrawableComponent;
import main.game.windows.KeyManager;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

public class View implements TickEventListener {
	static {
		Tween.registerAccessor(Vec2DF.class, new Vec2DFAccessor());
	}

	protected static final Color TRANSPARENT = new Color(0, true);
	protected static TweenManager tweenManager = new TweenManager();

	protected BufferedImage bufferedImage;
	protected Graphics2D graphics2D;
	protected boolean visible = true;
	protected float x = 0, y = 0;
	protected int width, height;
	protected ArrayList<View> viewList;
	protected List<DrawableComponent> drawables = new ArrayList<DrawableComponent>();

	protected KeyManager keyManager;

	public View(int width, int height) {
		this.width = width;
		this.height = height;

		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphics2D = (Graphics2D) bufferedImage.getGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (D.GRAPHIC_HIGH) graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		// RenderingHints.VALUE_INTERPOLATION_BILINEAR
		// RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
		// RenderingHints.VALUE_INTERPOLATION_BICUBIC;
		graphics2D.setBackground(TRANSPARENT);

		graphics2D.setColor(D.FONT_DEBUG);
		graphics2D.setFont(D.FONT_CONSOLAS);

		viewList = new ArrayList<>();

		clear();
	}

	protected static void updateTween(float elaspedTimeInSeconds) {
		tweenManager.update(elaspedTimeInSeconds);
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void onTick(TickEvent tickEvent) {
		clear();
		if (visible) {
			for (View v : viewList) {
				v.clear();
				v.onTick(tickEvent);
			}
			updateTween(tickEvent.getElaspedTimeFromTick());
		}
	}

	public void clear() {
		// ((Graphics2D)graphics2D).setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,
		// 0));
		graphics2D.clearRect(0, 0, width, height);
	}

	protected void showFPS(TickEvent tickEvent) {
		graphics2D.setColor(D.FONT_DEBUG);
		graphics2D.drawString("fps : " + tickEvent.getFPSFromTick(), width - 55, 15);
	}

	protected void showFPSandPING(TickEvent tickEvent, int ping) {
		showFPS(tickEvent);
		graphics2D.drawString(String.format("ping : %02dms", ping), width - 72, 25);
	}

	protected void addView(View view) {
		viewList.add(view);
	}

	public void setKeyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
	}

	public void init() {}

	public static TweenManager getTweenManager() {
		return tweenManager;
	}

	public void addDrawable(DrawableComponent d) {
		synchronized (drawables) {
			drawables.add(d);
		}
	}

	public void free() {
		// visible = false;
		drawables.clear();
		viewList.clear();
		tweenManager.killAll();
		// graphics2D = null;
		// bufferedImage = null;
		// tweenManager = null;
	}

	public void removeKeyManager() {}

}

class Vec2DFAccessor implements TweenAccessor<Vec2DF> {
	@Override
	public int getValues(Vec2DF target, int tweenType, float[] returnValues) {
		returnValues[0] = target.x;
		returnValues[1] = target.y;
		return 2;
	}

	@Override
	public void setValues(Vec2DF target, int tweenType, float[] newValues) {
		target.x = newValues[0];
		target.y = newValues[1];
	}
}
