package mapeditor;

import entityedit.EntityEditor;
import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.StartupNew;

public class DoorEnumButton extends ButtonMenuItem {
	private DoorEnumerator doorEnum;
	
	public DoorEnumButton(int x, int y, StartupNew state) {
		super("EditDoor",x,y,64,31,state);
	}
	
	public String execute() {
//		doorEnum = new DoorEnumerator(((MapEditMenu)state.getMenuStack().peek()).getMapPreview().getMap().getEntities());
//		DoorEnumMenu menu = new DoorEnumMenu(state, doorEnum.getDoors());
//		state.getMenuStack().push(menu);
//		menu.createMenu();
		EntityEditor ee = new EntityEditor(state);
		ee.setMap(((MapEditMenu)state.getMenuStack().peek()).getMapPreview().getMap());
		return null;
	}
}
