package mapeditor;

import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import global.InputController;
import mapeditor.tools.MapTool;
import mapeditor.tools.SingleTile;
import menu.Menu;
import menu.StartupNew;

public class TileEditMenu extends MapEditMenu {
	private TilePreview tilePrev;
	private TileBar tileBar;
	private ArrayList<TextLabel> colValues;
	@Override
	public void update(InputController input) {
		Tile t = ((SingleTile) prevTool).getTile();
		tilePrev.setTile(t);
		
		menuItems.removeAll(colValues);
		colValues = new ArrayList<TextLabel>();
		
		int x = 656;
		int y = 500;
		for (String s : t.getInstance(0).getCollisionData().split(",")) {
			TextLabel tl = new TextLabel(s,x,y,state);
			tl.setWhite();
			colValues.add(tl);
			x+=64;
			if (x >= 656+256) {
				x = 400+256;
				y+=64;
			}
		}
		menuItems.addAll(colValues);
	}
	
	public TileEditMenu(StartupNew state) {
		super(state,state.tileMap);
		
		colValues = new ArrayList<TextLabel>();
		prevTool = new SingleTile(state.tileMap.getTile(0),state);
		menuItems.clear();
		drawables.clear();
		needToAdd.clear();
		tilePrev = new TilePreview("",400,500,state);
		tileBar = new TileBar(48,8,state.tileMap,state);
		
		drawables.add(tilePrev);
		drawables.add(tileBar);
		
		state.loadAllTiles("img/tilesets/cave.png");
		
		addMenuItem(tilePrev);
		addMenuItem(tileBar);
	}
	
}
