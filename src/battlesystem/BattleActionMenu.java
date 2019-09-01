package battlesystem;

import java.util.ArrayList;

import actionmenu.ActionMenu;
import battlesystem.options.BattleMenuSelectionTextWindow;
import gamestate.PartyMember;
import global.InputController;
import menu.StartupNew;

public class BattleActionMenu extends ActionMenu {
	private int index;
	
	public BattleActionMenu(StartupNew m, ArrayList<PartyMember> party, int index) {
		super(m, party);
		this.index = index;
		// TODO Auto-generated constructor stub
	}
	
	public void onPop() {}

	public void createSTW() {
		stw = new BattleMenuSelectionTextWindow(state,party,index);
	}
	
	@Override
	public void update(InputController input) {
		super.update(input);
		menuItems.remove(moneyLabel);
	}

	public void setDoAnim(boolean b) {
		// TODO Auto-generated method stub
//		doAnim = b;
		backingBar.setY(0);
		moneyLabel.setY(16);
		buttonLabel.setY(0);
		stw.setY(0);
	}
}
