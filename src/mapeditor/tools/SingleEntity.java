package mapeditor.tools;

import gamestate.Entity;
import mapeditor.MapEditMenu;
import mapeditor.MapPreview;

public class SingleEntity extends MapTool {
	private Entity entity;
	
	public SingleEntity(Entity e) {
		super();
		this.entity = e;
	} 
	
	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
		Entity newEntity = entity.createCopy(x,y,16,24);
		MapPreview mp = ((MapEditMenu) entity.getState().getMenuStack().peek()).getMapPreview();
		int tilesize = mp.getTileSize();
		
//		if (xMouse > mp.getWidth() || yMouse > mp.getHeight()
//			|| xMouse < mp.getX() || yMouse < mp.getY()) {
//			return;
//		} 
		newEntity.setCoordinates(x * tilesize + xMouse%tilesize,(y) *tilesize + yMouse%tilesize);
		map.addToEntities(newEntity);
		mp.setHoldableState(false);
	}
	
	public void doActionOnMapRightClick(int x, int y, int xMouse, int yMouse) {
		MapPreview mp = ((MapEditMenu) entity.getState().getMenuStack().peek()).getMapPreview();
		for (Entity e : mp.getMap().getEntities()) {
			//set up the code to remove the entities near the mouse click
		};
	}
	
}
