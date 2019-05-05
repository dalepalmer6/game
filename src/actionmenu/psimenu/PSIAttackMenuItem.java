package actionmenu.psimenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenu;
import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenuItem;
import battlesystem.BattleMenu;
import battlesystem.SelectTargetMenu;
import battlesystem.options.BattleAction;
import battlesystem.options.BattleSelectionTextWindow;
import battlesystem.options.itemsmenu.BattleEntitySelectItem;
import gamestate.BattleEntity;
import gamestate.Enemy;
import gamestate.PCBattleEntity;
import gamestate.PartyMember;
import gamestate.elements.psi.PSIAttack;
import menu.MenuItem;
import menu.StartupNew;

public class PSIAttackMenuItem extends MenuItem {
private PSIAttack psi;
ArrayList<PartyMember> party;
int index;
	
	public PSIAttackMenuItem(PSIAttack psi, int x, int y, StartupNew m, ArrayList<PartyMember> party, int index) {
		super(psi.getStage(),x,y,m);
		this.psi = psi;
		this.party = party;
		this.index = index;
		// TODO Auto-generated constructor stub
	}

	
	
	public PSIAttack getPSI() {
		return psi;
	}
	
	public String execute() {
		//if in battle, select a target, depends on the action type
		//0 1 target; 1 all allies; 2 one enemy; 3 all foes
		int ttype = psi.getTargetType();
		boolean all = false;
		boolean onParty = false;
		if (ttype == 0) {
			all = false;
			onParty = true;
		} else if (ttype == 1) {
			all = true;
			onParty = true;
		} else if (ttype == 2) {
			all = false;
			onParty = false;
		} else if (ttype == 3) {
			all = true;
			onParty = false;
		}
		if (state.inBattle) {
			//use in battle, needs a User and a Target
			BattleEntity user = state.battleMenu.getPartyMembers().get(index);
			state.battleMenu.setCurrentAction(new BattleAction(state));
			state.battleMenu.getCurrentAction().setUser(user);
			state.battleMenu.getCurrentAction().setAction("psi");
			state.battleMenu.getCurrentAction().setIndexOfUse(psi);
			SelectTargetMenu sem;
			if (onParty) {
				sem = new SelectTargetMenu(state,all,state.battleMenu.getPartyMembers());
			} else {
				sem = new SelectTargetMenu(state,all,state.battleMenu.getEnemies());
			}
			
			state.getMenuStack().push(sem);
		} else {
			PartyMember user = party.get(index);
			PartyMember target = null;
			if (party.size() == 1) {
				target = party.get(0);
				PartyMemberSelectMenuItem use = new PartyMemberSelectMenuItem(target,psi,user,state);
				use.execute();
				state.getMenuStack().pop();
			} else {
				//show the list of party members
				PartyMemberSelectMenu pmsm = new PartyMemberSelectMenu(state,party,psi,user);
				pmsm.createMenu();
				state.getMenuStack().push(pmsm);
			}
//			item.useOutOfBattle(user,target);
			return null;
		}
		return null;
	}
}
