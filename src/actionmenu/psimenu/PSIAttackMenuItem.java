package actionmenu.psimenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenu;
import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenuItem;
import battlesystem.BattleMenu;
import battlesystem.SelectTargetMenu;
import battlesystem.options.BattleAction;
import battlesystem.options.BattleSelectionTextWindow;
import battlesystem.options.itemsmenu.BattleEntitySelectItem;
import font.SelectionTextWindow;
import gamestate.BattleEntity;
import gamestate.Enemy;
import gamestate.PCBattleEntity;
import gamestate.PartyMember;
import gamestate.elements.psi.PSIAttack;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;
import menu.TeleportDestinationMenuItem;

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
			if (psi.getTargetType() != -1) {
				if (party.size() == 1) {
					target = party.get(0);
					PartyMemberSelectMenuItem use = new PartyMemberSelectMenuItem(target,psi,user,state);
					use.execute();
					state.getMenuStack().pop();
				} else if (psi.getTargetType() == 0) {
					//show the list of party members
					PartyMemberSelectMenu pmsm = new PartyMemberSelectMenu(state,party,psi,user);
					pmsm.createMenu();
					state.getMenuStack().push(pmsm);
				} else if (psi.getTargetType() == 1) {
					//use on the whole party
					String result = "";
					result += user.getName() + " tried " + psi.getName() + ".[PROMPTINPUT]";
					result += psi.useOutOfBattle(user,party);
					state.setResultOfMenuToDisplay(result);
					state.setClearMenuStack();
				}
			} else {
				switch(psi.getActionType()) {
					case 20: 
						//teleport a
						Menu m = new Menu(state);
						SelectionTextWindow stw = new SelectionTextWindow(0,0,10,10,state);
						/*make this its own method and get the destinations from a different place*/
						stw.add(new TeleportDestinationMenuItem("Podunk",200,200,"podunk",state));
						stw.add(new TeleportDestinationMenuItem("Merrysville",200,200,"merrysville",state));
						stw.add(new TeleportDestinationMenuItem("Snowman",200,200,"snowman",state));
						stw.add(new TeleportDestinationMenuItem("Reindeer",200,200,"reindeer",state));
						m.addMenuItem(stw);
						state.getMenuStack().push(m);
						break;
					case 21:
						//teleport b
						break;
					case 22: 
						//telepathy
						break;
				}
			}
//			item.useOutOfBattle(user,target);
			return null;
		}
		return null;
	}
}
