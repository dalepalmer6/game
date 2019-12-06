package menu.actionmenu.equipmenu;

import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import system.SystemState;

public class EquipmentSelectMenuItem extends MenuItem {
	private Item item;
	private String type;
	private PartyMember pm;
	
	public EquipmentSelectMenuItem(SystemState state, Item item, PartyMember pm, String constraint) {
		super(item.getName(), 0,0,state);
		this.item = item;
		this.type = constraint;
		this.pm = pm;
	}
	
	public Item getItem() {
		return item;
	}
	
	public String execute() {
//		state.getMenuStack().pop();
		EquipsMenu em = ((EquipsMenu) state.getMenuStack().peek());
		em.reload();
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
