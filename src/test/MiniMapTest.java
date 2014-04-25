package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import main.game.graphics.Sprite;
import main.game.view.ui.MiniMap;

import org.junit.Before;
import org.junit.Test;

public class MiniMapTest {
	private ArrayList<Sprite> list;
	private ArrayList<Integer> hash;
	private MiniMap m;
	
	@Before
	public void init() {
		m = new MiniMap(100, 100);
		list = new ArrayList<>();
		hash = new ArrayList<>();
		int count = 5;
		Sprite temp;
		for (int i = 0; i < count; i++) {
			temp = new Sprite(null);
			list.add(temp);
			hash.add(temp.hashCode());
			m.addSprite(temp);
		}
	}
	
	@Test
	public void testToFront() throws Exception {
		Sprite sprite = list.get(0);
		m.toFront(sprite);
		assertEquals(sprite, m.getSpriteAt(m.size() - 1));
		
		sprite = list.get(1);
		m.toFront(sprite);
		assertEquals(sprite, m.getSpriteAt(m.size() - 1));
		
		sprite = list.get(list.size() - 1);
		m.toFront(sprite);
		assertEquals(sprite, m.getSpriteAt(m.size() - 1));
	}
	
	@Test
	public void testToBackward() throws Exception {
		Sprite sprite = list.get(0);
		m.toBackward(sprite);
		assertEquals(sprite, m.getSpriteAt(0));
		
		sprite = list.get(1);
		m.toBackward(sprite);
		assertEquals(sprite, m.getSpriteAt(0));
		
		sprite = list.get(list.size() - 1);
		m.toBackward(sprite);
		assertEquals(sprite, m.getSpriteAt(0));
	}
	
	@Test
	public void testSwapIndex() throws Exception {
		Sprite spriteA = list.get(0);
		Sprite spriteB = list.get(1);
		m.swapIndex(0, 1);
		assertEquals(spriteB, m.getSpriteAt(0));
		assertEquals(spriteA, m.getSpriteAt(1));
	}
}
