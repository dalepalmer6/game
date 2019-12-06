package menu.actionmenu.equipmenu;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.Menu;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.SystemState;

public class EquipSelectFromInventory extends Menu {
	private SelectionTextWindow stw;
	private TextWindow tw;
	private PartyMember pm;
	
	public EquipSelectFromInventory(SystemState state,PartyMember pm) {
		super(state);
		this.pm = pm;
	}
	
//	public void update
	
	public void createMenu(String constraint) {
		stw = new SelectionTextWindow(0,0,5,10,state);
		for (Item i : pm.getItemsList())  {
			if (i.getEquipmentType().equals(constraint)) {
				stw.add(new EquipmentSelectMenuItem(state,i,pm,constraint));
			}
		}
		addMenuItem(stw);
	}
}
