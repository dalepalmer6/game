package mapeditor;

import menu.BackButton;
import menu.Menu;
import menu.StartupNew;

public class MapEditMenu extends Menu {
	private int TILE_SIZE = 32;
	private Map map;
	private MapPreview mapPreview;
	
	public Map getMap() {
		return map;
	}
	
	public MapPreview getMapPreview() {
		return mapPreview;
	}
	
	public void setMapPreview(MapPreview mp) {
		mapPreview = mp;
	}
	
	public MapEditMenu(StartupNew m, TileHashMap tm) {
		super(m);
		this.map = new Map("podunk",34,34, tm, state);
		mapPreview = new MapPreview(TILE_SIZE,3 * TILE_SIZE,3 * TILE_SIZE, map, tm,state);
		TileBar tilebar = new TileBar(16,8,tm, mapPreview,state);
		addMenuItem(mapPreview);
		addMenuItem(tilebar);
		addMenuItem(new EntityBar(16,8,mapPreview,tilebar,state));
		addMenuItem(new SaveMapButton("Save",0,0,m));
		addMenuItem(new LoadMapButton("Load",300,0,m));
		addMenuItem(new LayerBaseButton("",500,0,150,50,m));
		addMenuItem(new LayerBGButton("",750,0,150,50,m));
		addMenuItem(new LayerFGButton("",1000,0,150,50,m));
		addMenuItem(new DoorEnumButton(1500,0,m));
		addMenuItem(new BackButton(m));
		addMenuItem(new ScrollLeftButton("L", tilebar.getX()-30, tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
		addMenuItem(new ScrollRightButton("R", tilebar.getX() + tilebar.getWidth(), tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
	}
}
