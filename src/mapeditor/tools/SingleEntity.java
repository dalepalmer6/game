package mapeditor.tools;

import gamestate.Entity;
import mapeditor.MapEditMenu;
import mapeditor.MapPreview;

public class SingleEntity extends MapTool {
	private Entity entity;
	
	public SingleEntity(Entity e) {
		super();
		this.entity = e;
		this.toolInfo = entity.getText();
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
