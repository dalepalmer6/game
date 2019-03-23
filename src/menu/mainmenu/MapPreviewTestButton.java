package menu.mainmenu;

import global.MenuStack;
import mapeditor.MapEditMenu;
import menu.MenuItem;
import menu.StartupNew;

public class MapPreviewTestButton extends MenuItem {
	private static String text = "Map Editor";
	
	public MapPreviewTestButton(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		System.out.println("Loading map edit.");
		state.getMenuStack().push(new MapEditMenu(state,state.tileMap));
		return null;
	}
}
