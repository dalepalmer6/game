package menu.mapeditmenu;

import menu.MenuItem;
import menu.mapeditmenu.tools.MapTool;
import menu.windows.TextWindow;
import system.MainWindow;
import system.SystemState;

public class ToolInfoWindow extends MenuItem {
	private TextWindow toolWindow;
	private MapTool tool;
	
	public void setText(String s) {
		toolWindow.setText(s);
	}
	
	public void setToolWindow(TextWindow tw) {
		toolWindow = tw;
	}
	
	public void setTool(MapTool tool) {
		this.tool = tool;
	}
	
	public ToolInfoWindow(SystemState state, MapTool tool) {
		super("",1600,300,2,6,state);
		toolWindow = new TextWindow(true,"Tool Info",(int)x,(int)y,width,height,state);
		toolWindow.setIgnoreCodes();
	}
	
	public void draw(MainWindow m) {
		toolWindow.draw(m);
		if (tool != null) {
			tool.draw(m,(int)toolWindow.getX(), (int)toolWindow.getY());
		}
		
	}
	
}
