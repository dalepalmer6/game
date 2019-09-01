package battlesystem;

import menu.StartupNew;
import menu.TexturedMenuItem;

public class SmashMenuItem extends TexturedMenuItem {
	private long timer;
	private long killTime;
	
	public SmashMenuItem(double x, double y, int width, int height, StartupNew state, String texture, int dx, int dy,
			int dw, int dh) {
		super(x, y, width, height, state, texture, dx, dy, dw, dh);
		// TODO Auto-generated constructor stub
		timer = 0;
		killTime = 60;
	}

	public void updateAnim() {
		if (timer > killTime) {
			state.getMenuStack().peek().setToRemove(this);
		} else {
			timer++;
		}
	}
	
}
