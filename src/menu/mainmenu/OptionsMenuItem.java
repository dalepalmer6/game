package menu.mainmenu;

import mapeditor.TileEditMenu;
import menu.MenuItem;
import menu.StartupNew;

public class OptionsMenuItem extends MenuItem {
	private static String text = "Options";
	
	public OptionsMenuItem(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		System.out.println("Loading the options menu.");
		//load the tile editor
		TileEditMenu tem = new TileEditMenu(state);
		state.getMenuStack().push(tem);
		return null;
	}
	
}
