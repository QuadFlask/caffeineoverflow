package main.game.graphics.particles_deprecated;

import java.awt.Color;


public class EffectParticle extends Particle {
	public Color[] colorList;
	public double life = 1;
	
	public EffectParticle(int x, int y) {
		super(x, y);
	}
	
	@Override
	public boolean integrate() {
		return super.integrate();
	}
}
