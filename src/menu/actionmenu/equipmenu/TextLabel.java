package menu.actionmenu.equipmenu;

import menu.MenuItem;
import system.MainWindow;
import system.SystemState;

public class TextLabel extends MenuItem {
	int r = 0;
	int g = 0;
	int b = 0;
	
	public TextLabel(String t, int x, int y,SystemState state) {
		super(t,x,y,state);
		textObject = state.createTextEngine(true,t,x,y,0,0);
		textObject.setAsSingleString();
	}
	
	public void setWhite() {
		r = 255;
		g = 255;
		b = 255;
	}
	
	public int getWidthOfText() {
		return textObject.getWidth();
	}
	
	public void setText(String s) {
		textObject.setText(s);
	}
	
	public void draw(MainWindow m) {
		draw(m,r,g,b);
	}
	
	public void draw(MainWindow m, int r, int g, int b) {
		textObject.draw(m,r,g,b);
	}
}
