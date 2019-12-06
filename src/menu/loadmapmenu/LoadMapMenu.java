package menu.loadmapmenu;

import java.util.ArrayList;

import menu.Menu;
import menu.mapeditmenu.MapSelectMenuItem;
import menu.mapeditmenu.mappreview.MapPreview;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.SystemState;

public class LoadMapMenu extends Menu {
	//select a system.map to load in the editor
	private MapPreview mapPrev;
	
	public void createMenu(ArrayList<String> mapNames) {
		state.getMenuStack().push(this);
		TextWindow tw = new TextWindow(true,"Select a Map to load in the editor",10,10,15,3,state);
		SelectionTextWindow stw = new SelectionTextWindow("vertical",250,250,30,10,state);
		stw.setSteps(352,0);
		for (String s : mapNames) {
			stw.add(new MapSelectMenuItem(s,mapPrev,state));
		}
		addMenuItem(tw);
		addMenuItem(stw);
	}
	
	public LoadMapMenu(MapPreview m,SystemState state) {
		super(state);
		this.mapPrev = m;
	}
	
}
