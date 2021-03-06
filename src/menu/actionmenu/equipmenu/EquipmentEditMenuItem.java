package menu.actionmenu.equipmenu;

import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class EquipmentEditMenuItem extends MenuItem{
	protected Item equipped;
	protected PartyMember pm;
	protected String constraint;
	protected SelectionTextWindow parentSTW;
	
	public enum MemberIndexes {
		NINTEN(16),
		ANA(32),
		LLOYD(64),
		TEDDY(128);
		
		private int index;
		
		private MemberIndexes(int val) {
			this.index = val;
		}
	}
	
	public enum EquipmentTypes {
		WEAPON(1),
		HEAD(2),
		BODY(4),
		OTHER(8);
		
		private int index;
		
		private EquipmentTypes(int index) {
			this.index = index;
		}
	}
	
	public EquipmentEditMenuItem(SystemState state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
		super(equipped.getName(),0,0,state);
		this.equipped = equipped;
		this.pm = pm;
		parentSTW = stw;
	}
	
	public String execute() {
		SelectionTextWindow stw = new SelectionTextWindow(256+640,128,10,9,state);
		boolean atLeastOne = false;
		for (Item i : pm.getItemsList())  {
			if (i.getEquipmentType().equals(constraint) && i.canPartyMemberEquip(pm.getId())) {
				atLeastOne = true;
				stw.add(new EquipmentSelectMenuItem(state,i,pm,constraint));
			}
//			((EquipsMenu)state.getMenuStack().peek()).setSearchInventorySTW(stw);
		}
		if (atLeastOne) {
			((EquipsMenu)state.getMenuStack().peek()).setSearchInventorySTW(stw);
		} else {
			((EquipsMenu)state.getMenuStack().peek()).setSearchInventorySTW(null);
		}
		return null;
	}

	public Item getItem() {
		// TODO Auto-generated method stub
		return equipped;
	}
}
