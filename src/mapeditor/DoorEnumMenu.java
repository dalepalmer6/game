package mapeditor;

import java.util.ArrayList;

import font.SelectionTextWindow;
import gamestate.DoorEntity;
import menu.Menu;
import menu.StartupNew;

public class DoorEnumMenu extends Menu {
	private ArrayList<DoorEntity> doors;
	
	public DoorEnumMenu(StartupNew m, ArrayList<DoorEntity> dList) {
		super(m);
		// TODO Auto-generated constructor stub
		doors = dList;
	}
	
	public void createMenu() {
		SelectionTextWindow stw = new SelectionTextWindow(0,0,50,50,state);
		for (DoorEntity door : doors) {
			String text = door.getDesc() + ", (" + door.getX() + ":" + door.getWidth() + "," + door.getY() + ":" + door.getHeight() + ")" + " - GOES TO - " + door.getDestMap() + ": (" + door.getDestX() + "," + door.getDestY() + ")";
			stw.add(new DoorMenuItem(door, text,state));
		}
		addMenuItem(stw);
	}

}
