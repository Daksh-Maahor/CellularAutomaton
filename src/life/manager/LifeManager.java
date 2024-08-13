package life.manager;

import java.awt.Color;
import java.awt.Graphics;

import life.Handler;
import life.gfx.Assets;

public class LifeManager {

	// helpful constants

	public static final boolean ALIVE_CELL = true;
	public static final boolean DEAD_CELL = false;

	public static final int little = 1 /* Least Recommended */, large = 10, medium1 = 2 /* Most Recommended */,
			medium2 = 4;

	public static int scale = 10;

	public static final int extraSpaceTimes = 3;

	public static final int scrollSpeed = 2;

	public static final double speed = 0.1;

	// class

	private int[] offsets = { -1, 0, 1 };

	private boolean[][] cells, next;
	private Handler handler;

	private Color cellColor;

	private int width, height;

	private int xOffset, yOffset, tick;

	private boolean randStart;
	
	private int aliveCells;

	public LifeManager(Handler handler, Color cellColor, boolean randStart) {
		width = (handler.getWidth() / scale) * (extraSpaceTimes + 1);
		height = (handler.getHeight() / scale) * (extraSpaceTimes + 1);
		xOffset = 0;
		yOffset = 0;
		this.handler = handler;
		this.aliveCells = 0;
		cells = new boolean[width][height];
		next = new boolean[width][height];
		this.cellColor = cellColor;
		tick = 0;
		this.randStart = randStart;
		init();
	}

	private void init() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = DEAD_CELL;
			}
		}

		if (this.randStart)
			randStart();
		else
			manualStart();
	}

	public int getSpecialNum(int num) {
		int numTerms = 0;
		int sum = 0;

		while (!(num <= 1)) {
			if (num % 2 == 0) {
				sum = num / 2;
			} else {
				sum = num * 3 + 1;
			}

			num = sum;
			numTerms++;
		}

		return numTerms % 2;
	}

	private void randStart() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				cells[x][y] = DEAD_CELL;
			}
		}
		double onChance = 0.2;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double chance = Math.random();
				if (chance < onChance)
					this.setAlive(x, y);
			}
		}
	}

	private void manualStart() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.setDead(x, y);
			}
		}
	}

	public void update() {
		if (tick >= 1 / speed) {
			// set next to current
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					next[x][y] = cells[x][y];
				}
			}
			// Loop through all cells
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {

					boolean currentCell = cells[x][y];
					int aliveNeighbours = 0;

					for (int dx : offsets) {
						int tx = x + dx;
						for (int dy : offsets) {
							if (dx == 0 && dy == 0) {
								continue;
							}

							int ty = y + dy;

							if (tx < 0 || ty < 0) {
								continue;
							}

							if (tx >= width || ty >= height) {
								continue;
							}

							aliveNeighbours += cells[tx][ty] ? 1 : 0;
						}
					}

					if (currentCell && (aliveNeighbours < 2 || aliveNeighbours > 3)) {
						next[x][y] = !currentCell;
					}
					if (!currentCell && aliveNeighbours == 3) {
						next[x][y] = !currentCell;
					}

				}
			}

			// set current to next
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					cells[x][y] = next[x][y];
				}
			}
			tick = 0;
		}
		tick++;
	}
	
	private int countNumCells() {
		int numCells = 0;
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				numCells += cells[x][y] ? 1 : 0;
			}
		}
		
		return numCells;
	}

	public void tick() {
		this.aliveCells = countNumCells();
		if (handler.getKeyManager().step && !handler.getKeyManager().details) {
			update();
		}
		
		if (!handler.getKeyManager().step && !handler.getKeyManager().details) {
			if (handler.getKeyManager().random) {
				randStart();
			}
		}
		
		if (!handler.getKeyManager().details) {
			if (handler.getKeyManager().right) {
				if (xOffset + handler.getWidth() / scale + scrollSpeed < width) {
					xOffset += scrollSpeed;
				} else if (xOffset + handler.getWidth() / scale + 1 < width) {
					xOffset += 1;
				}
			}
			if (handler.getKeyManager().left) {
				if (xOffset - scrollSpeed >= 0) {
					xOffset -= scrollSpeed;
				} else if (xOffset - 1 >= 0) {
					xOffset -= 1;
				}
			}
			if (handler.getKeyManager().down) {
				if (yOffset + handler.getHeight() / scale + scrollSpeed < height) {
					yOffset += scrollSpeed;
				} else if (yOffset + handler.getHeight() / scale + 1 < height) {
					yOffset += 1;
				}
			}
			if (handler.getKeyManager().up) {
				if (yOffset - scrollSpeed >= 0) {
					yOffset -= scrollSpeed;
				} else if (yOffset - 1 >= 0) {
					yOffset -= 1;
				}
			}
			if (handler.getKeyManager().center) {
				xOffset = width / 2 - handler.getWidth() / (2 * scale);
				yOffset = height / 2 - handler.getHeight() / (2 * scale);
			}
			if (handler.getKeyManager().origin) {
				xOffset = 0;
				yOffset = 0;
			}
			if (!handler.getKeyManager().step) {
				input();
				if (handler.getKeyManager().clear) {
					clearCells();
				}
			}
		}
	}

	private void clearCells() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.cells[x][y] = DEAD_CELL;
			}
		}
	}

	private void input() {
		if (handler.getMouseManager().isLeft()) {
			int x = handler.getMouseManager().getMouseX() / scale + this.xOffset;
			int y = handler.getMouseManager().getMouseY() / scale + this.yOffset;

			if (!(x < 0 || y < 0 || x > cells.length || y > cells[0].length)) {
				this.setAlive(x, y);
			}
		}
		if (handler.getMouseManager().isRight()) {
			int x = handler.getMouseManager().getMouseX() / scale + this.xOffset;
			int y = handler.getMouseManager().getMouseY() / scale + this.yOffset;

			if (!(x < 0 || y < 0 || x > cells.length || y > cells[0].length)) {
				this.setDead(x, y);
			}
		}
	}

	public void render(Graphics g) {
		int xStart = Math.max(0, xOffset);
		int yStart = Math.max(0, yOffset);
		g.setColor(cellColor);
		for (int y = yStart; y < yStart + handler.getHeight() / scale; y++) {
			for (int x = xStart; x < xStart + handler.getWidth() / scale; x++) {
				if (cells[x][y] == ALIVE_CELL) {
//					g.fillRect((x - xStart) * scale, (y - yStart) * scale, scale, scale);
					g.drawImage(Assets.pixel, (x - xStart) * scale, (y - yStart) * scale, scale, scale, null);
				}
			}
		}
	}

	private void setAlive(int x, int y) {
		cells[x][y] = ALIVE_CELL;
	}

	private void setDead(int x, int y) {
		cells[x][y] = DEAD_CELL;
	}

	public void setCellColor(Color color) {
		this.cellColor = color;
	}

	public static void setScale(String scale) {
		String text = scale.toLowerCase();
		switch (text) {
		case "little":
			LifeManager.scale = LifeManager.little;
			break;
		case "medium1":
			LifeManager.scale = LifeManager.medium1;
			break;
		case "medium2":
			LifeManager.scale = LifeManager.medium2;
			break;
		case "large":
			LifeManager.scale = LifeManager.large;
			break;
		default:
			System.out.println("Don't have that scale, so choosing my own scale");
			LifeManager.scale = LifeManager.medium1;
			break;
		}
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public int getAliveCells() {
		return aliveCells;
	}

	public void setAliveCells(int aliveCells) {
		this.aliveCells = aliveCells;
	}

}
