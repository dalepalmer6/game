package mapeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import menu.MenuItem;
import menu.StartupNew;

public class SaveMapButton extends MenuItem {

	public SaveMapButton(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	
	public void saveMap() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		try {
			PrintWriter pw = new PrintWriter(new File(m.getMapId() + ".map"));
			pw.write(m.toString());
			pw.flush();
			pw.close();
		} catch(FileNotFoundException e) {
			
		} 
		System.out.println("Successfully saved.");
	}
	
	public void loadMap() {
		
	}
	
	public String execute() {
		System.out.println("Saving Map");
		saveMap();
		return null;
	}
}
