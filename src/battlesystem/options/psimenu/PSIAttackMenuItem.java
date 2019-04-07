package battlesystem.options.psimenu;

import battlesystem.BattleMenu;
import battlesystem.options.BattleSelectionTextWindow;
import battlesystem.options.itemsmenu.BattleEntitySelectItem;
import battlesystem.options.itemsmenu.HelpMenuItem;
import battlesystem.options.itemsmenu.UseMenuItem;
import font.SelectionTextWindow;
import gamestate.Enemy;
import gamestate.PCBattleEntity;
import gamestate.PartyMember;
import gamestate.elements.psi.PSIAttack;
import menu.MenuItem;
import menu.StartupNew;

public class PSIAttackMenuItem extends MenuItem {
	private PSIAttack psi;
	
	public PSIAttackMenuItem(PSIAttack psi, StartupNew m) {
		super(psi.getName(),0,0,m);
		this.psi = psi;
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		BattleSelectionTextWindow stw = new BattleSelectionTextWindow("vertical",0, 0, 6, 6, state);
			if (psi.getTargetType() == 0) {
				//single party member
				for (PCBattleEntity member : m.getPartyMembers()) {
					stw.add(new BattleEntitySelectItem(member,psi,state));
				}
			} else if (psi.getTargetType() == 1) {
				//all party members
			} else if (psi.getTargetType() == 2) {
				for (Enemy e : m.getEnemies()) {
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
