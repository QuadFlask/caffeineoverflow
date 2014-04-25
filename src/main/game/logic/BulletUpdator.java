package main.game.logic;

import static main.flask.net.SimpleLogger.log;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.flask.threadpool.DurationRunnable;
import main.flask.utils.Vec2D;
import main.game.obj.factory.special.BlackHole;
import main.game.obj.factory.unit.Unit;
import main.game.obj.property.Shootable;
import main.game.resource.GameSoundPlayer;
import main.game.resource.SoundAsset.SOUND_ID;

/*
 * 히어로가 생성한 총알은 컨트롤러에 넘어가고, 컨트롤러가 따로 이 총알들을 관리한다.
 * 
 * 각각 총알이 밖으로 나갔을때 삭제도 여기서, 총알들이 각각 호밍능력이 있을땬 여기서 최초 타겟을 인젝션.
 * 
 * 타겟은 최초 한번만 인젝션 하고, 이 타겟이 없어지면 마지막 위치에까지 가서 터지는 이펙트 생성.
 * 
 * 총알 관리에서 할 것들...
 * 
 * 총알 스테이지 나갔을때 삭제.
 * 
 * 총알 업데이트(내부적으로 업데이트 되기 때문에 여기서는 호출만)
 * 
 * 총알 부가 기능 발현 타이밍. 탄막 미사일의 경우, 주변에 일정 거리 이내에 몹이 있을때 탄막 스프레딩. 스포어미사일? 결정.
 * 
 * 포스 적용. ( 블랙홀 ... )
 * 
 * 충돌 판정.
 * 
 * 
 * 시퀀스
 * 
 * 히어로의 불릿펙토리를 통한 총알 생성.
 * 
 * 총알들은 컨트롤러에 등록됨.
 * 
 * 컨트롤러에서는 등록된 총알들에 호밍 여부를 확힌하고, 호밍불릿일때 타깃을 할당해준다. -> 타깃 할당 알고리즘 필요. 여러개 발사시엔 같은 타깃만 적용될 수 있기 때문에 두번째 가까운놈이나 세번째 등등 랜덤이나 이런식으로 같은 타깃 할당을 막도록,.
 * 
 * 게임로직으로 부터 틱을 받아 총알들 업데이트 실시. -> 이 부분은 게임 로직으로 부터 틱을 받아야 에니메이션을 시간 기반으로 가능한데, 이러면 성능 저하.
 * 
 * 따라서, 틱은 따로 할당 받고, 그와 관련을 최소화 해서 따로 쓰레드 돌리기로.
 *  
 * 
 * 업데이트 실시중, 
 * 
 * 업데이트 매서드로부터 불린값 false 를 받으면, 폭팔 이펙트 생성뒤 리스트에서 삭제.
 * 
 * 또는 스테이지 엇어나면 리스트에서 삭제.
 * 
 */

public class BulletUpdator extends DurationRunnable {

	private List<Shootable> shootables = Collections.synchronizedList(new ArrayList<Shootable>());
	private List<Unit> mobs;

	private List<Unit> unitList;
	private List<Shootable> outOfStageList = new ArrayList<Shootable>();
	private List<Shootable> deleteList = new ArrayList<Shootable>();
	private List<Shootable> addingList = new ArrayList<Shootable>();
	private TargetSelector targetSelector = new TargetSelector();
	private BlackHole blackHole;

	private Rectangle2D stage;
	private final int gcDelay = 10;
	private int gcTime = 0;

	public BulletUpdator(Rectangle2D stage, List<Unit> unitList) {
		setStage(stage);
		System.out.println(stage.toString());
		this.unitList = unitList;
	}

	@Override
	public void run() {
		update(duration);
	}

	public void update(float duration) {
		deleteList.clear();
		addingList.clear();

		if (++gcTime > gcDelay) {
			outOfStageList.clear();
			synchronized (shootables) {
				for (Shootable b : shootables)
					if (!b.checkInStage(stage)) outOfStageList.add(b);
			}
			gcTime = 0;
			log("bullet GC prepare... " + outOfStageList.size());
		}

		if (blackHole.isAnimating()) addBlackHoleForce();

		collisionCheck();

		synchronized (shootables) {
			shootables.removeAll(outOfStageList);
			shootables.removeAll(deleteList);
			shootables.addAll(addingList);
			for (Shootable s : deleteList) {
				s.setConsumed();
			}
		}

		updateShootables(duration);
	}

	private void collisionCheck() {
		synchronized (shootables) {
			Vec2D sPos, uPos;
			for (Shootable s : shootables) {
				sPos = s.getPosition();
				innerFor: for (Unit unit : unitList) {
					uPos = unit.getPosition();
					// System.out.println(unit.toString());
					if (sPos.y > uPos.y - 100) {
						if (sPos.distanceWith2(uPos) < unit.getUnitSizeRange2()) {
							unit.damaging(s.getDamage());
							deleteList.add(s);
							break innerFor;
						}
					}
				}
			}
		}
	}

	private void addBlackHoleForce() {
		for (Shootable s : shootables) {
			s.addForce(blackHole.getForce(s.getPosition()));
		}
	}

	private void updateShootables(float duration) {
		synchronized (shootables) {
			for (Shootable s : shootables) {
				if (!s.update(duration)) {
					deleteList.add(s);
					break;
				}

				// if (s.isSpread()) {
				// if (targetSelector.isInRange(s, 64)) {
				// addingList.addAll(s.activateFunction());
				// }
				// }
			}
		}
	}

	public void addShootable(Shootable s) {
		if (s.isHomming()) {
			Unit u = targetSelector.getClosest(s);
			if (u != null) {
				s.setTarget(u);
			}
		}
		shootables.add(s);
	}

	public void addShootables(List<Shootable> s) {
		shootables.addAll(s);
		GameSoundPlayer.play(SOUND_ID.EFFECTS_SHOT_SHORT, 0.15f);
	}

	public void setMobListRef(List<Unit> mobs) {
		this.mobs = mobs;
	}

	public void addForceAffector(BlackHole b) {
		this.blackHole = b;
	}

	public void setStage(Rectangle2D stage) {
		this.stage = stage;
	}

	public List<Shootable> getBulletList() {
		return shootables;
	}

	public int getBulletCount() {
		return shootables.size();
	}

	class TargetSelector {
		public Unit getClosest(Shootable s) {
			Vec2D pos = s.getPosition();
			Unit target = null;
			double min = Double.MAX_VALUE, d;

			for (Unit mob : mobs) {
				d = pos.distanceWith(mob.getPosition());
				if (min > d) {
					target = mob;
					min = d;
				}
			}

			return target;
		}

		public boolean isInRange(Shootable s, float range) {
			Vec2D pos = s.getPosition();
			for (Unit mob : mobs) {
				if (pos.distanceWith(mob.getPosition()) < range) return true;
			}
			return false;
		}

		public Unit getAutoSelect(Shootable s) {
			return null;
		}
	}

}

//

//

//

//

//