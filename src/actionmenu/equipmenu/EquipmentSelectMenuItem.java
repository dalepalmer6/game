package actionmenu.equipmenu;

import gamestate.PartyMember;
import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class EquipmentSelectMenuItem extends MenuItem {
	private Item item;
	private String type;
	private PartyMember pm;
	
	public EquipmentSelectMenuItem(StartupNew state, Item item, PartyMember pm, String constraint) {
		super(item.getName(), 0,0,state);
		this.item = item;
		this.type = constraint;
		this.pm = pm;
	}
	
	public String execute() {
		state.getMenuStack().pop();
		int indexToEquip=0;
		switch(type) {
		case "WPN" : indexToEquip = 0; break;
		case "BDY" : indexToEquip = 1; break;
		case "HED" : indexToEquip = 2; break;
		case "OTR" : indexToEquip = 3; break;
		}
		pm.getEquips().set(indexToEquip,(EquipmentItem) item);
//		((EquipsMenu) state.getMenuStack().peek()).createMenu();
		return null;
	}
}
