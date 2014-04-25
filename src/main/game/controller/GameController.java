package main.game.controller;

import main.flask.net.NetWorker;
import main.game.common.D;
import main.game.event.OnChangeEventDispatcher;
import main.game.logic.GameLogic;
import main.game.logic.Logic;
import main.game.view.GameView;
import main.game.view.View;

public class GameController extends OnChangeEventDispatcher implements Controller {
	private View view;
	private Logic logic;
	private NetWorker netWorker;

	public GameController() {
		netWorker = new NetWorker();

		view = new GameView(D.SCREEN_WIDTH, D.SCREEN_HEIGHT);
		logic = new GameLogic(D.SCREEN_WIDTH, D.SCREEN_HEIGHT, view, netWorker);
		((GameLogic) logic).setController(this);
		((GameView) view).setLogic(logic);
	}

	public boolean startNetworking(boolean isServer) {
		if (isServer) netWorker.accept(D.PORT);
		else netWorker.connect(D.HOST, D.PORT);
		return netWorker.isConnected();
	}

	@Override
	public View getView() {
		return view;
	}

	public void free() {
		netWorker.disconnect();
		netWorker.removeAll();

		view.free();
		logic.free();
		// logic.stop();
	}

}
