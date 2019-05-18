package mapeditor.tools;

import global.InputController;
import mapeditor.LayerBGButton;
import mapeditor.LayerBaseButton;
import mapeditor.LayerFGButton;
import mapeditor.MapEditMenu;
import mapeditor.MapPreview;
import mapeditor.Tile;
import menu.StartupNew;
import tiles.PremadeTileObject;

public class SingleTile extends MapTool {
	protected Tile tile;
	
//	public void draw(MainWindow m, int x, int y) {
//		tile.getInstance(0).draw(m);
//	}
	
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
	
	public void setButtons(StartupNew state) {
		
	}
	
	public SingleTile(Tile tile, StartupNew state) {
		super(state);
		this.tile = tile;
		toolInfo = "Tile ID: " + tile.getId();
		associatedButtons.add(new LayerBaseButton("",800,0,64,16,state));
		associatedButtons.add(new LayerBGButton("",1000,0,64,16,state));
		associatedButtons.add(new LayerFGButton("",1200,0,64,16,state));
	}
}
