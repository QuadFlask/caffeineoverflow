package main.game.view;

import static main.flask.net.SimpleLogger.log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;

import main.flask.imageprocessing.BlurFilter;
import main.flask.imageprocessing.IntegerBlender;
import main.flask.utils.Vec2D;
import main.game.common.D;
import main.game.controller.MenuController;
import main.game.event.OnChangeEvent;
import main.game.event.OnChangeEvent.CONTROLLER;
import main.game.event.TickEvent;
import main.game.graphics.GraphicsDrawer;
import main.game.graphics.effect.factory.Jitter;
import main.game.graphics.effects.Grid;
import main.game.logic.Logic;
import main.game.resource.ImageAsset;
import main.game.resource.ImageAsset.IMAGE_ID;
import main.game.view.ui.DrawableComponent;
import main.game.windows.KeyPressingCallbackAdapter;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Expo;

public class MenuView extends View {

	boolean isMaximized = false;
	private Logic logic;

	private BlurFilter blurFilter = new BlurFilter(4, 4, BlurFilter.TYPE_LINEAR);
	private Kernel kernel = blurFilter.getMatrixAsKernel(1.5f);
	private ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

	private BufferedImage logo, upperBuffer, blurBuffer, blurBuffer_temp;
	private Graphics2D g_upper, g_blur, g_blur_temp;
	private Grid grid;

	private int k = 4;

	public MenuView(int width, int height) {
		super(width, height);
		graphics2D.setColor(D.FONT_DEBUG);
		graphics2D.setFont(D.FONT_CONSOLAS);

		upperBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);// BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		blurBuffer = new BufferedImage(width / k, height / k, BufferedImage.TYPE_INT_ARGB);// BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		blurBuffer_temp = new BufferedImage(width / k, height / k, BufferedImage.TYPE_INT_ARGB);// BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

		g_upper = (Graphics2D) upperBuffer.getGraphics();
		g_blur = (Graphics2D) blurBuffer.getGraphics();
		g_blur_temp = (Graphics2D) blurBuffer_temp.getGraphics();

		g_upper.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g_upper.setBackground(TRANSPARENT);
		g_blur.setBackground(TRANSPARENT);
		g_blur_temp.setBackground(TRANSPARENT);

		graphics2D.setBackground(Color.black);

