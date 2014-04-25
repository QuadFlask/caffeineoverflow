package main.game.obj.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.game.obj.factory.wave.Wave;
import main.game.obj.factory.wave.Wave1;

public class WaveFactory {
	private static List<Wave> waveList;
	private static Iterator<Wave> iterator;

	static {
		waveList = new ArrayList<Wave>();
		waveList.add(new Wave1());
		
		iterator = waveList.iterator();
	}

	public static Wave nextWave() {
		if (hasNext()) return iterator.next();
		else return null;
	}

	public static boolean hasNext() {
		return iterator.hasNext();
	}
	
	public static void reset(){
		iterator = waveList.iterator();
	}
}
