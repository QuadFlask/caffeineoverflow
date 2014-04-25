package main.game.view.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.flask.utils.StringUtil;
import main.game.common.D;
import main.game.graphics.GraphicsDrawer;
import main.game.resource.ImageAsset;
import main.game.resource.ImageAsset.IMAGE_ID;
import main.game.view.View;

public class StatusBar extends View implements DrawableComponent {
	private BufferedImage myHPImage, otherHPImage, caffeinImage, coffee_bean, glow_point_green, glow_point_red, glow_point_yellow;
	private Color backgroundColor = new Color(0xcc000000, true);
	private static int height = 48;
	private float half = 0;
	
	private float myHp = 80;
	private float otherHp = 50;
	private int targetMoney = 10000;
	private float money = 0;
	private float caffein = 50;
	
	private int tab = 10;
	
	public StatusBar(int width) {
		super(width, height);
		this.half = (width - tab * 2) / 2;
		
		myHPImage = new BufferedImage(1, 4, BufferedImage.TYPE_4BYTE_ABGR);
		otherHPImage = new BufferedImage(1, 4, BufferedImage.TYPE_4BYTE_ABGR);
		caffeinImage = new BufferedImage(1, 4, BufferedImage.TYPE_4BYTE_ABGR);
		
		BufferedImage b = ImageAsset.getImage(IMAGE_ID.GAUGEBAR);
		coffee_bean = ImageAsset.getImage(IMAGE_ID.COFFEE_BEAN);
		glow_point_green = ImageAsset.getImage(IMAGE_ID.GLOW_POINT_GREEN);
		glow_point_red = ImageAsset.getImage(IMAGE_ID.GLOW_POINT_RED);
		glow_point_yellow = ImageAsset.getImage(IMAGE_ID.GLOW_POINT_YELLOW);
		
		myHPImage.getGraphics().drawImage(b, 0, 0, 1, 4, null);
		
		for (int y = 0; y < 4; y++)
			otherHPImage.setRGB(0, y, b.getRGB(0, y));
		
		for (int y = 0; y < 4; y++)
			caffeinImage.setRGB(0, y, b.getRGB(2, y));
	}
	
	public void update() {
		graphics2D.clearRect(0, 0, width, height);
		graphics2D.setColor(backgroundColor);
		graphics2D.fillRect(0, 0, width, height - 12);
		
		updateHP();
		
		drawHP();
		drawTexts();
		updateText();
	}
	
	private void drawHP() {
		int myHpGuage = (int) (myHp / max * (half - tab));
		graphics2D.drawImage(myHPImage, (int) (half - myHpGuage), 32, myHpGuage, 4, null);
		graphics2D.drawImage(glow_point_green, (int) (half - myHpGuage) - 10, 24, 20, 20, null);
		
		graphics2D.drawImage(otherHPImage, (int) half, 32, (int) (otherHp / max * (half + tab)), 4, null);
		graphics2D.drawImage(glow_point_red, (int) (half + otherHp / max * (half + tab)) - 10, 24, 20, 20, null);
		
		graphics2D.drawImage(coffee_bean, (int) half - coffee_bean.getWidth() / 2 - 50, 4, coffee_bean.getWidth(), coffee_bean.getHeight(), null);
	}
	
	private void drawTexts() {
		GraphicsDrawer.drawBitmapText(graphics2D, money_str, 3, 1, Color.yellow, (int) half - 30, 8);
	}
	
	// //////
	
	public int getHeight() {
		return height;
	}
	
	private float myTargetHP, myCurrentHP;
	private float otherTargetHP2, otherCurrentHP2;
	private float max;
	
	public void setMaxHP(float max) {
		this.max = max;
	}
	
	private void updateHP() {
		myCurrentHP += 0.04 * (myTargetHP - myCurrentHP);
		otherCurrentHP2 += 0.04 * (otherTargetHP2 - otherCurrentHP2);
	}
	
	public void setHPValues(int my, int other) {
		myTargetHP = my;
		otherTargetHP2 = other;
		this.myHp = (int) myCurrentHP;
		this.otherHp = (int) otherCurrentHP2;
	}
	
	public void setCaffein(float caffein) {
		this.caffein = caffein;
	}
	
	public void setMoney(int newMoney) {
		targetMoney = newMoney;
	}
	
	public void increaseMoney(int amount) {
		targetMoney += amount;
	}
	
	public void decreaseMoney(int amount) {
		targetMoney -= amount;
	}
	
	public boolean isMoneyAvailable(int decAmount) {
		return targetMoney >= decAmount;
	}
	
	private String money_str = "* ------";
	private int calced = 0;
	
	private void updateText() {
		money += 0.15f * (targetMoney - money);
		calced = (int) (money + 0.5f);
		money_str = "* " + StringUtil.zeroPadding(calced, 5);
	}
	
	@Override
	public boolean draw(Graphics2D g) {
		g.drawImage(bufferedImage, 0, 0, width, height, null);
		
		int iCaffein = (int) (caffein / 100 * width);
		g.drawImage(caffeinImage, 0, D.SCREEN_HEIGHT - 54, iCaffein, 4, null);
		g.drawImage(glow_point_yellow, iCaffein - 10, D.SCREEN_HEIGHT - 52 - 10, 20, 20, null);
		
		GraphicsDrawer.drawBitmapText(g, StringUtil.paddingWith((int) caffein, 3, "|") + "%", 3, 1, Color.yellow, 2, D.SCREEN_HEIGHT - 50 + 4);
		return true;
	}
	
}
