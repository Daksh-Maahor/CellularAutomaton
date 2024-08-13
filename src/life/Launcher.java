package life;

import java.awt.Color;
import java.io.IOException;

public class Launcher {

	public static void main(String[] args) throws IOException {
		Color color, oppColor;
		
		color = Color.white;
		oppColor = Color.black;
		boolean randStart = false;
		
		Life life = new Life("Cellular Automaton v4", 1360, 680, color, oppColor, randStart);
		life.start();
	}

}
