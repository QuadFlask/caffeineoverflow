package main.game.controller;

import static main.flask.net.SimpleLogger.log;

import java.awt.event.KeyEvent;

import main.game.common.D;
import main.game.event.OnChangeEvent;
import main.game.event.OnChangeEventDispatcher;
import main.game.event.OnChangeEventListener;
import main.game.resource.BGMPlayer;
import main.game.windows.Display;
import main.game.windows.KeyPressingCallbackAdapter;
import main.game.windows.WindowForm;

public class MainController implements OnChangeEventListener {

	private GameController gameController;
	private MenuController menuController;
	private Display display;
	private WindowForm windowForm;

	public MainController() {
		windowForm = new WindowForm(D.FORM_TITLE, D.SCREEN_WIDTH, D.SCREEN_HEIGHT, D.FPS);
		display = new Display(windowForm);
		display.getKeyManager().reset();

		// 메인 컨트롤러 -> 메뉴 컨트롤러
		// 메인 컨트롤러 -> 게임 컨트롤러
		// 메인 컨트롤러 -> 랭킹 컨트롤러
		// 메인으로 부터 메시지 받아서, 메뉴을 시작한다.
		// 메뉴에선 게임/랭킹을 실행시킨다.
		// 그러다가 게임/랭킹이 끝나면 다시 메인을 호출!, 메뉴를 띄움.
		// 그런데 이벤트 전달을 하는 주체는 메뉴/게임/랭킹 컨트롤러이고, 리스너는 메인컨트롤러임.
		// 따라서, 메인 컨트롤러는 리스너를 구현하고, 메뉴/게임/랭킹 컨트롤러는 이벤트를 발생시켜야함.
		// 그런데 어차피 이벤트를 넘기는것도 그냥 어차피 레퍼런스를 등록밖에 안하는거라서 의미가 어신듯?
		// GameController.addListener(this);
		// in GameController....
		// fireOnChangeEvent(new )
		// EventDispatcher
		// display.addView(new MainMenu(D.SCREEN_WIDTH, D.SCREEN_HEIGHT));

		// // For debug test start
		// gameController = new GameController(true); // 여기서 한번 블로킹.
		// gameController.addListener(this);
		//
		// display.addView(gameController.getView()); // Add views
		//
		// windowForm.start(display); // start!

		onChange(OnChangeEventDispatcher.RETURN_TO_MENU);
		// TODO 일단 브금 중지....
		if (D.BGM_ON) BGMPlayer.playRandomBGM(1);
	}

	@Override
	public void onChange(final OnChangeEvent e) {
		// log("[MainController] onChange:" + e.controller.name());
		switch (e.controller) {
		case GAME:
			// 기존 게임 컨트롤러 제거 작업 ㄱㄱ
			// 하기 전에! 화면 전환 에니메이션 실행ㄱㄱ 해줘야
			// 전환 에니메이션 끝나면 다시 실행....
			// if (gameController != null) {
			// gameController.free();
			// System.gc();
			// }

			menuController.showWaitBox(isServer(e));
			gameController = new GameController();
			gameController.addOnChangeListener(this);

			display.getKeyManager().addCallback(new KeyPressingCallbackAdapter() {
				@Override
				public void onReleased(int keyCode) {
					if (keyCode == KeyEvent.VK_ESCAPE) {
						gameController.free();
						display.getKeyManager().removeCallback(this);
					}
				}
			});

			new Thread(new Runnable() {
				@Override
				public void run() {
					boolean connectionResult = D.SKIP_NETWORK;

					try {
						if (!D.SKIP_NETWORK) connectionResult = gameController.startNetworking(isServer(e)); // 여기서블로킹.
					} catch (Exception e) {
						e.printStackTrace();
					}
					menuController.hideWaitBox();
					log("connectionResult : " + connectionResult);

					if (connectionResult) { // connected
						freeController(menuController);
						attachController(gameController); // start!
					} else { // not connected
						menuController.showErrorBox();
						freeController(gameController);
					}
				}
			}).start();
			break;
		case MENU:
			freeController(gameController);

			System.gc();

			menuController = new MenuController(); // 여기서 한번 블로킹.
			menuController.addOnChangeListener(this);

			attachController(menuController); // start!
			break;
		case RANK:
			break;
		default:
			break;
		}
	}

	private void freeController(Controller controller) {
		if (controller != null) {
			display.removeView(controller.getView());
			controller.free();
		}
	}

	private void attachController(Controller controller) {
		display.addView(controller.getView()); // Add view
		if (!windowForm.isStarted()) windowForm.start(display); // start!
	}

	private boolean isServer(OnChangeEvent e) {
		return e.data == 1;
	}

}
