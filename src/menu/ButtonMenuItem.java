package menu;

import canvas.MainWindow;
import font.TextWindow;

public class ButtonMenuItem extends MenuItem {
	private TextWindow tw;
	public ButtonMenuItem(String t, int x, int y, int width, int height, StartupNew m) {
		super("", x, y, 175, 63, m);
		tw = new TextWindow(true,t,x,y,width/32,height/32,m);
		// TODO Auto-generated constructor stub
	}
	
	public void draw(MainWindow m) {
		//draw the textwindow
		tw.draw(m);
	}
	
}
