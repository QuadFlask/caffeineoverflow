package main.game.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.flask.imageprocessing.BlurFilter;
import main.flask.imageprocessing.IntegerBlender;
import main.game.event.TickEvent;
import main.game.graphics.effect.factory.ParticleEmitter;
import main.game.graphics.effects.Grid;
import main.game.graphics.effects.PerlinBackground;
import main.game.logic.GameLogic;
import main.game.logic.Logic;
import main.game.obj.factory.unit.Unit;
import main.game.obj.property.Shootable;
import main.game.view.ui.DrawableComponent;

public class GameView extends View {
	private static final Color CCBlack = new Color(0xcc000000, true);

	private ExecutorService threadPool = Executors.newFixedThreadPool(16);

	private BufferedImage upperBuffer, blurBuffer, blurBuffer_temp;
	private Graphics2D g_upper, g_blur, g_blur_temp;
	private BlurFilter blurFilter = new BlurFilter(4, 4, BlurFilter.TYPE_LINEAR);
	private Kernel kernel = blurFilter.getMatrixAsKernel(1.75f);
	private ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

	private PerlinBackground background;
	private Grid grid;
	private GameLogic gameLogic;

	private boolean isGridVisible = false;
	private int k = 4;
	private int pk = k * 2;

	public GameView(int width, int height) {
		super(width, height);

		upperBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);// BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		blurBuffer = new BufferedImage(width / k, height / k, BufferedImage.TYPE_INT_ARGB);// BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		blurBuffer_temp = new BufferedImage(width / k, height / k, BufferedImage.TYPE_INT_ARGB);// BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

		background = new PerlinBackground(width / pk, height / pk);
		background.setZoom(0.1);

		grid = new Grid(width, height, 5);
	}

	@Override
	public void init() {
		keyManager.reset();

		keyManager.addCallback(gameLogic.getKeyCallbacks());

		g_upper = (Graphics2D) upperBuffer.getGraphics();
		g_blur = (Graphics2D) blurBuffer.getGraphics();
		g_blur_temp = (Graphics2D) blurBuffer_temp.getGraphics();

		g_upper.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g_upper.setBackground(TRANSPARENT);
		g_blur.setBackground(TRANSPARENT);
		g_blur_temp.setBackground(TRANSPARENT);

		graphics2D.setBackground(Color.black);
	}

	@Override
	public void onTick(TickEvent tickEvent) {
		super.onTick(tickEvent);
		gameLogic.execute(tickEvent);
		ParticleEmitter.updateParticles(tickEvent.getElaspedTimeFromTick());

		threadPool.execute(background.getPerlinBackgroundMoving());

		drawing(tickEvent);

		// showFPS(tickEvent);
		showFPSandPING(tickEvent, gameLogic.getPing());
	}

	private void drawing(TickEvent tickEvent) {
		graphics2D.clearRect(0, 0, width, height);
		g_upper.clearRect(0, 0, width, height);

		// /

		drawGameContents();

		// /

		drawBackground();
		makeItBlury();

		// /

		if (isGridVisible) graphics2D.drawImage(grid.getBufferedImage(), 0, 0, width, height, null);
		graphics2D.drawImage(blurBuffer, 0, 0, width, height, null);
		graphics2D.drawImage(upperBuffer, 0, 0, width, height, null);

		// /

		drawEdgeFrame();

		drawDrawableComponents();
	}

	private void makeItBlury() {
		g_blur_temp.drawImage(upperBuffer, 0, 0, width / k, height / k, null);
		op.filter(blurBuffer_temp, blurBuffer);
		IntegerBlender.apply(blurBuffer_temp, 0.95f);
	}

	private void drawBackground() {
		g_blur_temp.drawImage(background.getFloting(), 0, 0, width / k, height / k, null);
	}

	private void drawGameContents() {
		drawBullets(); // 부하 심함

		ParticleEmitter.draw(g_upper); // 부하 심함.

		drawUnits();
		gameLogic.getHero().draw(g_upper);
	}

	private void drawDrawableComponents() {
		for (DrawableComponent d : drawables) {
			d.draw(graphics2D);
		}
	}

	private void drawEdgeFrame() {
		graphics2D.setColor(CCBlack);
		graphics2D.fillRect(0, 36, 12, height - 36 - 50);
		graphics2D.fillRect(width - 12, 36, 12, height - 36 - 50);
	}

	private void drawUnits() {
		List<Unit> unitList = new ArrayList<Unit>(gameLogic.getUnitList());
		synchronized (unitList) {
			for (Unit u : unitList)
				u.draw(g_upper);
		}
	}

	private void drawBullets() {
		List<Shootable> bulletList = gameLogic.getBulletList();
		synchronized (bulletList) {
			for (Shootable b : bulletList)
				b.draw(g_upper);
		}

		bulletList = gameLogic.getMobBulletList();
		synchronized (bulletList) {
			for (Shootable b : bulletList)
				b.draw(g_upper);
		}
	}

	// private void drawBullets() {
	// List<Shootable> bulletList = gameLogic.getBulletList();
	// synchronized (bulletList) {
	// for (Shootable b : bulletList)
	// b.draw(g_upper);
	// }
	//
	// bulletList = gameLogic.getMobBulletList();
	// synchronized (bulletList) {
	// for (Shootable b : bulletList)
	// b.draw(g_upper);
	// }
	// }

	public void setLogic(Logic logic) {
		this.gameLogic = (GameLogic) logic;
	}

	@Override
	public void free() {
		super.free();
		threadPool.shutdown();
	}
}

//

//
//
//
// TwirlFilter.filter(this.bufferedImage, twril, 200, 200, 50, 0.00001f);
// PinchFilter.filter(this.bufferedImage, twril, blackHoleX, blackHoleY,
// blackHoleRadius, 8.f, 0.8f);
// synchronized (twril) {
// PinchFilter.setWithIndex(this.bufferedImage, twril);
// }

//
//
//
//
//
// FiberParticle p = new FiberParticle(new Vec2D(250, 500), 16, Color.red,
// Color.white);
// p.addForce(new Vec2D(Math.random() - 0.5, Math.random() -
// 0.5).getMultiply(15));
// pList.add(p);
// // p.damping = 0.5f;
//
// for (FiberParticle fp : pList) {
// fp.draw(graphics2D);
// fp.integrate();
// // fp.addForce(new Vec2D(Math.random() - 0.5, Math.random() - 0.5));
// }