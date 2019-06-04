package actionmenu.goodsmenu.goodsactionmenu.use;

import java.util.ArrayList;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.Menu;
import menu.StartupNew;

public class PartyMemberSelectMenu extends Menu {
	ArrayList<PartyMember> party;
	Item item;
	PartyMember user;
	String action;
	
	public PartyMemberSelectMenu(StartupNew s, ArrayList<PartyMember> party, Item item, PartyMember user) {
		super(s);
		this.party = party;
		this.item =item;
		this.user = user;
		action = "use";
	}
	
	public void setType(String type) {
		switch(type) {
		case "give": action = "give"; break;
		}
	}
	
	public void createMenu() {
		SelectionTextWindow stw = new SelectionTextWindow(0,0,5,5,state);
		for (PartyMember pm : party) {
			if (action.equals("give")) {
				if (pm.getOpenInventorySpace() != -1) {
					stw.add(new PartyMemberSelectMenuItem(action,pm,item,user,state));
				}
			}
			else if (action.equals("use")) {
				stw.add(new PartyMemberSelectMenuItem(action,pm,item,user,state));
			}
			
		}
		addMenuItem(stw);
	}
}
