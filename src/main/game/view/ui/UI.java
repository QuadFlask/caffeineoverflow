package main.game.view.ui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.game.event.GenerateRequestEvent;
import main.game.event.GenerateRequestEventListener;
import main.game.graphics.effect.factory.ParticleEmitter;
import main.game.obj.factory.UnitFactory;
import main.game.resource.GameSoundPlayer;
import main.game.resource.SoundAsset.SOUND_ID;
import main.game.view.View;
import main.game.windows.KeyPressingCallback;

public class UI implements DrawableComponent, KeyPressingCallback {
	
	private StatusBar statusBar;
	private FactoryBar factoryBar;
	private int factoryBarY;
	
	public UI(int width, int height) {
		statusBar = new StatusBar(width);
		
		factoryBar = new FactoryBar(width, View.getTweenManager());
		factoryBarY = height - 50;
		factoryBar.setY(factoryBarY);
		factoryBar.makeCoolDownEffects(UnitFactory.MOB_COST, UnitFactory.MOB_COOLDOWN_TIME);
	}
	
	public void setValues(int hero_hp, int other_hp) {
		statusBar.setHPValues(hero_hp, other_hp);
	}
	
	public void setMaxHP(int max) {
		statusBar.setMaxHP(max);
	}
	
	public void update() {
		statusBar.update();
		factoryBar.update();
	}
	
	@Override
	public boolean draw(Graphics2D g) {
		factoryBar.draw(g);
		statusBar.draw(g);
		return true;
	}
	
	@Override
	public void onPressing(int keyCode) {}
	
	private List<GenerateRequestEventListener> generateRequestEventListenerList = new ArrayList<GenerateRequestEventListener>();
	
	public void addReqeustListener(GenerateRequestEventListener l) {
		generateRequestEventListenerList.add(l);
	}
	
	public void removeReqeustListener(GenerateRequestEventListener l) {
		generateRequestEventListenerList.remove(l);
	}
	
	private void fireReqeustEvent(GenerateRequestEvent e) {
		for (GenerateRequestEventListener eventListener : generateRequestEventListenerList) {
			eventListener.onReqeust(e);
		}
	}
	
	public void earnMoney(int amount) {
		statusBar.increaseMoney(amount);
	}
	
	private Map<Integer, Boolean> releaseMap = new HashMap<>();
	
	@Override
	public void onPressing(List<Integer> keyCodes) {
		int zeroBased;
		for (int i = KeyEvent.VK_1; i <= KeyEvent.VK_9; i++) {
			zeroBased = i - KeyEvent.VK_1;
			if (!containKey(keyCodes, zeroBased, i)) releaseMap.put(i, true);
			
			if (containKey(keyCodes, zeroBased, i)) {
				final int mobCost = getMobCost(zeroBased);
				
				if (statusBar.isMoneyAvailable(mobCost) && !factoryBar.isAnimating(zeroBased)) {
					if (factoryBar.startEffect(zeroBased + 1)) {
						statusBar.decreaseMoney(mobCost);
						
						final GenerateRequestEvent request = new GenerateRequestEvent(this);
						request.unitClass = UnitFactory.requestUnit(zeroBased);
						fireReqeustEvent(request);
						
						GameSoundPlayer.play(SOUND_ID.EFFECTS_BOUGHT2, 0.4f);
						releaseMap.put(i, false);
					}
				}
				// not enough money or not yet warning
				if (releaseMap.get(i) == null || releaseMap.get(i)) {
					releaseMap.put(i, false);
					GameSoundPlayer.play(SOUND_ID.EFFECTS_SE_CHANGEITEM, 0.4f);
					ParticleEmitter.makeBalloonBox(75 + zeroBased * 50, factoryBarY - 15, "NOT YET!", 1, 2);
				}
			}
		}
	}
	
	private boolean containKey(List<Integer> keyCodes, int zeroBased, int i) {
		return keyCodes.contains(i) || keyCodes.contains(KeyEvent.VK_NUMPAD1 + zeroBased);
	}
	
	@Override
	public void onNotPressing() {
		clearReleaseMap();
	}
	
	private void clearReleaseMap() {
		for (int i = KeyEvent.VK_1; i <= KeyEvent.VK_9; i++)
			releaseMap.put(i, true);
	}
	
	private int getMobCost(final int zeroBased) {
		return UnitFactory.MOB_COST[zeroBased];
	}
	
	@Override
	public void onReleased(int keyCode) {}
	
}
