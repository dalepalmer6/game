package actionmenu.equipmenu;

import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class WeaponEditMenuItem extends MenuItem {
	private Item equipped;
	private PartyMember pm;
	
	public WeaponEditMenuItem(StartupNew state, Item equipped, PartyMember pm) {
		super(equipped.getName(),0,0,state);
		this.equipped = equipped;
		this.pm = pm;
	}
	
	public String execute() {
		EquipSelectFromInventory esfi = new EquipSelectFromInventory(state,pm);
		esfi.createMenu("WPN");
		state.getMenuStack().push(esfi);
		return null;
	}

}
