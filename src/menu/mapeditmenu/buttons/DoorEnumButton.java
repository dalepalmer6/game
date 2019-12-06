package menu.mapeditmenu.buttons;

import entityedit.EntityEditor;
import menu.ButtonMenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;

public class DoorEnumButton extends ButtonMenuItem {
	
	public DoorEnumButton(int x, int y, SystemState state) {
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
