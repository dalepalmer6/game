package mapeditor;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.StartupNew;

public class DoorEnumButton extends ButtonMenuItem {
	private DoorEnumerator doorEnum;
	
	public DoorEnumButton(int x, int y, StartupNew state) {
		super("Door Edit",x,y,175,31,state);
	}
	
	public String execute() {
		doorEnum = new DoorEnumerator(((MapEditMenu)state.getMenuStack().peek()).getMapPreview().getMap().getEntities());
		DoorEnumMenu menu = new DoorEnumMenu(state, doorEnum.getDoors());
		state.getMenuStack().push(menu);
		menu.createMenu();
		return null;
	}
}
