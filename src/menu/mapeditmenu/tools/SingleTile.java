package menu.mapeditmenu.tools;

import menu.ButtonMenuItem;
import menu.mapeditmenu.MapEditMenu;
import menu.mapeditmenu.buttons.LayerBGButton;
import menu.mapeditmenu.buttons.LayerBaseButton;
import menu.mapeditmenu.buttons.LayerFGButton;
import menu.mapeditmenu.mappreview.MapPreview;
import system.SystemState;
import system.controller.InputController;
import system.map.Tile;
import tiles.PremadeTileObject;

public class SingleTile extends MapTool {
	protected Tile tile;
	private ButtonMenuItem buttonOn = null;
	
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
	
	public void setButtons(SystemState state) {
		
	}
	
	public void update(InputController input) {
		for (ButtonMenuItem bm : associatedButtons) {
			if (bm.isOn() && bm != buttonOn) {
				if (buttonOn != null) {
					buttonOn.toggle();
				}
				buttonOn = bm;
			}
		}
	}
	
	public SingleTile(Tile tile, SystemState state) {
		super(state);
		this.tile = tile;
		toolInfo = "Tile ID: " + tile.getId();
		associatedButtons.add(new LayerBaseButton("",800,0,64,16,state));
		associatedButtons.add(new LayerBGButton("",1000,0,64,16,state));
		associatedButtons.add(new LayerFGButton("",1200,0,64,16,state));
	}
}
