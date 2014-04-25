package main.game.graphics.effects;

import aurelienribon.tweenengine.TweenAccessor;
import main.flask.utils.Vec2DF;

public class MenuAccessor implements TweenAccessor<MenuBox> {
	public static final int POS_TL = 0;
	public static final int POS_TR = 1;
	public static final int POS_BR = 2;
	public static final int POS_BL = 3;
	public static final int POS_ALL = 4;

	@Override
	public int getValues(MenuBox target, int tweenType, float[] returnValues) {
		if (tweenType == POS_ALL) {
			Vec2DF[] v = target.getPosition();
			returnValues[0] = v[POS_TL].x;
			returnValues[1] = v[POS_TL].y;
			returnValues[2] = v[POS_TR].x;
			returnValues[3] = v[POS_TR].y;
			returnValues[4] = v[POS_BR].x;
			returnValues[5] = v[POS_BR].y;
			returnValues[6] = v[POS_BL].x;
			returnValues[7] = v[POS_BL].y;
			return 8;
		} else {
			Vec2DF v = target.getPosition()[tweenType];
			returnValues[0] = v.x;
			returnValues[1] = v.y;
			return 2;
		}
	}

	@Override
	public void setValues(MenuBox target, int tweenType, float[] newValues) {
		if (tweenType == POS_ALL) {
			Vec2DF[] v = target.getPosition();
			v[POS_TL].x = newValues[0];
			v[POS_TL].y = newValues[1];
			v[POS_TR].x = newValues[2];
			v[POS_TR].y = newValues[3];
			v[POS_BR].x = newValues[4];
			v[POS_BR].y = newValues[5];
			v[POS_BL].x = newValues[6];
			v[POS_BL].y = newValues[7];
		} else {
			Vec2DF v = target.getPosition()[tweenType];
			v.x = newValues[0];
			v.y = newValues[1];
		}
	}
}
