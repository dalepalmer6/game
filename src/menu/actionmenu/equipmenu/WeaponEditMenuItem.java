package menu.actionmenu.equipmenu;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class WeaponEditMenuItem extends EquipmentEditMenuItem {
	
	public WeaponEditMenuItem(SystemState state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
		super(state,equipped,pm,stw);
		constraint = "WPN";
	}
	
//	public String execute() {
//		//replace the stw with a new stw that follows this logic
//		EquipSelectFromInventory esfi = new EquipSelectFromInventory(state,pm);
//		esfi.createMenu("WPN");
//		state.getMenuStack().push(esfi);
//		return null;
//	}

}
