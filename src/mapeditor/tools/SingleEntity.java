package mapeditor.tools;

import canvas.MainWindow;
import gamestate.Entity;
import global.InputController;
import mapeditor.MapEditMenu;
import mapeditor.MapPreview;
import mapeditor.NewDoorButton;
import mapeditor.NewEnemySpawnButton;
import mapeditor.NewEntityButton;
import mapeditor.NewHotSpotButton;
import menu.StartupNew;

public class SingleEntity extends MapTool {
	private Entity entity;
	
	public void draw(MainWindow m, int x, int y) {
		entity.drawEntity(m,x,y);
	}
	
	public void update(InputController input) {
		if (input.getSignals().get("KEY_NUMPAD8")) {
			entity.increaseSize(0,4);
		}
		if (input.getSignals().get("KEY_NUMPAD6")) {
			entity.increaseSize(4,0);
		}
		if (input.getSignals().get("KEY_NUMPAD4")) {
			entity.increaseSize(-4,0);
		}
		if (input.getSignals().get("KEY_NUMPAD2")) {
			entity.increaseSize(0,-4);
		}
	}
	
	public SingleEntity(Entity e,StartupNew state) {
		super(state);
		this.entity = e;
		this.toolInfo = entity.getInfoForTool();
//		associatedButtons.add(new NewEntityButton(500,0,159,31,state));
//		associatedButtons.add(new NewDoorButton(700,0,159,31,state));
//		associatedButtons.add(new NewHotSpotButton(900,0,159,31,state));
//		associatedButtons.add(new NewEnemySpawnButton(1100,0,159,31,state));
	}
	
	/*
	 * Old method only creates a new entity with the current face/sprite
	 * 
	 * */
//	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
//		Entity newEntity = entity.createCopy(x,y,entity.getWidth(),entity.getHeight(),entity.getName());
//		MapPreview mp = ((MapEditMenu) entity.getState().getMenuStack().peek()).getMapPreview();
//		int tilesize = mp.getTileSize();
//		
////		if (xMouse > mp.getWidth() || yMouse > mp.getHeight()
////			|| xMouse < mp.getX() || yMouse < mp.getY()) {
////			return;
////		} 
//		newEntity.setCoordinates((x-1) * tilesize + xMouse%tilesize,(y-1) *tilesize + yMouse%tilesize);
//		map.addToEntities(newEntity);
//		mp.setHoldableState(false);
//	}
	
//	public void setButtons(StartupNew state) {
//	}
	
	/*New Entity, set the new x,y of the entity*/
	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
//		Entity newEntity = entity.createCopy(x,y,entity.getWidth(),entity.getHeight(),entity.getName());
		MapPreview mp = ((MapEditMenu) entity.getState().getMenuStack().peek()).getMapPreview();
		int tilesize = mp.getTileSize();
		
	//	if (xMouse > mp.getWidth() || yMouse > mp.getHeight()
	//		|| xMouse < mp.getX() || yMouse < mp.getY()) {
	//		return;
	//	} 
		entity.setCoordinates((x-1) * tilesize + xMouse%tilesize,(y-1) *tilesize + yMouse%tilesize);
//		map.addToEntities(newEntity);
		mp.setHoldableState(false);
	}
	
	public void doActionOnMapRightClick(int x, int y, int xMouse, int yMouse) {
		MapPreview mp = ((MapEditMenu) entity.getState().getMenuStack().peek()).getMapPreview();
		for (Entity e : mp.getMap().getEntities()) {
			//set up the code to remove the entities near the mouse click
		};
	}
	
}
