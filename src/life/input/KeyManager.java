package life.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
	
	private boolean[] keys;
	
	public boolean step, right, left, down, up, center, origin, details, clear, next, random;
	
	public KeyManager() {
		keys = new boolean[256];
	}
	
	public void tick() {
		step = keys[KeyEvent.VK_SPACE];
		right = keys[KeyEvent.VK_D];
		left = keys[KeyEvent.VK_A];
		up = keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_S];
		center = keys[KeyEvent.VK_O];
		origin = keys[KeyEvent.VK_I];
		details = keys[KeyEvent.VK_ESCAPE];
		clear = keys[KeyEvent.VK_C];
		next = keys[KeyEvent.VK_N];
		
		random = keys[KeyEvent.VK_R];
	}
	
	public void init() {
		for (int i=0; i<keys.length; i++) {
			keys[i] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ESCAPE))
			keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			keys[e.getKeyCode()] = !keys[e.getKeyCode()];
		} else {
			keys[e.getKeyCode()] = false;
		}
	}

}