		grid = new Grid(width, height, 2);
		log("MenuView");
	}

	@Override
	public void init() {
		logo = ImageAsset.getImage(IMAGE_ID.LOGO);
		// keyManager.addCallback(new KeyPressingCallbackAdapter() {
		// @Override
		// public void onPressing(int keyCode) {
		// if (!isMaximized) {
		// menuList.get(0).maximize(tweenManager, new TweenCallback() {
		// @Override
		// public void onEvent(int type, BaseTween<?> source) {
		// isMaximized = true;
		// }
		// });
		// } else {
		// menuList.get(0).minimize(tweenManager, new TweenCallback() {
		// @Override
		// public void onEvent(int type, BaseTween<?> source) {
		// isMaximized = false;
		// }
		// });
		// }
		// }
		// });
		// ////
		/*
		 * ballList2[0] = root[0]; ballList2[1] = rank; ballList2[2] = credit;
		 * ballList2[3] = play[0]; ballList2[4] = make; ballList2[5] = connect;
		 * 
		 * 0 -> 1, 2, 3 1 -> 0 2 -> 0 3 -> 0, 4, 5 4 -> 3 5 -> 3
		 */
		//

		//
		keyManager.reset();
		addKeyCallback();

		log("init");
	}

	public void addKeyCallback() {
		keyManager.addCallback(keyPressingCallbackAdapter);
	}

	public void removeKeyCallback() {
		keyManager.removeCallback(keyPressingCallbackAdapter);
	}

	KeyPressingCallbackAdapter keyPressingCallbackAdapter = new KeyPressingCallbackAdapter() {
		private boolean dispatched = false;

		@Override
		public void onPressing(List<Integer> keyCodes) {
			if (!isPressed) {
				if (keyCodes.contains(KeyEvent.VK_LEFT)) ball.select(getNextSelection(KeyEvent.VK_LEFT));
				else if (keyCodes.contains(KeyEvent.VK_RIGHT)) ball.select(getNextSelection(KeyEvent.VK_RIGHT));
				else if (keyCodes.contains(KeyEvent.VK_UP)) ball.select(getNextSelection(KeyEvent.VK_UP));
				else if (keyCodes.contains(KeyEvent.VK_DOWN)) ball.select(getNextSelection(KeyEvent.VK_DOWN));
				else if (keyCodes.contains(KeyEvent.VK_ENTER)) {
					if (currentSelected == 1 && !dispatched) {
						menuController.dispatch(new OnChangeEvent(CONTROLLER.RANK, 1));
						dispatched = true;
					} else if (currentSelected == 3) ball.select(getNextSelection(KeyEvent.VK_UP));
					else if (currentSelected == 4) menuController.dispatch(new OnChangeEvent(CONTROLLER.GAME, 1));
					else if (currentSelected == 5) menuController.dispatch(new OnChangeEvent(CONTROLLER.GAME, 2));
				} else {
					return;
				}

				isPressed = true;
				// log("menu onPressing");
			}
		}

		@Override
		public void onReleased(int keyCode) {
			super.onReleased(keyCode);
			isPressed = false;
			log("menu onReleased");
		}
	};

	public void removeKeyManager() {
		removeListener();
		log("menu removeKeyManager");
	}

	public void removeListener() {
		this.keyManager.removeCallback(keyPressingCallbackAdapter);
		log("menu removeListener");
	}

	private int getNextSelection(int keycode) {
		log("menu getNextSelection");
		if (currentSelected == 0) {
			if (keycode == KeyEvent.VK_LEFT) currentSelected = 1;
			if (keycode == KeyEvent.VK_RIGHT) currentSelected = 3;
			if (keycode == KeyEvent.VK_DOWN) currentSelected = 2;
		} else if (currentSelected == 1) {
			if (keycode == KeyEvent.VK_RIGHT) currentSelected = 0;
		} else if (currentSelected == 2) {
			if (keycode == KeyEvent.VK_RIGHT || keycode == KeyEvent.VK_UP) currentSelected = 0;
		} else if (currentSelected == 3) {
			if (keycode == KeyEvent.VK_LEFT) currentSelected = 0;
			if (keycode == KeyEvent.VK_RIGHT) currentSelected = 5;
			if (keycode == KeyEvent.VK_UP) currentSelected = 4;
		} else if (currentSelected == 4) {
			if (keycode == KeyEvent.VK_LEFT) currentSelected = 3;
			if (keycode == KeyEvent.VK_DOWN) currentSelected = 3;
		} else if (currentSelected == 5) {
			if (keycode == KeyEvent.VK_LEFT) currentSelected = 3;
		}

		return currentSelected;
	}

	private int currentSelected = 0;
	private boolean isPressed = false;

	@Override
	public void onTick(TickEvent tickEvent) {
		super.onTick(tickEvent);

		draw();

		showFPS(tickEvent);

		log("menu onTick");
	}

	private void draw() {
		// log("menu draw");
		graphics2D.clearRect(0, 0, width, height);
		g_upper.clearRect(0, 0, width, height);

		g_upper.drawImage(logo, 0, 50 + 32, null);
		drawings(g_upper);

		g_blur_temp.drawImage(upperBuffer, 0, 0, width / k, height / k, null);
		op.filter(blurBuffer_temp, blurBuffer);
		IntegerBlender.apply(blurBuffer_temp, 0.95f);

		graphics2D.drawImage(grid.getBufferedImage(), 0, 0, width, height, null);
		graphics2D.drawImage(blurBuffer, 0, 0, width, height, null);
		graphics2D.drawImage(upperBuffer, 0, 0, width, height, null);

		drawDrawableComponents();
	}

	ViscosityMap ball = new ViscosityMap();

	private void drawings(Graphics2D g) {

		// ball.draw(g);
		ball.fill(g);

	}

	List<DrawableComponent> removeList = new ArrayList<DrawableComponent>();

	private void drawDrawableComponents() {
		synchronized (drawables) {
			for (DrawableComponent d : new ArrayList<DrawableComponent>(drawables)) {
				if (!d.draw(graphics2D)) removeList.add(d);
			}
			drawables.removeAll(removeList);
		}
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
		log("menu setLogic");
	}

	public static class ViscosityMap {
		private ViscosityBall[] root;
		private ViscosityBall rank;
		private ViscosityBall credit;

		private ViscosityBall[] play;
		private ViscosityBall make;
		private ViscosityBall connect;

		private List<ViscosityBall> ballList = new ArrayList<ViscosityBall>();
		private ViscosityBall[] ballList2 = new ViscosityBall[6];

		private Path2D[] links = new Path2D[5];
		public static final Color coffeeColor = new Color(0x6f3919);

		private double r = 36;
		private double y = 300;
		private double dTheta = 0.9f;
		private double thickness = 0;

		private double diverse = 25;

		public ViscosityMap() {
			double dy = 120;
			rank = new ViscosityBall(new Vec2D(Jitter.range(100, diverse), Jitter.range(y, diverse)), 0, dTheta, r, thickness);
			Vec2D p = new Vec2D(Jitter.range(220, diverse), Jitter.range(y, diverse));
			root = new ViscosityBall[] {//
			new ViscosityBall(p, 0, dTheta, r, thickness), //
					new ViscosityBall(p, 0, dTheta, r, thickness), //
					new ViscosityBall(p, 0, dTheta, r, thickness), //
			};
			make = new ViscosityBall(new Vec2D(Jitter.range(340, diverse), Jitter.range(y, diverse)), 0, dTheta, r, thickness);

			credit = new ViscosityBall(new Vec2D(Jitter.range(160, diverse), Jitter.range(y, diverse) + dy), 0, dTheta, r, thickness);
			p = new Vec2D(Jitter.range(280, diverse), Jitter.range(y, diverse) + dy);
			play = new ViscosityBall[] {//
			new ViscosityBall(p, 0, dTheta, r, thickness), //
					new ViscosityBall(p, 0, dTheta, r, thickness), //
					new ViscosityBall(p, 0, dTheta, r, thickness), //
			};
			connect = new ViscosityBall(new Vec2D(Jitter.range(400, diverse), Jitter.range(y, diverse) + dy), 0, dTheta, r, thickness);

			buildPaths();
			root[0].setSelected(true, tweenManager);
			// log("ViscosityMap ViscosityMap");
		}

		public void buildPaths() {
			// log("ViscosityMap buildPaths");
			root[0].setTarget(rank);
			root[1].setTarget(play[1]);
			root[2].setTarget(credit);
			rank.setTarget(root[0]);

			credit.setTarget(root[0]);
			play[1].setTarget(root[1]);
			play[0].setTarget(make);
			play[2].setTarget(connect);

			make.setTarget(play[0]);
			connect.setTarget(play[2]);

			links[0] = makePath(root[0], rank);
			links[1] = makePath(root[1], play[1]);
			links[2] = makePath(root[2], credit);

			links[3] = makePath(play[0], make);
			links[4] = makePath(play[2], connect);

			ballList.add(root[1]);
			ballList.add(root[2]);
			ballList.add(root[0]);
			ballList.add(play[1]);
			ballList.add(play[2]);
			ballList.add(play[0]);

			ballList.add(rank);
			ballList.add(make);
			ballList.add(connect);
			ballList.add(credit);

			ballList2[0] = root[0];
			ballList2[1] = rank;
			ballList2[2] = credit;
			ballList2[3] = play[0];
			ballList2[4] = make;
			ballList2[5] = connect;

		}

		private int currentSelected = 0;

		private void select(int selected) {
			// log("ViscosityMap  select");
			ballList2[currentSelected].setSelected(false, tweenManager);
			currentSelected = selected;
			ballList2[currentSelected].setSelected(true, tweenManager);
		}

		private Path2D makePath(ViscosityBall from, ViscosityBall to) {
			// log("ViscosityMap  makePath");
			Path2D path = new GeneralPath();
			Vec2D[] tipFrom = from.getTip();
			Vec2D[] tipTo = to.getTip();
			path.moveTo(tipFrom[0].x, tipFrom[0].y);
			path.lineTo(tipFrom[1].x, tipFrom[1].y);
			path.lineTo(tipTo[0].x, tipTo[0].y);
			path.lineTo(tipTo[1].x, tipTo[1].y);
			path.closePath();
			return path;
		}

		public void draw(Graphics2D g) {
			// log("ViscosityMap  draw");
			g.setColor(coffeeColor);
			for (int i = 0; i < 5; i++)
				g.draw(links[i]);
			for (ViscosityBall ball : ballList)
				ball.draw(g);
		}

		public void fill(Graphics2D g) {
			// log("ViscosityMap  fill");
			g.setColor(coffeeColor);
			for (int i = 0; i < 5; i++) {
				g.draw(links[i]);
				g.fill(links[i]);
			}
			for (ViscosityBall ball : ballList) {
				ball.draw(g);
				ball.fill(g);
			}
			drawTitle(g, "root", Color.orange, 2, root[0]);
			drawTitle(g, "rank", Color.red, 2, rank);
			drawTitle(g, "credit", Color.green, 2, credit);
			drawTitle(g, "play", Color.blue, 2, play[0]);
			drawTitle(g, "[make]", Color.cyan, 1, make);
			drawTitle(g, "[connect]", Color.magenta, 1, connect);
		}

		private void drawTitle(Graphics2D g, String title, Color color, int boxSize, ViscosityBall ball) {
			GraphicsDrawer.drawBitmapTextCenter(g, title, boxSize, 0, color, (int) ball.position.x, (int) (ball.position.y - 1.75 * boxSize));
		}
	}

	public static class ViscosityBall {
		static {
			Tween.registerAccessor(ViscosityBall.class, new ViscosityBallAccessor());
		}

		static class ViscosityBallAccessor implements TweenAccessor<ViscosityBall> {
			@Override
			public int getValues(ViscosityBall target, int tweenType, float[] returnValues) {
				returnValues[0] = target.tweenInp;
				return 1;
			}

			@Override
			public void setValues(ViscosityBall target, int tweenType, float[] newValues) {
				target.tweenInp = newValues[0];
			}
		}

		private Vec2D position;
		private Vec2D a0, b0, c0;
		private Vec2D a1, b1, c1;
		private double theta = 0;
		private double dTheta = 0;
		private double r = 6;
		private double t = 3;
		private double selectorMaxR = r - 2;
		float tweenInp = 0;
		private Shape s;
		private Ellipse2D.Double selector;
		private Path2D path;
		private Color selectorColor = new Color(0xa67c52);

		public ViscosityBall(Vec2D postion, double theta, double dTheta, double r, double thickness) {
			setFactors(postion, theta, dTheta, r, thickness / Math.sin(dTheta));
		}

		public void setFactors(Vec2D position, double theta, double dTheta, double r, double thickness) {
			this.position = position;
			this.theta = theta;
			this.dTheta = dTheta;
			this.r = r;
			this.t = thickness;
			this.selectorMaxR = r * 2 - 6;

			s = new Ellipse2D.Double(position.x - r, position.y - r, r * 2, r * 2);
			selector = new Ellipse2D.Double(position.x - r, position.y - r, 0, 0);
			path = new GeneralPath();

			calc();
			makePath();
		}

		public void setTarget(ViscosityBall target) {
			setTarget(target.position);
		}

		public void setTarget(Vec2D target) {
			Vec2D diff = position.getSub(target);
			this.theta = Math.atan2(diff.y, diff.x) + Math.PI;
			calc();
			makePath();
		}

		private void calc() {
			a0 = new Vec2D(r, 0);
			a0.rotate(dTheta);

			double l = (r - t) * Math.tan(dTheta);
			double d = Math.PI / 2 - dTheta;

			b0 = new Vec2D(l);
			b0.rotate(-d);
			b0.add(a0);

			c0 = new Vec2D(l);
			c0.add(b0);

			// ///////////////

			a1 = new Vec2D(r, 0);
			a1.rotate(-dTheta);

			b1 = new Vec2D(l);
			b1.rotate(d);
			b1.add(a1);

			c1 = new Vec2D(l);
			c1.add(b1);

			// ////////////////

			a0.rotate(theta);
			b0.rotate(theta);
			c0.rotate(theta);

			a1.rotate(theta);
			b1.rotate(theta);
			c1.rotate(theta);

			a0.add(position);
			b0.add(position);
			c0.add(position);
			a1.add(position);
			b1.add(position);
			c1.add(position);
		}

		public Vec2D[] getTip() {
			return new Vec2D[] { c0, c1 };
		}

		public Path2D makePath() {
			path.reset();

			path.moveTo(position.x, position.y);
			path.lineTo(a0.x, a0.y);
			path.curveTo(a0.x, a0.y, b0.x, b0.y, c0.x, c0.y);

			path.lineTo(c1.x, c1.y);

			path.curveTo(c1.x, c1.y, b1.x, b1.y, a1.x, a1.y);
			path.lineTo(position.x, position.y);

			path.closePath();
			return path;
		}

		private float duration = 1;
		private TweenEquation tweenEquation = Elastic.OUT;
		private Tween curTween;

		public void setSelected(boolean isSelected, TweenManager manager) {
			manager.killTarget(this);
			if (isSelected) Tween.to(this, 0, duration).target(1).ease(tweenEquation).start(manager);
			else Tween.to(this, 0, duration / 4).target(0).ease(Expo.IN).start(manager);
		}

		public void update() {
			// log("ViscosityBall  update");
			selector.width = selector.height = tweenInp * selectorMaxR;
			selector.x = position.x - selector.width / 2d;
			selector.y = position.y - selector.height / 2d;
		}

		public void draw(Graphics2D g) {
			// log("ViscosityBall  draw");
			update();
			g.setColor(ViscosityMap.coffeeColor);
			g.draw(path);
			g.draw(s);
			g.setColor(selectorColor);
			g.draw(selector);
		}

		public void fill(Graphics2D g) {
			// log("ViscosityBall  fill");
			update();
			g.setColor(ViscosityMap.coffeeColor);
			g.fill(path);
			g.fill(s);
			g.setColor(selectorColor);
			g.fill(selector);
		}
	}

	MenuController menuController;

	public void setController(MenuController menuController) {
		// log("ViscosityBall  setController");
		this.menuController = menuController;
	}

	@Override
	public void free() {
		super.free();
	}

}

