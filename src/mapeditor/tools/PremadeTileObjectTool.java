package mapeditor.tools;

import global.InputController;
import mapeditor.Tile;
import menu.StartupNew;
import tiles.PremadeTileObject;

public class PremadeTileObjectTool extends SingleTile {
	PremadeTileObject pto;
	
	@Override
	public void doActionOnMap(int x, int y, int mx, int my) {
		for (int i = 0; i < tile.getWidth(); i++) {
			for (int j = 0; j < tile.getHeight(); j++) {
				this.map.setTile(tile.getId(), x+i, y+j);
			}
		}
	}
	
	public PremadeTileObjectTool(Tile t, StartupNew state) {
		super(t,state);
	}
		
}
