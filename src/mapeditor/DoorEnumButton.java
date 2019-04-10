package mapeditor;

import menu.MenuItem;
import menu.StartupNew;

public class DoorEnumButton extends MenuItem {
	private DoorEnumerator doorEnum;
	
	public DoorEnumButton(int x, int y, StartupNew state) {
		super("",x,y,150,75,state);
	}
	
	public String execute() {
		doorEnum = new DoorEnumerator(((MapEditMenu)state.getMenuStack().peek()).getMapPreview().getMap().getEntities());
		DoorEnumMenu menu = new DoorEnumMenu(state, doorEnum.getDoors());
		state.getMenuStack().push(menu);
		menu.createMenu();
		return null;
	}
}
