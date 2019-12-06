package battlesystem.options.psimenu;

import battlesystem.BattleEntity;
import battlesystem.menu.BattleMenu;
import battlesystem.options.BattleSelectionTextWindow;
import battlesystem.options.itemsmenu.BattleEntitySelectItem;
import gamestate.elements.psi.PSIAttack;
import menu.MenuItem;
import system.SystemState;

public class PSIAttackMenuItem extends MenuItem {
	private PSIAttack psi;
	
	public PSIAttackMenuItem(PSIAttack psi, SystemState m) {
		super(psi.getName(),0,0,m);
		this.psi = psi;
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		BattleSelectionTextWindow stw = new BattleSelectionTextWindow("vertical",0, 0, 6, 6, state);
			if (psi.getTargetType() == 0) {
				//single party member
				for (BattleEntity member : m.getPartyMembers()) {
					stw.add(new BattleEntitySelectItem(member,psi,state));
				}
			} else if (psi.getTargetType() == 1) {
				//all party members
			} else if (psi.getTargetType() == 2) {
				for (BattleEntity e : m.getEnemies()) {
					stw.add(new BattleEntitySelectItem(e,psi,state));
				}
				//one foe
			} else if (psi.getTargetType() == 3) {
				//all foes
			}
		m.addToMenuItems(stw);
		stw.setKillWhenComplete();
		m.getCurrentAction().setIndexOfUse(psi);
		return null;
	}
	
}
