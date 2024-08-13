package life;

import life.input.KeyManager;
import life.input.MouseManager;

public class Handler {

	private Life life;

	public Handler(Life life) {
		this.life = life;
	}

	public int getWidth() {
		return life.getWidth();
	}

	public int getHeight() {
		return life.getHeight();
	}

	public KeyManager getKeyManager() {
		return life.getKeyManager();
	}

	public Thread getThread() {
		return life.getThread();
	}
	
	public MouseManager getMouseManager() {
		return life.getMouseManager();
	}

}
