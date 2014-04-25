package main.game.obj.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.List;

import main.flask.utils.Vec2D;
import main.game.obj.factory.bullet.Bullet1;
import main.game.obj.property.Shootable;

public abstract class BulletFactory implements Serializable {
	private static final long serialVersionUID = -8918982687517662349L;

	protected Constructor<Shootable> concretBullet;

	@SuppressWarnings("unchecked")
	public void setConcreteBullet(Class<? extends Shootable> bulletClass) throws NoSuchMethodException, SecurityException {
		this.concretBullet = (Constructor<Shootable>) bulletClass.getConstructor();
	}

	protected Shootable getInstance() {
		try {
			return (Shootable) this.concretBullet.newInstance();
		} catch (Exception e) {
			System.err.println("BulletFactory: Concrete Bullet Class is not setted");
			e.printStackTrace();
		}
		return new Bullet1();
	}

	public abstract List<Shootable> create(Vec2D position);

}
