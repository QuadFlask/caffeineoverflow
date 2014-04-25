package main.game.obj.behavior;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import main.flask.utils.Vec2D;
import main.game.obj.property.Shootable;

public class Attack {
	
	// TODO 총알들 존나 많이 만들때 new 너무 많이 호출하기 때문에 속도가 떨이지는듯? 아니면 드로잉 부분인데,,,;;
	// 드로잉할때도 좀 최적화 할 방법 찾아야...
	// 총알 풀 생성해서 총알 생성하면 여기에 담아두기 해야겟음.
	
	public static List<? extends Shootable> simpleStraigh(Class<? extends Shootable> bulletClass, Vec2D position, float speed) {
		List<Shootable> s = new ArrayList<Shootable>();
		Shootable bullet = getConcreteBullet(bulletClass);
		bullet.setPosition(position.clone());
		bullet.addForce(new Vec2D(0, speed));
		s.add(bullet);
		return s;
	}
	
	public static List<? extends Shootable> polar(Class<? extends Shootable> bulletClass, int count, Vec2D position, float speed) {
		List<Shootable> s = new ArrayList<Shootable>();
		Shootable bullet;
		Vec2D force = new Vec2D(0, speed);
		for (int i = 0; i < count; i++) {
			bullet = getConcreteBullet(bulletClass);
			bullet.setPosition(position.clone());
			force.rotate(Math.PI * 2 * ((float) i / count));
			bullet.addForce(force);
			s.add(bullet);
		}
		return s;
	}
	
	public static List<? extends Shootable> arc(Class<? extends Shootable> bulletClass, int count, Vec2D position, float speed, float centerAngle, float rangeAngle) {
		List<Shootable> s = new ArrayList<Shootable>();
		Shootable bullet;
		Vec2D force = new Vec2D(0, speed);
		float tick = rangeAngle / count;
		centerAngle -= rangeAngle / 2;
		for (int i = 0; i < count; i++) {
			bullet = getConcreteBullet(bulletClass);
			bullet.setPosition(position.clone());
			force.rotateBy60(centerAngle + tick * i);
			bullet.addForce(force);
			s.add(bullet);
		}
		return s;
	}
	
	public static Shootable getConcreteBullet(Class<? extends Shootable> bulletClass) {
		try {
			Constructor<? extends Shootable> concretBullet = (Constructor<? extends Shootable>) bulletClass.getConstructor();
			return (Shootable) concretBullet.newInstance();
		} catch (Exception e) {
			System.err.println("BulletFactory: Concrete Bullet Class is not setted");
			e.printStackTrace();
		}
		return null;
	}
	
}
