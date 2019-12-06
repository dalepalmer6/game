package battlesystem.options;

import battlesystem.menu.BattleMenu;
import battlesystem.menu.psi.PSIMenuSelectMember;
import battlesystem.menu.psi.PSIPickMenu;
import battlesystem.options.itemsmenu.ItemMenuItem;
import battlesystem.options.psimenu.PSIAttackMenuItem;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import menu.MenuItem;
import menu.actionmenu.goodsmenu.GoodsMenu;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class PSI extends MenuItem{

	public PSI(String t, int x, int y, SystemState m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	public String execute() {
//		//show the PSI attack menu
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		PSIMenuSelectMember gm = new PSIMenuSelectMember(state,state.getGameState().getPartyMembers(),m.getMemberIndex()-1);
		gm.createMenu();
		state.getMenuStack().push(gm);
		return null;
	}
	
}
