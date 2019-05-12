package mapeditor.tools;

import gamestate.DoorEntity;
import gamestate.Entity;
import mapeditor.Map;
import mapeditor.MapEditMenu;
import mapeditor.MapPreview;
import menu.StartupNew;

public class DoorTool extends MapTool {
	private DoorEntity door;
	private boolean otherMapState; // false if in first map, true if in destination map
	private Map oldMap;
	private Map newMap;
	private int savedViewX;
	private int savedViewY;

	public DoorTool(DoorEntity door, StartupNew state) {
		super(state);
		this.door = door;
	}
	
	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
		//create a new Map object and set the MapPreview object to draw it instead for this
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
			System.out.println("Resetting map to old map. Hopefully it worked for you!");
			otherMapState = false;
		}
		else {
			if (xMouse > mp.getWidth() || yMouse > mp.getHeight()
					|| xMouse < mp.getX() || yMouse < mp.getY()) {
					return;
			} 
			door.setLocation((x-1) * tilesize + xMouse%tilesize,(y-1) *tilesize + yMouse%tilesize);
//			mp.setHoldableState(false);
			//load in a new map to set the dest
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
