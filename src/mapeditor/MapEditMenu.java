package mapeditor;

import menu.Menu;

public class MapEditMenu extends Menu {
	public MapEditMenu() {
		super();
		addMenuItem(new MapPreview(new Map("TestMap",20,20)));
		addMenuItem(new TileBar());
	}
}
