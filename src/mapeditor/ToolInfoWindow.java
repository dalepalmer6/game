package mapeditor;

import canvas.MainWindow;
import font.TextWindow;
import mapeditor.tools.MapTool;
import menu.MenuItem;
import menu.StartupNew;

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
	
	public ToolInfoWindow(StartupNew state, MapTool tool) {
		super("",1600,300,5,3,state);
		toolWindow = new TextWindow(true,"Tool Info",x,y,width,height,state);
		toolWindow.setIgnoreCodes();
	}
	
	public void draw(MainWindow m) {
		toolWindow.draw(m);
		if (tool != null) {
			tool.draw(m,toolWindow.getX(), toolWindow.getY());
		}
		
	}
	
}
