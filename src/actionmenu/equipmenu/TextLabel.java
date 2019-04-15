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
		textObject = new Text(true,t,x,y,1000,1000,state.charList);
	}
	
	public void draw(MainWindow m) {
		textObject.draw(m);
	}
}