//
// public static class BranchTree {
// private BranchNode root;
// private BranchNode play;
// private BranchNode rank;
// private BranchNode credit;
// private BranchNode connect;
// private BranchNode make;
//
// private BranchLine root2play;
// private BranchLine root2rank;
// private BranchLine root2credit;
// private BranchLine play2connect;
// private BranchLine play2make;
//
// public BranchTree() {
// root = new BranchNode("", 100, 300);
// play = new BranchNode("", 250, 300);
// rank = new BranchNode("", 250, 400);
// credit = new BranchNode("", 250, 500);
//
// connect = new BranchNode("", 400, 300);
// make = new BranchNode("", 400, 400);
//
// root2play = new BranchLine(root, play, Color.red);
// root2rank = new BranchLine(root, rank, Color.green);
// root2credit = new BranchLine(root, credit, Color.magenta);
//
// play2connect = new BranchLine(play, connect, Color.red);
// play2make = new BranchLine(play, make, Color.yellow);
// }
//
// public void draw(Graphics2D g) {
// root2play.draw(g);
// root2rank.draw(g);
// root2credit.draw(g);
// play2connect.draw(g);
// play2make.draw(g);
//
// root.draw(g);
// play.draw(g);
// rank.draw(g);
// credit.draw(g);
// connect.draw(g);
// make.draw(g);
// }
//
// }
//
// public static class BranchNode {
// private Point2D.Float node = new Point2D.Float();
// private Ellipse2D.Float shape;
// private Color innerColor = new Color(0x222222);
// private Color outerColor = new Color(0xffffff);
// private Color selectedColor = new Color(0xccff00);
// private Stroke stroke;
//
// private String nodeName;
//
// private int fontSize = 10;
// private float fixedSize = 32;
// private float currentSize = fixedSize;
//
// private boolean isSelected = false;
//
// public BranchNode(String name, float x, float y) {
// this.nodeName = name;
// node.x = x;
// node.y = y;
// shape = new Ellipse2D.Float(node.x, node.y, currentSize, currentSize);
// updateShape();
// stroke = new BasicStroke(fixedSize / 6);
// }
//
// private void updateShape() {
// shape.x = node.x - currentSize / 2;
// shape.y = node.y - currentSize / 2;
// }
//
// public void draw(Graphics2D g) {
// if (isSelected) g.setColor(selectedColor);
// else g.setColor(innerColor);
// g.fill(shape);
//
// g.setStroke(stroke);
// g.setColor(outerColor);
// g.draw(shape);
// }
//
// public void setSelected(boolean isSelected) {
// this.isSelected = isSelected;
// }
//
// public float x() {
// return node.x;
// }
//
// public float y() {
// return node.y;
// }
//
// }
//
// public static class BranchLine {
// private Point2D anchor1;
// private Point2D control;
// private Point2D anchor2;
// private Stroke stroke;
// private QuadCurve2D.Float shape;
// private Color color = Color.red;
// private float amount = 0.5f;
//
// public BranchLine(BranchNode node1, BranchNode node2, Color color) {
// anchor1 = new Point2D.Float(node1.x(), node1.y());
// anchor2 = new Point2D.Float(node2.x(), node2.y());
// control = new Point2D.Float((node1.x() + node2.x()) / 2, +node2.y());
// shape = new QuadCurve2D.Float();
// shape.setCurve(anchor1, control, anchor2);
// stroke = new BasicStroke(8);
// this.color = color;
// }
//
// public void update() {
//
// }
//
// public void draw(Graphics2D g) {
// g.setStroke(stroke);
// g.setColor(color);
// g.draw(shape);
// }
//
// public Shape getShape() {
// return shape;
// }
// }