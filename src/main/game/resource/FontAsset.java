package main.game.resource;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class FontAsset {

	// private static HashMap<>
	public static Font font1;
	public static Font font2;
	public static Font font3;
	public static Font font4;
	public static Font font5;
	public static Font font6;

	static {
		try {
			font1 = Font.createFont(Font.TRUETYPE_FONT, new File(fontDirectoryResolver("Montserrat-Regular"))).deriveFont(12.0f);
			font2 = Font.createFont(Font.TRUETYPE_FONT, new File(fontDirectoryResolver("mplus-1c-medium"))).deriveFont(11.0f);
			font3 = Font.createFont(Font.TRUETYPE_FONT, new File(fontDirectoryResolver("mplus-1c-thin"))).deriveFont(12.0f);
			font4 = Font.createFont(Font.TRUETYPE_FONT, new File(fontDirectoryResolver("DIN Bold"))).deriveFont(12.0f);
			font5 = Font.createFont(Font.TRUETYPE_FONT, new File(fontDirectoryResolver("DIN Medium"))).deriveFont(12.0f);
			font6 = Font.createFont(Font.TRUETYPE_FONT, new File(fontDirectoryResolver("DIN Light"))).deriveFont(12.0f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	// 2번이 제일 나음...
	// graphics2D.setFont(FontAsset.font5.deriveFont(12.0f));
	// graphics2D.setColor(Color.white);
	// graphics2D.drawString("TEST", 200, 200);

	private static String fontDirectoryResolver(String name) {
		return String.format("%s/%s/%s.ttf", AssetConfigs.ASSET_ROOT, AssetConfigs.FONT_ROOT, name);
	}
}
