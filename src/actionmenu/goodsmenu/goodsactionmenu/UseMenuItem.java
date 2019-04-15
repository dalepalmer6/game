package actionmenu.goodsmenu.goodsactionmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenu;
import battlesystem.SelectTargetMenu;
import battlesystem.options.BattleAction;
import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class UseMenuItem extends MenuItem {
	private Item item;
	private ArrayList<PartyMember> party;
	private int userIndex;
	
	public UseMenuItem(Item item, StartupNew state, ArrayList<PartyMember> party, int partyIndex) {
		super("Use",0,0,state);
		this.item = item;
		this.party = party;
		this.userIndex = partyIndex;
	}
	
	public String execute() {
		int ttype = item.getTargetType();
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
			BattleEntity user = state.battleMenu.getPartyMembers().get(userIndex);
			state.battleMenu.setCurrentAction(new BattleAction(state));
			state.battleMenu.getCurrentAction().setUser(user);
			state.battleMenu.getCurrentAction().setAction("item");
			state.battleMenu.getCurrentAction().setIndexOfUse(item);
			SelectTargetMenu sem = new SelectTargetMenu(state,all,state.battleMenu.getEnemies());
			state.getMenuStack().push(sem);
		} else {
			PartyMember user = party.get(userIndex);
			PartyMember target = null;
			if (party.size() == 1) {
				target = party.get(0);
			} else {
				//show the list of party members
				PartyMemberSelectMenu pmsm = new PartyMemberSelectMenu(state,party,item,user);
				pmsm.createMenu();
				state.getMenuStack().push(pmsm);
			}
//			item.useOutOfBattle(user,target);
			return null;
		}
		return null;
	} 
}
