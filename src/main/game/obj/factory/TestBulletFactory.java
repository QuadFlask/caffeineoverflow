package main.game.obj.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import main.flask.utils.Vec2D;
import main.game.obj.factory.bullet.Bullet1;
import main.game.obj.property.Shootable;

public class TestBulletFactory extends BulletFactory {
	private final int ROUNDS_PER_SECOND = 10;
	private Timer timer = new Timer();
	private BulletFactory bulletFactory;
	private boolean isOpenFire = true;
	
	public TestBulletFactory(BulletFactory bulletFactory) {
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
			
			List<Shootable> list = new ArrayList<Shootable>();
			Bullet1 b = new Bullet1();
			b.setPosition(position);
			b.setVelocity(new Vec2D(0, -8));
			list.add(b);
			
			isOpenFire = false;
			return list;
		}
	}
	
}
