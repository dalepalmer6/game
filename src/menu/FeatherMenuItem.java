package menu;

import system.SystemState;

public class FeatherMenuItem extends TexturedMenuItem {
	private int timer;
	
	public FeatherMenuItem(String text, double x, double y, int width, int height, SystemState state, String texture, int dx,
			int dy, int dw, int dh) {
		super(text, x-width, y-(height/2), width, height, state, texture, dx, dy, dw, dh);
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
	
	public void setX(double x) {
		this.x = x - (width);
	}
	
	public void setY(double y) {
		this.y  = y - (height/2);
	}

}
