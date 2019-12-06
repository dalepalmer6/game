package menu.actionmenu.equipmenu;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class OtherEditMenuItem extends EquipmentEditMenuItem {
	
	public OtherEditMenuItem(SystemState state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
		super(state,equipped,pm,stw);
		constraint = "OTR";
	}
	
//	public String execute() {
//		EquipSelectFromInventory esfi = new EquipSelectFromInventory(state,pm);
//		esfi.createMenu("OTR");
//		state.getMenuStack().push(esfi);
//		return null;
//	}
}
