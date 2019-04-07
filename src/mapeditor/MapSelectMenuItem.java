package mapeditor;

import menu.MenuItem;
import menu.StartupNew;

public class MapSelectMenuItem extends MenuItem {
	private String mapName;
	private Map map;
	private MapPreview mp;
	public MapSelectMenuItem(String name, MapPreview m, StartupNew state) {
		super(name,0,0,state);
		this.mapName = name;
		this.mp = m;
		this.map = m.getMap();
	}
	
	public String execute() {
		map.parseMap(1,this.mapName);
		mp.resetView();
		state.needToPop = true;
		return null;
	}
}
