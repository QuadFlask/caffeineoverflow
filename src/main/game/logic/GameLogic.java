package main.game.logic;

import static main.flask.net.SimpleLogger.log;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.flask.net.NetWorker;
import main.flask.net.OnReceiveEvent;
import main.flask.net.OnReceiveEvent.STATE;
import main.flask.net.OnReceiveListener;
import main.flask.net.WrapperPacket;
import main.flask.threadpool.DurationRunnable;
import main.flask.utils.Vec2D;
import main.game.common.GameInfoPacket;
import main.game.common.MobInfo;
import main.game.controller.GameController;
import main.game.event.BulletGeneratedListener;
import main.game.event.GenerateRequestEvent;
import main.game.event.GenerateRequestEventListener;
import main.game.event.OnChangeEvent;
import main.game.event.OnChangeEvent.CONTROLLER;
import main.game.event.TickEvent;
import main.game.graphics.effect.factory.ParticleEmitter;
import main.game.obj.behavior.MovingPattern.SIDE;
import main.game.obj.factory.BulletFactory;
import main.game.obj.factory.MobFactory;
import main.game.obj.factory.MultiShotFactory;
import main.game.obj.factory.TestBulletFactory;
import main.game.obj.factory.bullet.Bullet1;
import main.game.obj.factory.bullet.Bullet2;
import main.game.obj.factory.special.BlackHole;
import main.game.obj.factory.unit.Hero;
import main.game.obj.factory.unit.Unit;
import main.game.obj.factory.unit.mob.Mob;
import main.game.obj.factory.unit.mob.WaveMob;
import main.game.obj.property.Shootable;
import main.game.resource.ImageAsset;
import main.game.resource.ImageAsset.IMAGE_ID;
import main.game.view.View;
import main.game.view.ui.UI;
import main.game.windows.KeyPressingCallback;
import main.game.windows.KeyPressingCallbackAdapter;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

public class GameLogic implements Logic, GenerateRequestEventListener, OnReceiveListener {
	private ExecutorService threadPool = Executors.newFixedThreadPool(16);
	private Rectangle2D.Double stage;

	private List<Unit> unitList = new ArrayList<Unit>();
	private Hero hero;
	private BlackHole blackHole;

	private UnitUpdator unitUpdator;
	private HeroUpdator heroUpdator;
	private KeyResolver keyResolver;
	private BulletUpdator bulletUpdator;
	private BulletUpdator mobBulletUpdator;
	private MobBulletResolver mobBulletResolver;

	@SuppressWarnings("unused")
	private View view;
	private UI ui;
	private NetWorker netWorker;
	PingResponser pingResponser;

