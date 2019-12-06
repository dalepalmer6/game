package menu.mainmenu;

import menu.MenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.MenuStack;
import system.SystemState;

public class MapPreviewTestButton extends MenuItem {
	private static String text = "Map Editor";
	
	public MapPreviewTestButton(int x, int y, SystemState m) {
		super(text,x,y,m);
	}
	
	public String execute() {
//		SystemState.out.println("Loading system.map edit.");
		state.getMenuStack().push(new MapEditMenu(state,state.tileMap));
		return null;
	}
}
