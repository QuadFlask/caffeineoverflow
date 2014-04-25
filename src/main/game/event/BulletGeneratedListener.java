package main.game.event;

import java.io.Serializable;
import java.util.List;

import main.game.obj.property.Shootable;

public interface BulletGeneratedListener extends Serializable {

	public void onGenerated(List<Shootable> shootables);

}
