package main.game.view.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.game.common.D;
import main.game.graphics.GraphicsDrawer;
import main.game.graphics.effects.CoolDownEffector;
import main.game.resource.FontAsset;
import main.game.resource.ImageAsset;
import main.game.resource.ImageAsset.IMAGE_ID;
import main.game.view.View;
import aurelienribon.tweenengine.TweenManager;

public class FactoryBar extends View implements DrawableComponent {
	private Color backgroundColor = new Color(0x88000000, true);
	private static int height = 50;
	private int count = 10;

	private CoolDownEffector[] coolDownEffectors;
	private BufferedImage[] iconList;
	private TweenManager tweenManager;
	private int[] mobCost, mobCoolDownTime;

	public FactoryBar(int width, TweenManager tweenManager) {
		super(width, height);
		this.tweenManager = tweenManager;

		iconList = new BufferedImage[count];
	}

	public void makeCoolDownEffects(int[] cost, int[] time) {
		setCosts(cost);
		setCoolDownTime(time);

		coolDownEffectors = new CoolDownEffector[count];
		for (int i = 1; i < coolDownEffectors.length; i++) {
			coolDownEffectors[i] = new CoolDownEffector(50, time[i - 1], new Color(0x88ffffff, true));
		}

		iconList[1] = ImageAsset.getImage(IMAGE_ID.WRENCH);
		iconList[2] = ImageAsset.getImage(IMAGE_ID.WRENCH);
		iconList[3] = ImageAsset.getImage(IMAGE_ID.WRENCH);
		iconList[4] = ImageAsset.getImage(IMAGE_ID.WRENCH);
		iconList[5] = ImageAsset.getImage(IMAGE_ID.WRENCH);
		iconList[6] = ImageAsset.getImage(IMAGE_ID.ROCK);
		iconList[7] = ImageAsset.getImage(IMAGE_ID.SHEILD);
		iconList[8] = ImageAsset.getImage(IMAGE_ID.WRENCH);
		iconList[9] = ImageAsset.getImage(IMAGE_ID.BLACKHOLE);
	}

	public void update() {
		graphics2D.clearRect(0, 0, width, height);
		graphics2D.setColor(backgroundColor);
		graphics2D.fillRect(0, 0, width, height);
		drawIcons();
	}

	public boolean startEffect(int i) {
		return coolDownEffectors[i].startEffect(tweenManager);
	}

	public boolean isAnimating(int i) {
		return coolDownEffectors[i + 1].isAnimating();
	}

	private void drawIcons() {
		for (int i = 1; i < iconList.length; i++) {
			graphics2D.drawImage(iconList[i], i * height, 0, height, height, null);
			coolDownEffectors[i].onTick(null);
			graphics2D.drawImage(coolDownEffectors[i].getBufferedImage(), i * height, 0, height, height, null);
		}
	}

	public int getHeight() {
		return height;
	}

	private int y = 0;

	@Override
	public boolean draw(Graphics2D g) {
		g.drawImage(bufferedImage, 0, y, width, height, null);

		drawCostAndCoolDownTime(g);

		return true;
	}

	private void drawCostAndCoolDownTime(Graphics2D g) {

		// g.setFont(FontAsset.font2);
		// g.setColor(Color.white);

		// graphics2D.drawString("TEST", 200, 200);

		for (int i = 0; i < mobCost.length; i++) {
			GraphicsDrawer.drawBitmapTextCenter(g, "$" + mobCost[i], 2, 0, Color.white, 50 * (i + 1) + 25, D.SCREEN_HEIGHT - 10);
			// Color.WHITE, 50 * (i + 1) + 25, D.SCREEN_HEIGHT - 10);
			// Rectangle2D r = FontAsset.font5.getStringBounds("$" + mobCost[i],
			// g.getFontRenderContext());
			// System.out.println(r);
			// g.drawString("$" + mobCost[i], 50 * (i + 1) + 25 - (int)
			// r.getWidth() / 2, D.SCREEN_HEIGHT - 10 + 10);
			GraphicsDrawer.drawBitmapTextRight(g, mobCoolDownTime[i] + "\"", 1, 0, Color.WHITE, 50 * (i + 2) - 2, D.SCREEN_HEIGHT - 48);
		}
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setCosts(int[] mobCost) {
		this.mobCost = mobCost;
	}

	public void setCoolDownTime(int[] time) {
		this.mobCoolDownTime = time;

	}
}
