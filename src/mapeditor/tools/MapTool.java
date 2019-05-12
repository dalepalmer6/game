package mapeditor.tools;

import java.util.ArrayList;
import java.util.Collection;

import canvas.MainWindow;
import font.TextWindow;
import global.InputController;
import mapeditor.Map;
import menu.ButtonMenuItem;
import menu.StartupNew;

public class MapTool {
	protected Map map;
	protected String toolInfo;
	protected ArrayList<ButtonMenuItem> associatedButtons;
	private StartupNew state;
	
	public void draw(MainWindow m, int x, int y) {
		
	}
	
	public String getToolInfo() {
		return toolInfo;
	}
	
	public void setMap(Map m) {
		this.map = m;
	}
	
	public MapTool(StartupNew state) {
		this.state = state;
		toolInfo = "Tool Info";
		associatedButtons = new ArrayList<ButtonMenuItem>();
	}
	
	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
		
	}
	
	public void doActionOnMapRightClick(int x, int y, int xMouse, int yMouse) {
		
	}

	public void update(InputController input) {
		// TODO Auto-generated method stub
		
	}
	public void setButtons(StartupNew state) {}

	public ArrayList<ButtonMenuItem> getAssociatedButtons() {
		// TODO Auto-generated method stub
		return associatedButtons;
	}
}
