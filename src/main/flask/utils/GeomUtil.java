package main.flask.utils;

public class GeomUtil {
	/**
	 * 모서리에 붙는 기능 from 플생
	 * http://cafe.naver.com/flashdev.cafe?iframe_url=/ArticleRead.nhn%3Fclubid=10019103%26menuid=119%26boardtype=%26page=%26userDisplay=%26articleid=32726
	 * var w:Number = stage.stageWidth/2;
	 * var h:Number = stage.stageHeight/2;
	 * var a:Number = Math.atan2(h, w);
	 * <p/>
	 * stage.addEventListener(MouseEvent.MOUSE_MOVE, stage_mouseMove);
	 * <p/>
	 * function stage_mouseMove(e:MouseEvent):void {
	 * <p/>
	 * var ang:Number = Math.atan2(mouseY - h, mouseX - w);
	 * var aabs:Number = Math.abs(ang);
	 * var bln:Boolean = a < aabs && Math.PI - a > aabs;
	 * var cr:Number = (bln) ? Math.abs(h / Math.cos(ang - Math.PI/2)) : Math.abs(w / Math.cos(ang));
	 * <p/>
	 * mc.x = cr * Math.cos(ang) + w;
	 * mc.y = cr * Math.sin(ang) + h;
	 * mc.rotation = ang * 180 / Math.PI;
	 * <p/>
	 * trace(a);
	 * <p/>
	 * }
	 */
	public static final double[] snapTo(double w, double h, double angle) {
		double a = Math.atan2(h, w);
		double aabs = Math.abs(angle);
		boolean bln = a < aabs && Math.PI - a > aabs;
		double cr = (bln) ? Math.abs(h / Math.cos(angle - Math.PI / 2)) : Math.abs(w / Math.cos(angle));
		return new double[]{cr * Math.cos(angle) + w, cr * Math.sin(angle) + h};
	}
}
