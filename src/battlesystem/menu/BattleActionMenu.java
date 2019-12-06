package battlesystem.menu;

import java.util.ArrayList;

import battlesystem.options.BattleMenuSelectionTextWindow;
import gamestate.partymembers.PartyMember;
import menu.actionmenu.ActionMenu;
import system.SystemState;
import system.controller.InputController;

public class BattleActionMenu extends ActionMenu {
	private int index;
	
	public BattleActionMenu(SystemState m, ArrayList<PartyMember> party, int index) {
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
