package battlesystem.options;

import actionmenu.goodsmenu.GoodsMenu;
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
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		GoodsMenu gm = new GoodsMenu(state,state.getGameState().getPartyMembers(),m.getMemberIndex()-1);
		gm.createMenu();
		state.getMenuStack().push(gm);
		return null;
	}
	
}
