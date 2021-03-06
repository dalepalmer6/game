package menu.mapeditmenu;

import java.util.ArrayList;

import menu.BackButton;
import menu.ButtonMenuItem;
import menu.Menu;
import menu.mapeditmenu.buttons.DoorEnumButton;
import menu.mapeditmenu.buttons.GridToggleButton;
import menu.mapeditmenu.buttons.LoadMapButton;
import menu.mapeditmenu.buttons.SaveMapButton;
import menu.mapeditmenu.entitybar.EntityBar;
import menu.mapeditmenu.mappreview.MapPreview;
import menu.mapeditmenu.tilebar.TileBar;
import menu.mapeditmenu.tools.MapTool;
import menu.windows.TextWindow;
import system.SystemState;
import system.controller.InputController;
import system.map.Map;
import system.map.TileHashMap;

public class MapEditMenu extends Menu {
	private int TILE_SIZE = 32;
	private Map map;
	private MapPreview mapPreview;
	private ToolInfoWindow toolInfoWindow;
	private ArrayList<ButtonMenuItem> currentToolButtons;
	protected MapTool prevTool;
	private TileBar tilebar;
	
	public TileBar getTileBar() {
		return tilebar;
	}
	
	public Map getMap() {
		return map;
	}
	
	public MapPreview getMapPreview() {
		return mapPreview;
	}
	
	public void setMapPreview(MapPreview mp) {
		mapPreview = mp;
	}
	
	public void setTool(MapTool m) {
		this.prevTool = m;
	}
	
	public MapTool getMapTool() {
		return prevTool;
	}
	
	
	public void update(InputController input) {
		if (input.getSignals().get("BACK")) {
			canRemove = true;
		}
		prevTool.update(input);
		currentToolButtons = prevTool.getAssociatedButtons();
		toolInfoWindow.setTool(prevTool);
		toolInfoWindow.setText(prevTool.getToolInfo());
		prevTool.update(input);
		tilebar.updateAnim();
		if (prevTool != null) {
			menuItems.removeAll(prevTool.getAssociatedButtons());
		}
		
		for (ButtonMenuItem mi : currentToolButtons) {
			mi.updateAnim();
			addMenuItem(mi);
		}
		mapPreview.setTool(prevTool);
	}
	
	public MapEditMenu(SystemState m, TileHashMap tm) {
		super(m);
		canRemove = false;
		this.map = new Map("podunk",34,34, tm, state);
		prevTool = new MapTool(state);
		mapPreview = new MapPreview(TILE_SIZE,3 * TILE_SIZE,3 * TILE_SIZE, map, tm,state);
		tilebar = new TileBar(48,8,tm, mapPreview,state);
		addMenuItem(mapPreview);
		addMenuItem(tilebar);
		addMenuItem(new EntityBar(16,8,mapPreview,tilebar,state));
		addMenuItem(new SaveMapButton("Save",0,0,m));
		addMenuItem(new LoadMapButton("Load",70*4,0,m));
		addMenuItem(new GridToggleButton("Grid",140*4,0,m));
		addMenuItem(new DoorEnumButton(1500,0,m));
		addMenuItem(new BackButton(m));
//		addMenuItem(new ScrollLeftButton("L", tilebar.getX()-30, tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
//		addMenuItem(new ScrollRightButton("R", tilebar.getX() + tilebar.getWidth(), tilebar.getY() + tilebar.getHeight()/2,30,30,m,tilebar));
		toolInfoWindow = new ToolInfoWindow(state, mapPreview.getTool());
//		toolInfoWindow.setTool();
		addMenuItem(toolInfoWindow);
		mapPreview.getTool().setButtons(state);
	}
}
