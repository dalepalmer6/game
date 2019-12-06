package menu.mapeditmenu.buttons;

import gamestate.entities.DoorEntity;
import gamestate.entities.HotSpot;
import menu.ButtonMenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class NewHotSpotButton extends ButtonMenuItem {

	public NewHotSpotButton(int x, int y, int width, int height, SystemState m) {
		super("New HotSpot", x, y, width, height, m);
		// TODO Auto-generated constructor stub
	}
	
	public String execute() {
		state.setHoldable(false);
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		HotSpot e = new HotSpot("Description",0,0,32,32,state,0,0,"podunk","Text","Cutscene Name");
		m.getEntities().add(e);
		return null;
	}
	
}