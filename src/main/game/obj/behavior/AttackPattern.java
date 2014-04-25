package main.game.obj.behavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.flask.utils.Vec2D;
import main.game.event.BulletGeneratedListener;
import main.game.obj.factory.unit.Unit;
import main.game.obj.property.Shootable;
import main.game.view.View;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

public class AttackPattern implements Serializable {
	private static final long serialVersionUID = -4623972746288379967L;
	
	private List<Pattern> patterns = new ArrayList<Pattern>();
	private List<Shootable> shootables = new ArrayList<Shootable>();
	private boolean isLoop = false;
	private Timeline timeLine;
	private Unit unit;

	private BulletGeneratedListener listener;

	public AttackPattern(Unit unit, List<Pattern> patterns) {
		this(unit, patterns, false);
	}

	public AttackPattern(Unit unit, List<Pattern> patterns, boolean loop) {
		this.unit = unit;
		this.patterns = patterns;
		this.isLoop = loop;
	}

	public void start() {
		timeLine = Timeline.createSequence();
		for (Pattern p : patterns) {
			timeLine.push(makeTween(p));
			timeLine.pushPause(p.getPostDelay());
		}
		if (isLoop) timeLine.repeat(100, patterns.get(patterns.size() - 1).getPreDelay());
		timeLine.start(View.getTweenManager());
	}

	private Tween makeTween(Pattern p) {
		p.setParent(this);
		return Tween.call(p).delay(p.getPreDelay());
	}

	public void clear() {
		timeLine.free();
	}

	public void addShootables(List<Shootable> s) {
		synchronized (shootables) {
			shootables.addAll(s);
			listener.onGenerated(copy());
			shootables.clear();
		}
	}

	private List<Shootable> copy() {
		List<Shootable> dest = new ArrayList<Shootable>();
		for (Shootable s : shootables) {
			dest.add(s);
		}
		return dest;
	}

	public void setBulletGeneratedListener(BulletGeneratedListener listener) {
		this.listener = listener;
	}

	public Vec2D getPosition() {
		return unit.getPosition();
	}

	public void setLoop(boolean loop) {
		isLoop = loop;
	}
}
