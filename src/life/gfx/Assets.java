package life.gfx;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Assets {
	
	private static BufferedImage sheet;
	
	public static ArrayList<BufferedImage> pixels;
	
	public static BufferedImage pixel;
	
	public static void init() {
		sheet = ImageLoader.loadImage("/textures/sheet.png");
		pixel = sheet.getSubimage(0, 0, 16, 16);
	}

}
