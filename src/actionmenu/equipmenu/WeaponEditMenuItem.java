package actionmenu.equipmenu;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class WeaponEditMenuItem extends EquipmentEditMenuItem {
	
	public WeaponEditMenuItem(StartupNew state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
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
