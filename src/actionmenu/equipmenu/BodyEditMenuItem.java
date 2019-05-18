package actionmenu.equipmenu;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class BodyEditMenuItem extends EquipmentEditMenuItem {
	
	public BodyEditMenuItem(StartupNew state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
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
