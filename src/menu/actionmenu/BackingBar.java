package menu.actionmenu;

import menu.MenuItem;
import system.MainWindow;
import system.SystemState;

public class BackingBar extends MenuItem {

	public BackingBar(SystemState state) {
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
