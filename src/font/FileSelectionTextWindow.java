package font;

import java.util.ArrayList;

import canvas.MainWindow;
import menu.MenuItem;
import menu.StartupNew;
import menu.continuemenu.SaveFileMenuItem;

public class FileSelectionTextWindow extends SelectionTextWindow {
	private boolean loading; // true if loading, false if saving
	
	public FileSelectionTextWindow(int x, int y, int width, int height, StartupNew m, String type) {
		super(x, y, width, height, m);
		switch(type) {
		case "load" : loading = true; break;
		case "save" : loading = false; break;
		}
		// TODO Auto-generated constructor stub
	}

	public void drawWindow(MainWindow m) {
		
	}
	
	public void add(MenuItem m, int i) {
		//adds a MenuItem at a particular index, to create dynamic STW with different row/col lengths
//		m.setX(currentOpenX);
//		m.setY(currentOpenY);
		if (!loading) {
			((SaveFileMenuItem) m).setSaving();
		}
		selections.add(new ArrayList<MenuItem>());
		selections.get(i).add(0,m);
	}
	
}
