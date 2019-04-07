package mapeditor.tools;

import global.InputController;
import mapeditor.MapEditMenu;
import mapeditor.MapPreview;
import mapeditor.Tile;
import tiles.PremadeTileObject;

public class SingleTile extends MapTool {
	protected Tile tile;
	
	public Tile getTile() {
		return tile;
	}
	
	@Override
	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
		this.map.setTile(tile.getId(), x, y);
	}
	
	public void doActionOnMapRightClick(int x, int y, int xMouse, int yMouse) {
		this.map.setTile(0,x,y);
	}
	
	public SingleTile(Tile tile) {
		super();
		this.tile = tile;
	}
}
