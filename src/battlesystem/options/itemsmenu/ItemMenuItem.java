package battlesystem.options.itemsmenu;

import battlesystem.BattleMenu;
import battlesystem.options.BattleAction;
import battlesystem.options.BattleSelectionTextWindow;
import font.SelectionTextWindow;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class ItemMenuItem extends MenuItem {
	private Item item;
	
	public ItemMenuItem(Item item, StartupNew state) {
		super(item.getName(), 0, 0, state);
		this.item = item;
	}
	
	public String execute() {
		//show the inventory menu
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		BattleSelectionTextWindow stw = new BattleSelectionTextWindow("vertical",0,0,6,6,state);
			stw.add(new UseMenuItem(state,item));
			stw.add(new HelpMenuItem(item,state));
		m.addToMenuItems(stw);
		stw.setKillWhenComplete();
		m.getCurrentAction().setIndexOfUse(item);
		return null;
	}
}
