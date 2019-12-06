package menu.actionmenu.equipmenu;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class HeadEditMenuItem extends EquipmentEditMenuItem {
	
	public HeadEditMenuItem(SystemState state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
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
