package main.game.graphics;

import java.awt.*;

import main.game.graphics.effects.Layer3D;

public class Sprite {
	private static final Color DefaultColor = new Color(0xffccff00);
	//
	public Layer3D layer;
	public Color fillColor = DefaultColor;
	public Color strokeColor = DefaultColor;
	public boolean isStroke = true;
	public boolean isFill = false;

	public Sprite(Layer3D layer) {
		this.layer = layer;
	}

	public static class Builder {
		private Sprite me;

		public Builder(Layer3D layer) {
			this.me = new Sprite(layer);
		}

		public Builder fillColor(Color color) {
			me.fillColor = color;
			return this;
		}

		public Builder fillColor(int color) {
			me.fillColor = new Color(color, true);
			return this;
		}

		public Builder strokeColor(Color color) {
			me.strokeColor = color;
			return this;
		}

		public Builder strokeColor(int color) {
			me.strokeColor = new Color(color, true);
			return this;
		}

		public Builder setDrawOpt(boolean isStroke, boolean isFill) {
			me.isFill = isFill;
			me.isStroke = isStroke;
			return this;
		}

		public Sprite build() {
			return me;
		}
	}
}