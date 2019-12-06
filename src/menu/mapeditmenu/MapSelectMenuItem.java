package menu.mapeditmenu;

import menu.MenuItem;
import menu.mapeditmenu.mappreview.MapPreview;
import system.SystemState;
import system.map.Map;

public class MapSelectMenuItem extends MenuItem {
	private String mapName;
	private Map map;
	private MapPreview mp;
	public MapSelectMenuItem(String name, MapPreview m, SystemState state) {
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
