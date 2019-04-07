package battlesystem.options;

import battlesystem.BattleMenu;
import battlesystem.options.itemsmenu.ItemMenuItem;
import font.SelectionTextWindow;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class Goods extends MenuItem{

	public Goods(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		//show the inventory menu
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		BattleSelectionTextWindow stw = new BattleSelectionTextWindow("vertical",0,0,10,10,state);
		for (Item i : state.items) {
			stw.add(new ItemMenuItem(i,state));
		}
		m.addToMenuItems(stw);
		stw.setKillWhenComplete();
		m.setCurrentAction(new BattleAction());
		m.getCurrentAction().setAction("item");
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
//		m.getCurrentAction().setTarget(null);
//		m.setDoneAction();
		return null;
	}
	
}
