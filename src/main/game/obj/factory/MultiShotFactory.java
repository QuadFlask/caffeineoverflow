package main.game.obj.factory;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import main.flask.utils.Vec2D;
import main.game.obj.property.Shootable;

public class MultiShotFactory extends BulletFactory {
	private final int ROUNDS_PER_SECOND = 10;
	private Timer timer = new Timer();
	private BulletFactory bulletFactory;
	private boolean isOpenFire = true;
	
	public MultiShotFactory(BulletFactory bulletFactory) {
		this.bulletFactory = bulletFactory;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				synchronized (this) {
					isOpenFire = true;
				}
			}
		}, 0, (long) (1000.0f / Math.max(1, ROUNDS_PER_SECOND)));
	}
	
	@Override
	public List<Shootable> create(Vec2D position) {
		synchronized (this) {
			if (!isOpenFire) return Collections.emptyList();
			
			List<Shootable> list = bulletFactory.create(position);
			if (list.size() == 0) return Collections.emptyList();
			
			int count = (list.size() + 1) / 2;
			double angle = count * Math.PI / 15;
			
			Shootable b = list.get(0);
			
			Shootable bClone = getInstance();
			bClone.setPosition(b.getPosition().clone());
			bClone.setVelocity(b.getVelocity().getRotate(angle));
			list.add(bClone);
			
			bClone = getInstance();
			bClone.setPosition(b.getPosition().clone());
			bClone.setVelocity(b.getVelocity().getRotate(-angle));
			list.add(bClone);
			
			isOpenFire = false;
			return list;
		}
	}
}
