package actionmenu.equipmenu;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class EquipmentEditMenuItem extends MenuItem{
	protected Item equipped;
	protected PartyMember pm;
	protected String constraint;
	protected SelectionTextWindow parentSTW;
	
	public EquipmentEditMenuItem(StartupNew state, Item equipped, PartyMember pm, SelectionTextWindow stw) {
		super(equipped.getName(),0,0,state);
		this.equipped = equipped;
		this.pm = pm;
		parentSTW = stw;
	}
	
	public String execute() {
		SelectionTextWindow stw = new SelectionTextWindow(256+640,128,10,9,state);
		boolean atLeastOne = false;
		for (Item i : pm.getItemsList())  {
			if (i.getEquipmentType().equals(constraint)) {
				atLeastOne = true;
				stw.add(new EquipmentSelectMenuItem(state,i,pm,constraint));
			}
		}
		if (atLeastOne) {
			((EquipsMenu)state.getMenuStack().peek()).setSearchInventorySTW(stw);
		}
		return null;
	}

	public Item getItem() {
		// TODO Auto-generated method stub
		return equipped;
	}
}
