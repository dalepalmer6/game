package menu.mapeditmenu.buttons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

import menu.ButtonMenuItem;
import menu.Menu;
import menu.MenuItem;
import menu.loadmapmenu.LoadMapMenu;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class LoadMapButton extends ButtonMenuItem {

	public LoadMapButton(String t, int x, int y, SystemState m) {
		super(t, x, y,64,16, m);
		// TODO Auto-generated constructor stub
	}
	
	public void loadMap() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		
//		m.parseMap(1);
		m.setChangeMap("MID");
//		SystemState.out.println("Successfully loaded.");
	}
	
	public String execute() {
		SystemState.out.println("Loading Map");
		LoadMapMenu m = new LoadMapMenu(((MapEditMenu) this.state.getMenuStack().peek()).getMapPreview(),state);
		m.createMenu(state.getMapNames());
//		loadMap();
		return text;
	}

}
