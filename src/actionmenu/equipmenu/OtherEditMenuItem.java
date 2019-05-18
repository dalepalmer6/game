package actionmenu.equipmenu;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class OtherEditMenuItem extends EquipmentEditMenuItem {
	
	public OtherEditMenuItem(StartupNew state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
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
