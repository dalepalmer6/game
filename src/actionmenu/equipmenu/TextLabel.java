package actionmenu.equipmenu;

import canvas.MainWindow;
import font.CharList;
import font.Text;
import menu.MenuItem;
import menu.StartupNew;

public class TextLabel extends MenuItem {
//	private Text textObject;
	//boolean shouldDrawAll, String s, int x, int y,int width, int height, CharList cd
	int r = 0;
	int g = 0;
	int b = 0;
	public TextLabel(String t, int x, int y,StartupNew state) {
		super(t,x,y,state);
		textObject = new Text(true,t,x,y,0,0,state.charList);
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
