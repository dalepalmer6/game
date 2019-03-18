package mapeditor;

import menu.Menu;
import menu.StartupNew;

public class MapEditMenu extends Menu {
	private int TILE_SIZE = 32;
	
	public MapEditMenu(StartupNew m, TileHashMap tm) {
		super(m);
		MapPreview createdView = new MapPreview(TILE_SIZE,3 * TILE_SIZE,3 * TILE_SIZE,new Map("TestMap",20,20, tm), tm);
		addMenuItem(createdView);
		addMenuItem(new TileBar((21 + 3) * TILE_SIZE,3 * TILE_SIZE,32,16,tm, createdView,state));
		addMenuItem(new SaveMapButton("Save",0,0,m));
		addMenuItem(new LoadMapButton("Load",200,0,m));
	}
}
