package battlesystem.options;

import battlesystem.menu.BattleMenu;
import battlesystem.options.itemsmenu.ItemMenuItem;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.actionmenu.goodsmenu.GoodsMenu;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class Goods extends MenuItem{

	public Goods(String t, int x, int y, SystemState m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		GoodsMenu gm = new GoodsMenu(state,state.getGameState().getPartyMembers(),m.getMemberIndex()-1);
		gm.createMenu();
		state.getMenuStack().push(gm);
		return null;
	}
	
}