	public GameLogic(int width, int height, View view, NetWorker netWorker) {
		log("GameLogic GameLogic");
		this.view = view;
		this.netWorker = netWorker;
		pingResponser = new PingResponser(netWorker);
		netWorker.addOnReceiveListener(this);
		netWorker.addOnReceiveListener(pingResponser);

		BulletFactory f = new MultiShotFactory(new TestBulletFactory(null));
		try {
			f.setConcreteBullet(Bullet2.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		f = new MultiShotFactory(f);
		try {
			f.setConcreteBullet(Bullet1.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		hero = new Hero(new Vec2D(width / 2, height * 0.75));
		hero.setBulletFactory(f);
		hero.setShapes(ImageAsset.image2Int(IMAGE_ID.HERO_NORMAL, IMAGE_ID.HERO_RIGHT, IMAGE_ID.HERO_LEFT));

		stage = new Rectangle2D.Double(0, 36, width, height - 36);
		Rectangle2D heroStage = new Rectangle2D.Double(stage.getMinX() + 12, stage.getMinY() + 12, stage.getMaxX() - 24, stage.getMaxY() - 36 - 80);
		hero.setStage(heroStage);
		System.out.println("stage : " + stage.toString());
		System.out.println("heroStage : " + heroStage.toString());

		unitUpdator = new UnitUpdator();
		heroUpdator = new HeroUpdator();
		keyResolver = new KeyResolver();
		bulletUpdator = new BulletUpdator(stage, unitList);
		mobBulletUpdator = new BulletUpdator(stage, Arrays.asList((Unit) hero));
		mobBulletResolver = new MobBulletResolver();

		ui = new UI(width, height);
		ui.setMaxHP(hero.getHP());
		ui.addReqeustListener(this);
		// TODO 지속적으로 얻는 수입? 월급? 카페인도 이런식으로 지속저으로 소량 증가 하도록,
		// 유닛 잡았을때는 또 그 가격에 비례해서 얻고,
		Tween.call(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				ui.earnMoney(10);
			}
		}).repeat(1000, 2).start(View.getTweenManager());

		// Mob mob = new Mob(new Vec2D(100, 100));
		// mob.setMovingPattern(new TestMovingB());
		// unitList.add(mob);

		blackHole = new BlackHole(new Vec2D(250, 250), width, 100);
		blackHole.setSourceBuffer(view.getBufferedImage());
		// blackHole.startEffect(View.getTweenManager());

		bulletUpdator.addForceAffector(blackHole);
		mobBulletUpdator.addForceAffector(blackHole);
		ParticleEmitter.setBlackHole(blackHole);

		view.addDrawable(blackHole);
		view.addDrawable(ui);
	}

	public int getPing() {
		return pingResponser.getCurrentPing();
	}

	public static class PingResponser implements OnReceiveListener {
		private long tMilli = 0;
		private int seq;
		private long currentPing;
		private NetWorker netWorker;

		public PingResponser(NetWorker netWorker) {
			log("GameLogic PingResponser");
			this.netWorker = netWorker;
		}

		public synchronized void start(int seq) {
			log("[PingResponser] start!");
			tMilli = System.currentTimeMillis();
			netWorker.send(new WrapperPacket(STATE.REQ_PING, seq));
			this.seq = seq;
		}

		@Override
		public void onReceive(OnReceiveEvent e) {
			// log("PingResponser onReceive");
			switch (e.state) {
			case REQ_PING:
				log("[PingResponser:onReceive] PING!");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				tMilli = System.currentTimeMillis();
				netWorker.send(new WrapperPacket(STATE.ACK_PING, seq));
				break;
			case ACK_PING:
				log("[PingResponser:onReceive] ACK_PING!");
				WrapperPacket ack = (WrapperPacket) e.data;
				if (ack.seq == seq) {
					currentPing = System.currentTimeMillis() - tMilli;
					log("[PingResponser:onReceive] ACK_PING! - ping : " + currentPing);
					netWorker.send(new WrapperPacket(STATE.REQ_PING, ++seq));
				}
				break;
			default:
				log("[PingResponser:onReceive] default");
				break;
			}
		}

		public int getCurrentPing() {
			return (int) currentPing;
		}
	}

	private int seq = 0; // SequenceNumberFactory???
	private Timer gameInfoSender;

	private void cancelGameInfoSender() {
		if (gameInfoSender != null) {
			log("gameInfoSender : cancle");
			gameInfoSender.cancel();
			gameInfoSender.purge();
			gameInfoSender = null;
		}
	}

	@Override
	public void onReceive(OnReceiveEvent e) {
		log("[GameLogic] onReceiving....");
		switch (e.state) {
		case CONNECTED:
			log("    [GameLogic] CONNECTED!");
			pingResponser.start(seq);

			cancelGameInfoSender();

			gameInfoSender = new Timer();
			gameInfoSender.schedule(new TimerTask() {
				@Override
				public void run() {
					log("gameinfoSender");
					netWorker.send(new GameInfoPacket(hero.getHP(), null));
				}
			}, 10, 100);
			break;
		case RECEIVED:
			log("    [GameLogic] RECEIVED! : " + e.data.toString());
			infoPacketHolder = (GameInfoPacket) e.data;
			if (infoPacketHolder.hasMobInfo()) { // 몹 받아오기. 몹 인포로부터 몹 생성!
				MobInfo mobInfo = infoPacketHolder.getMobInfo();
				List<Mob> mobs = MobFactory.createFromMobInfo(mobInfo, mobBulletResolver);
				unitList.addAll(mobs);
				log("        [GameLogic] receving MobInfo and creating : " + mobs.size());
			}
			break;
		case ACK:
			log("    [GameLogic] ACK!");
			break;

		case REFUSED:
			log("    [GameLogic] REFUSED!");
			cancelGameInfoSender();
			break;
		case ERROR:
			log("    [GameLogic] ERROR!");
			cancelGameInfoSender();
			break;
		case DISCONNECTED:
			log("    [GameLogic] DISCONNECTED!");
			cancelGameInfoSender();
			break;
		case CLOSED:
			log("    [GameLogic] CLOSED!");
			cancelGameInfoSender();
			break;
		default:
			log("    [GameLogic] default!");
			break;
		}
	}

	GameInfoPacket infoPacketHolder;

	public void execute(TickEvent tickEvent) {
		// log("GameLogic execute");
		float duration = tickEvent.getElaspedTimeFromTick() * 100;

		heroUpdator.setDuration(duration);
		unitUpdator.setDuration(duration);
		bulletUpdator.setDuration(duration);
		mobBulletUpdator.setDuration(duration);

		threadPool.execute(heroUpdator);
		threadPool.execute(unitUpdator);
		threadPool.execute(bulletUpdator);
		threadPool.execute(mobBulletUpdator);
		threadPool.execute(ParticleEmitter.getForceAccumRunner());

		if (blackHole.isAnimating()) threadPool.execute(blackHole.getBackgroundRunner());

		int otherHp = 0;
		if (infoPacketHolder != null) otherHp = infoPacketHolder.getHero_hp();
		ui.setValues(hero.getHP(), otherHp);
		ui.update();
	}

	@Override
	public void onReqeust(GenerateRequestEvent event) {
		// log("GameLogic onReqeust");
		Class<? extends Unit> unitClass = event.unitClass;

		if (unitClass.equals(BlackHole.class)) {
			System.out.println("BlackHole");
			if (!blackHole.isAnimating()) {
				blackHole.startEffect(View.getTweenManager());
				blackHole.setPosition(hero.getX(), hero.getY());
			}
		} else if (unitClass.equals(Mob.class)) {
			WaveMob m = new WaveMob(SIDE.LEFT, mobBulletResolver);
			unitList.add(m);

			m = new WaveMob(SIDE.RIGHT, mobBulletResolver);
			unitList.add(m);

			netWorker.send(new GameInfoPacket(hero.getHP(), new MobInfo(1, 0, 0, 0)));

			// TODO 여기서 몹 생성하는데, 몹의 위치가 0 이하가 되면, 샌드, 그렇지 않으면 위로 올라가면서 대충 싸우다가
			// 샌드.
			// 싸우다가 적 총알 맞으면 피 달은 상태로 샌드. 여기서 중요한건 몹 타입, 몹 위치(x), 몹 피, 몹 공격 패턴,
			// 몹 무빙 패턴, 몹 가격
			// (죽였을때 얻는 돈 -> 이거는 뷰단에서도 반영 되야함. 지금은 그냥 무조건 +100 표시됨...ㅅㅂ)이 있겠고,
			// 그외에 또 보낼 데이터는 히어로 피,,,만 보내도 될듯..
			// 햅퍼 패킷을 더 확장해서 만들어야 할듯? 아니면 따로 게임패킷 이렇게 만든다음에 그 안에 아주 필수 적인것만
			// 넣어두고(되도록 프리미티브 타입만 넣자.)
			// 리시브 이벤트에서 받도록 하는게 좋을듯함....

		} else {
			System.out.println("Null");
		}
	}

	// /////////////////////////////////////////////

	class UnitUpdator extends DurationRunnable {
		List<Unit> deleteList = new ArrayList<>();

		@Override
		public void run() {
			// log("GameLogic onReqeust");
			deleteList.clear();

			for (Unit b : unitList) {
				b.updatePosition(duration);
				if (b.isDead()) deleteList.add(b);
			}

			synchronized (unitList) {
				unitList.removeAll(deleteList);
			}
			// TODO add money and caffeine.
			// MoneyAndCaffeineProvider???
			// 여기서 돈, 카페인 관리??? 하고 유아이가 이넘ㄴ을 받아서 처리?????ㄴ
			moneyProvider.setProvided(deleteList);
			ui.earnMoney(moneyProvider.getMoney());
		}
	}

	class MobBulletResolver implements BulletGeneratedListener {
		private static final long serialVersionUID = 1912096412248133881L;

		@Override
		public void onGenerated(List<Shootable> shootables) {
			// log("MobBulletResolver onGenerated, total : " +
			// mobBulletUpdator.getBulletCount());
			log("MobBulletResolver onGenerated");
			mobBulletUpdator.addShootables(shootables);
		}
	}

	MoneyProvider moneyProvider = new MoneyProvider();

	class HeroUpdator extends DurationRunnable {
		@Override
		public void run() {
			// log("HeroUpdator run");
			hero.updatePosition(duration);
		}
	}

	class KeyResolver extends KeyPressingCallbackAdapter {
		private boolean dispatched = false;

		@Override
		public void onPressing(List<Integer> keyCodes) {
			 log("KeyResolver onPressing");
			float tx = 0, ty = 0;
			if (keyCodes.contains(KeyEvent.VK_UP)) ty = -1;
			if (keyCodes.contains(KeyEvent.VK_DOWN)) ty = 1;
			if (keyCodes.contains(KeyEvent.VK_LEFT)) tx = -1;
			if (keyCodes.contains(KeyEvent.VK_RIGHT)) tx = 1;
			if (keyCodes.contains(KeyEvent.VK_Z)) bulletUpdator.addShootables(hero.attack());
			if (keyCodes.contains(KeyEvent.VK_SHIFT)) hero.lowSpeed();
			else hero.restoreSpeed();

			if (keyCodes.contains(KeyEvent.VK_ESCAPE) && !dispatched) {
				gameController.dispatch(new OnChangeEvent(CONTROLLER.MENU, 1));
				dispatched = true;
			}
			if (tx * tx + ty * ty >= 1) hero.move(tx, ty);
		}
	}

	// /////////////////////////////////////////////

	public List<Shootable> getBulletList() {
		return bulletUpdator.getBulletList();
	}

	public List<Shootable> getMobBulletList() {
		return mobBulletUpdator.getBulletList();
	}

	public void shutdown() {
		System.out.println("shutdown : " + threadPool.shutdownNow().size());
	}

	public Unit getHero() {
		return hero;
	}

	public List<Unit> getUnitList() {
		return unitList;
	}

	public List<KeyPressingCallback> getKeyCallbacks() {
		return Arrays.asList(keyResolver, ui);
	}

	GameController gameController;

	public void setController(GameController gameController) {
		this.gameController = gameController;
	}

	@Override
	public void free() {
		log("GameLogic free");
		netWorker.removeAll();
		netWorker.close();
		cancelGameInfoSender();
	}

}
