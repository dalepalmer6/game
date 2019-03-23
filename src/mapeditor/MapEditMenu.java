package mapeditor;

import menu.BackButton;
import menu.Menu;
import menu.StartupNew;

public class MapEditMenu extends Menu {
	private int TILE_SIZE = 32;
	private Map map;
	
	public Map getMap() {
		return map;
	}
	
	public MapEditMenu(StartupNew m, TileHashMap tm) {
		super(m);
		this.map = new Map("TestMap",300,300, tm);
		MapPreview createdView = new MapPreview(TILE_SIZE,3 * TILE_SIZE,3 * TILE_SIZE, map, tm);
		TileBar tilebar = new TileBar(16,16,tm, createdView,state);
		addMenuItem(createdView);
		addMenuItem(tilebar);
		addMenuItem(new SaveMapButton("Save",0,0,m));
		addMenuItem(new LoadMapButton("Load",300,0,m));
		addMenuItem(new BackButton(m));
		addMenuItem(new ScrollLeftButton("L", tilebar.getX()-30, tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
		addMenuItem(new ScrollRightButton("R", tilebar.getX() + tilebar.getWidth(), tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
	}
}
