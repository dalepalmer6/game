package actionmenu.equipmenu;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class HeadEditMenuItem extends EquipmentEditMenuItem {
	
	public HeadEditMenuItem(StartupNew state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
		super(state,equipped,pm,stw);
		constraint = "HED";
	}
	
//	public String execute() {
//		EquipSelectFromInventory esfi = new EquipSelectFromInventory(state,pm);
//		esfi.createMenu("HED");
//		state.getMenuStack().push(esfi);
//		return null;
//	}
}
