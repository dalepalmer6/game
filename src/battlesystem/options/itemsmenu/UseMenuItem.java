package battlesystem.options.itemsmenu;

import battlesystem.BattleMenu;
import battlesystem.options.BattleAction;
import battlesystem.options.BattleSelectionTextWindow;
import font.SelectionTextWindow;
import gamestate.PCBattleEntity;
import gamestate.elements.UsableInBattle;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class UseMenuItem extends MenuItem{
	private Item item;
	
	public UseMenuItem(StartupNew state, Item i) {
		super("Use",0,0,state);
		item = i;
	}
	
	public String execute() {
		//show the inventory menu
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		BattleSelectionTextWindow stw = new BattleSelectionTextWindow("vertical",0,0,6,6,state);
		if (item instanceof UsableInBattle) {
			if (item.getTargetType() == 0) {
				//select a party member
				for (PCBattleEntity pc : m.getPartyMembers()) {
					stw.add(new BattleEntitySelectItem(pc,item,state));
				}
			}
		} else {
			//give prompt cant use this item bah
		}
		
		m.addToMenuItems(stw);
		stw.setKillWhenComplete();
		return null;
	}
}
