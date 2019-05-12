package mapeditor;

import gamestate.DoorEntity;
import gamestate.HotSpot;
import menu.ButtonMenuItem;
import menu.StartupNew;

public class NewHotSpotButton extends ButtonMenuItem {

	public NewHotSpotButton(int x, int y, int width, int height, StartupNew m) {
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
