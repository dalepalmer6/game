package actionmenu;

import canvas.MainWindow;
import menu.MenuItem;
import menu.StartupNew;

public class BackingBar extends MenuItem {

	public BackingBar(StartupNew state) {
		super("",0,0,0,0,state);
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img\\menu.png");
		//draw the backing bar
		for (int i = 0; i < m.getScreenWidth(); i+=128) {
			m.renderTile(i,y,128,128,0,0,32,32);
		}
	}
	
}
