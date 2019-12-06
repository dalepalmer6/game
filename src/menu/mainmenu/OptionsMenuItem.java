package menu.mainmenu;

import menu.MenuItem;
import menu.tileeditmenu.TileEditMenu;
import system.SystemState;

public class OptionsMenuItem extends MenuItem {
	private static String text = "Options";
	
	public OptionsMenuItem(int x, int y, SystemState m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		SystemState.out.println("Loading the options menu.");
		//load the tile editor
		TileEditMenu tem = new TileEditMenu(state);
		state.getMenuStack().push(tem);
		return null;
	}
	
}
