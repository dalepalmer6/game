package menu.mapeditmenu.tools;

import gamestate.entities.DoorEntity;
import gamestate.entities.Entity;
import menu.mapeditmenu.MapEditMenu;
import menu.mapeditmenu.mappreview.MapPreview;
import system.SystemState;
import system.controller.InputController;
import system.map.Map;

public class DoorTool extends MapTool {
	private DoorEntity door;
	private boolean otherMapState; // false if in first system.map, true if in destination system.map
	private Map oldMap;
	private Map newMap;
	private int savedViewX;
	private int savedViewY;
	private String newmapName;
	public DoorTool(DoorEntity door, SystemState state) {
		super(state);
		this.door = door;
	}
	
	public void setDoor(DoorEntity door) {
		this.door = door;
	}
	
	public void update(InputController input) {
		if (newmapName != null) {
			editDestinationOnly();
		}
	}
	
	public void setNewMapName(String n) {
		newmapName = n;
	}
	
	public void editDestinationOnly() {
		door.getState().setHoldable(false);
		MapPreview mp = ((MapEditMenu) door.getState().getMenuStack().peek()).getMapPreview();
		int tilesize = mp.getTileSize();
		String mapName = newmapName;//getthis from somewhere
		newMap = new Map("locmap",34,34,door.getState().tileMap,door.getState());
		newMap.parseMap(4,mapName);
		oldMap = mp.getMap();
		MapEditMenu mem = ((MapEditMenu) door.getState().getMenuStack().peek());
		savedViewX = mp.getViewX();
		savedViewY = mp.getViewY();
//		newMap = new MapPreview(32,96,96,mapToSetLocation,door.getState().tileMap,door.getState());
		mp.setMap(newMap);
		mp.setView(0,0);
		otherMapState = true;
		newmapName = null;
	}
	
	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
		//create a new Map object and set the MapPreview object to draw it instead for this
		door.getState().setHoldable(false);
		MapPreview mp = ((MapEditMenu) door.getState().getMenuStack().peek()).getMapPreview();
		int tilesize = mp.getTileSize();
		if (otherMapState) {
			
			if (xMouse > mp.getWidth() || yMouse > mp.getHeight()
					|| xMouse < mp.getX() || yMouse < mp.getY()) {
					return;
			}
			door.setDestination(mp.getMap().getMapId(), (x-1) * tilesize + xMouse%tilesize,(y-1) *tilesize + yMouse%tilesize);
			mp.setMap(oldMap);
			mp.setView(savedViewX,savedViewY);
			mp.getMap().readProperties();
//			SystemState.out.println("Resetting system.map to old system.map. Hopefully it worked for you!");
			otherMapState = false;
			
		}
		else {
			if (xMouse > mp.getWidth() || yMouse > mp.getHeight()
					|| xMouse < mp.getX() || yMouse < mp.getY()) {
					return;
			} 
			door.setLocation((x-1) * tilesize + xMouse%tilesize,(y-1) *tilesize + yMouse%tilesize);
//			mp.setHoldableState(false);
			//load in a new system.map to set the dest
			String mapName = door.getDestMap();//getthis from somewhere
			newMap = new Map("locmap",34,34,door.getState().tileMap,door.getState());
			newMap.parseMap(4,mapName);
			oldMap = mp.getMap();
			MapEditMenu mem = ((MapEditMenu) door.getState().getMenuStack().peek());
			savedViewX = mp.getViewX();
			savedViewY = mp.getViewY();
//			newMap = new MapPreview(32,96,96,mapToSetLocation,door.getState().tileMap,door.getState());
			mp.setMap(newMap);
			mp.setView(0,0);
			otherMapState = true;
		}
	}
	
//	public void doActionOnMapRightClick(int x, int y, int xMouse, int yMouse) {
//		MapPreview mp = ((MapEditMenu) entity.getState().getMenuStack().peek()).getMapPreview();
//		for (Entity e : mp.getMap().getEntities()) {
//			//set up the code to remove the entities near the mouse click
//		};
//	}
}
