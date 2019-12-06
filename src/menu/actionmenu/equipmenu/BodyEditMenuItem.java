package menu.actionmenu.equipmenu;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class BodyEditMenuItem extends EquipmentEditMenuItem {
	
	public BodyEditMenuItem(SystemState state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
		super(state, equipped, pm,stw);
		constraint = "BDY";
	}

//	public String execute() {
//		EquipSelectFromInventory esfi = new EquipSelectFromInventory(state,pm);
//		esfi.createMenu("BDY");
//		state.getMenuStack().push(esfi);
//		return null;
//	}
}
