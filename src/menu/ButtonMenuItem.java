package menu;

import menu.windows.TextWindow;
import system.MainWindow;
import system.SystemState;

public class ButtonMenuItem extends MenuItem {
	private int width;
	private int height;
	protected boolean on;
	protected TextWindow tw;
	
	public boolean isOn() {
		return on;
	}
	
	public void toggle() {
		on = !on;
	}
	
	public ButtonMenuItem(String t, int x, int y, int width, int height, SystemState m) {
		super(t, x, y,175,63, m);
		this.width = width;
		this.height = height;
		createTextWindow(t);
		on = false;
//		width = 175;
//		height = 63;
		// TODO Auto-generated constructor stub
	}
	
	public void createTextWindow(String t) {
		tw = new TextWindow(true,t,(int)x,(int)y,width/32,height/32,state);
	}
	
	public void draw(MainWindow m) {
		//draw the textwindow
		tw.draw(m);
	}
	
}
