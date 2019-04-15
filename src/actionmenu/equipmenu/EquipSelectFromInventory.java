package actionmenu.equipmenu;

import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.Menu;
import menu.StartupNew;

public class EquipSelectFromInventory extends Menu {
	private SelectionTextWindow stw;
	private TextWindow tw;
	private PartyMember pm;
	
	public EquipSelectFromInventory(StartupNew state,PartyMember pm) {
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
