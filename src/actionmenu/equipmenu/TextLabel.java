package actionmenu.equipmenu;

import canvas.MainWindow;
import font.CharList;
import font.Text;
import menu.MenuItem;
import menu.StartupNew;

public class TextLabel extends MenuItem {
	private Text textObject;
	//boolean shouldDrawAll, String s, int x, int y,int width, int height, CharList cd
	public TextLabel(String t, int x, int y,StartupNew state) {
		super(t,x,y,state);
		textObject = new Text(true,t,x,y,0,0,state.charList);
		textObject.setAsSingleString();
	}
	
	public int getWidthOfText() {
		return textObject.getWidth();
	}
	
	public void setText(String s) {
		textObject.setText(s);
	}
	
	public void draw(MainWindow m) {
		draw(m,0,0,0);
	}
	
	public void draw(MainWindow m, int r, int g, int b) {
		textObject.draw(m,r,g,b);
	}
}
