package life;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import life.display.Display;
import life.gfx.Assets;
import life.input.KeyManager;
import life.input.MouseManager;
import life.manager.LifeManager;

public class Life implements Runnable {

	private String title;
	private int width, height;

	private Display display;

	private boolean running = false;
	private Thread thread;

	private Graphics g;
	private BufferStrategy bs;

	private LifeManager manager;
	private Handler handler;

	private KeyManager keyManager;

	private Color color, oppColor;

	private int ticksToBePrinted;
	
	private boolean randStart;
	
	private MouseManager mouseManager;

	public Life(String title, int width, int height, Color color, Color oppColor, boolean randStart) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.color = color;
		this.oppColor = oppColor;
		this.ticksToBePrinted = 0;
		this.randStart = randStart;
	}

	private void init() {
		display = new Display(title, width, height);

		handler = new Handler(this);
		
		Assets.init();

		manager = new LifeManager(handler, color, randStart);
		manager.setCellColor(color);

		keyManager = new KeyManager();
		mouseManager = new MouseManager();
		
		keyManager.init();
		display.getFrame().addKeyListener(keyManager);
		display.getCanvas().addKeyListener(keyManager);
		
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
	}

	@Override
	public void run() {
		init();

		int fps = 100;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				tick();
				ticks++;
				delta--;
			}

			if (timer >= 1000000000) {
				if (!handler.getKeyManager().details) {
					this.ticksToBePrinted = ticks;
				}
				ticks = 0;
				timer = 0;
			}
		}

		stop();
	}

	private void update() {
		keyManager.tick();
		manager.tick();
	}

	private void tick() {
		// Update everything
		update();
		// Render everything
		render();
	}

	private void render() {
		bs = display.getCanvas().getBufferStrategy();

		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();

		// clear
		g.clearRect(0, 0, width, height);
		g.setColor(oppColor);
		g.fillRect(0, 0, width, height);
		// draw
		if (handler.getKeyManager().details) {
			this.drawDetails(g);
		} else {
			manager.render(g);
		}
		// show
		bs.show();
		g.dispose();
	}

	private void drawDetails(Graphics g) {
		g.setColor(oppColor);
		g.fillRect(0, 0, width, height);
		g.setColor(color);
		g.setFont(new Font("SansSerif", Font.PLAIN, 18));
		String text = "FPS : "+this.ticksToBePrinted;
		g.drawString(text, 0, 50);
		text = "Current X : "+manager.getxOffset();
		g.drawString(text, 0, 100);
		text = "Current Y : "+manager.getyOffset();
		g.drawString(text, 0, 150);
		text = "Alive Cells : "+manager.getAliveCells();
		g.drawString(text, 0, 200);
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public void setKeyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
	}

	public LifeManager getManager() {
		return manager;
	}

	public void setManager(LifeManager manager) {
		this.manager = manager;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public MouseManager getMouseManager() {
		return mouseManager;
	}

	public void setMouseManager(MouseManager mouseManager) {
		this.mouseManager = mouseManager;
	}

}
