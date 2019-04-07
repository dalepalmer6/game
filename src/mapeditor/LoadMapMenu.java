package mapeditor;

import java.util.ArrayList;

import font.SelectionTextWindow;
import font.TextWindow;
import menu.Menu;
import menu.StartupNew;

public class LoadMapMenu extends Menu {
	//select a map to load in the editor
	private MapPreview mapPrev;
	
	public void createMenu(ArrayList<String> mapNames) {
		state.getMenuStack().push(this);
		TextWindow tw = new TextWindow(true,"Select a Map to load in the editor",10,10,15,3,state);
		SelectionTextWindow stw = new SelectionTextWindow("vertical",250,250,30,30,state);
		for (String s : mapNames) {
			stw.add(new MapSelectMenuItem(s,mapPrev,state));
		}
		addMenuItem(tw);
		addMenuItem(stw);
	}
	
	public LoadMapMenu(MapPreview m,StartupNew state) {
		super(state);
		this.mapPrev = m;
	}
	
}
