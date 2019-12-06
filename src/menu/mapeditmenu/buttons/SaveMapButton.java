package menu.mapeditmenu.buttons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class SaveMapButton extends ButtonMenuItem {

	public SaveMapButton(String t, int x, int y, SystemState m) {
		super(t, x, y,64,16, m);
		// TODO Auto-generated constructor stub
	}
	
	public void saveMap() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		m.saveMap();
		SystemState.out.println("Successfully saved.");
	}
	
	public void loadMap() {
		
	}
	
	public String execute() {
		SystemState.out.println("Saving Map");
		saveMap();
		return null;
	}
}
