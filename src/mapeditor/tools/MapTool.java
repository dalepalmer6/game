package mapeditor.tools;

import font.TextWindow;
import global.InputController;
import mapeditor.Map;

public class MapTool {
	protected Map map;
	protected String toolInfo;
	
	public String getToolInfo() {
		return toolInfo;
	}
	
	public void setMap(Map m) {
		this.map = m;
	}
	
	public MapTool() {
		toolInfo = "Tool Info";
	}
	
	public void doActionOnMap(int x, int y, int xMouse, int yMouse) {
		
	}
	
	public void doActionOnMapRightClick(int x, int y, int xMouse, int yMouse) {
		
	}
}
