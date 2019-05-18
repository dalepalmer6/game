package mapeditor;

import java.util.ArrayList;

import font.TextWindow;
import global.InputController;
import mapeditor.tools.MapTool;
import menu.BackButton;
import menu.ButtonMenuItem;
import menu.Menu;
import menu.StartupNew;

public class MapEditMenu extends Menu {
	private int TILE_SIZE = 32;
	private Map map;
	private MapPreview mapPreview;
	private ToolInfoWindow toolInfoWindow;
	private ArrayList<ButtonMenuItem> currentToolButtons;
	private MapTool prevTool;
	
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
		currentToolButtons = mapPreview.getTool().getAssociatedButtons();
		toolInfoWindow.setTool(mapPreview.getTool());
		toolInfoWindow.setText(mapPreview.getTool().getToolInfo());
		mapPreview.getTool().update(input);
		if (prevTool != null) {
			menuItems.removeAll(prevTool.getAssociatedButtons());
		}
		
		for (ButtonMenuItem mi : currentToolButtons) {
			addMenuItem(mi);
		}
		prevTool = mapPreview.getTool();
	}
	
	public MapEditMenu(StartupNew m, TileHashMap tm) {
		super(m);
		this.map = new Map("podunk",34,34, tm, state);
		mapPreview = new MapPreview(TILE_SIZE,3 * TILE_SIZE,3 * TILE_SIZE, map, tm,state);
		TileBar tilebar = new TileBar(48,8,tm, mapPreview,state);
		addMenuItem(mapPreview);
		addMenuItem(tilebar);
		addMenuItem(new EntityBar(16,8,mapPreview,tilebar,state));
		addMenuItem(new SaveMapButton("Save",0,0,m));
		addMenuItem(new LoadMapButton("Load",70*4,0,m));
		addMenuItem(new GridToggleButton("Grid",140*4,0,m));
		addMenuItem(new DoorEnumButton(1500,0,m));
		addMenuItem(new BackButton(m));
		addMenuItem(new ScrollLeftButton("L", tilebar.getX()-30, tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
		addMenuItem(new ScrollRightButton("R", tilebar.getX() + tilebar.getWidth(), tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
		toolInfoWindow = new ToolInfoWindow(state, mapPreview.getTool());
//		toolInfoWindow.setTool();
		addMenuItem(toolInfoWindow);
		mapPreview.getTool().setButtons(state);
	}
}
