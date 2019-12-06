package battlesystem.menu.psi;

import java.util.ArrayList;

import battlesystem.BattleEntity;
import battlesystem.menu.SelectTargetMenu;
import battlesystem.options.BattleAction;
import gamestate.elements.psi.PSIAttack;
import gamestate.partymembers.PartyMember;
import menu.Menu;
import menu.MenuItem;
import menu.actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenu;
import menu.actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenuItem;
import menu.teleportmenu.TeleportDestinationMenuItem;
import menu.windows.SelectionTextWindow;
import system.MotherSystemState;
import system.SystemState;

public class PSIAttackMenuItem extends MenuItem {
private PSIAttack psi;
ArrayList<PartyMember> party;
int index;
	
	public PSIAttackMenuItem(PSIAttack psi, int x, int y, SystemState m, ArrayList<PartyMember> party, int index) {
		super(psi.getStage(),x,y,m);
		this.psi = psi;
		this.party = party;
		this.index = index;
		// TODO Auto-generated constructor stub
	}

	public PSIAttackMenuItem(PSIAttack psi, String fullName, int x, int y, SystemState m, ArrayList<PartyMember> party, int index) {
		super(fullName,x,y,m);
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
		MotherSystemState state = (MotherSystemState) this.state;
		if (state.inBattle) {
			//use in battle, needs a User and a Target
			BattleEntity user = state.battleMenu.getPartyMembers().get(index);
			if (user.hasEnoughPP(psi.getPPConsumption())) {
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
			}
		} else {
			PartyMember user = party.get(index);
			if (user.hasEnoughPP(psi.getPPConsumption())) {
				PartyMember target = null;
				if (psi.getTargetType() != -1) {
					if (party.size() == 1) {
						target = party.get(0);
						PartyMemberSelectMenuItem use = new PartyMemberSelectMenuItem("use",target,psi,user,state);
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
						result += "[PLAYSFX_psicast.wav]" + user.getName() + " tried " + psi.getName() + ".[PROMPTINPUT]";
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
							stw.add(new TeleportDestinationMenuItem("My Home",338*4,266*4,"podunk",state));
							stw.add(new TeleportDestinationMenuItem("Mother's Day",1544*4,3168*4,"podunk",state));
							stw.add(new TeleportDestinationMenuItem("Thanksgiving",1735*4,2161*4,"merrysville",state));
							stw.add(new TeleportDestinationMenuItem("Reindeer",1675*4,1613*4,"reindeer",state));
							stw.add(new TeleportDestinationMenuItem("Halloween",2015*4,1591*4,"spookane",state));
							stw.add(new TeleportDestinationMenuItem("Snowman",2313*4,1020*4,"snowman",state));
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
			
		} 
//			item.useOutOfBattle(user,target);
			return null;
		}
		return null;
	}
}
