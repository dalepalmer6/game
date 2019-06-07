package battlesystem;

import menu.StartupNew;
import menu.TexturedMenuItem;

public class FeatherMenuItem extends TexturedMenuItem {
	private int timer;
	
	public FeatherMenuItem(String text, int x, int y, int width, int height, StartupNew state, String texture, int dx,
			int dy, int dw, int dh) {
		super(text, x, y, width, height, state, texture, dx, dy, dw, dh);
		// TODO Auto-generated constructor stub
		timer = 0;
	}
	
	public void updateAnim() {
		timer++;
		if (timer % 4 == 0) {
			x-=4*Math.cos(timer*Math.PI/16);
			y-=4*Math.cos(timer*Math.PI/16);
		}
		
	}

}
