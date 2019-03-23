package mapeditor;

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

import menu.MenuItem;
import menu.StartupNew;

public class LoadMapButton extends MenuItem {

	public LoadMapButton(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	
	public void loadMap() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		m.parseMap(new File("TestMap.map"));
		System.out.println("Successfully loaded.");
	}
	
	public String execute() {
		System.out.println("Loading Map");
		loadMap();
		return text;
	}

}
