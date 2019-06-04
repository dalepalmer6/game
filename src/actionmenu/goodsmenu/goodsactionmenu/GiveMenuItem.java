package actionmenu.goodsmenu.goodsactionmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenu;
import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenuItem;
import battlesystem.SelectTargetMenu;
import battlesystem.options.BattleAction;
import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class GiveMenuItem extends MenuItem {
	private Item item;
	private ArrayList<PartyMember> party;
	private int userIndex;
	
	public GiveMenuItem(Item item, StartupNew state, ArrayList<PartyMember> party, int partyIndex) {
		super("Give",0,0,state);
		this.item = item;
		this.party = party;
		this.userIndex = partyIndex;
	}
	
	public String execute() {
		PartyMember user = party.get(userIndex);
		PartyMember target = null;
		//show the list of party members
		PartyMemberSelectMenu pmsm = new PartyMemberSelectMenu(state,party,item,user);
		pmsm.setType("give");
		pmsm.createMenu();
		state.getMenuStack().push(pmsm);
		return null;
	} 
}
