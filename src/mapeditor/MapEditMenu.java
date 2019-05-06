package mapeditor;

import font.TextWindow;
import global.InputController;
import menu.BackButton;
import menu.Menu;
import menu.StartupNew;

public class MapEditMenu extends Menu {
	private int TILE_SIZE = 32;
	private Map map;
	private MapPreview mapPreview;
	private TextWindow toolInfoWindow;
	
	public Map getMap() {
		return map;
	}
	
	public MapPreview getMapPreview() {
		return mapPreview;
	}
	
	public void setMapPreview(MapPreview mp) {
		mapPreview = mp;
	}
	
	public void update(InputController input) {
		if (mapPreview.getTool() != null) {
			toolInfoWindow.setText(mapPreview.getTool().getToolInfo());
			mapPreview.getTool().update(input);
		}
		
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
		toolInfoWindow = new TextWindow(true,"",1500,700,10,3,state);
		toolInfoWindow.setIgnoreCodes();
		addMenuItem(toolInfoWindow);
	}
}
