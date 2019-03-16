package menu.mainmenu;

import global.GlobalVars;
import mapeditor.MapEditMenu;
import menu.MenuItem;

public class MapPreviewTestButton extends MenuItem {
	private static String text = "Map Editor";
	
	public MapPreviewTestButton(int x, int y) {
		super(text,x,y);
	}
	
	public void execute() {
		System.out.println("Loading map edit.");
		GlobalVars.menuStack.push(new MapEditMenu());
	}
}
