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
	
	public PartyMemberSelectMenu(StartupNew s, ArrayList<PartyMember> party, Item item, PartyMember user) {
		super(s);
		this.party = party;
		this.item =item;
		this.user = user;
	}
	
	public void createMenu() {
		SelectionTextWindow stw = new SelectionTextWindow(0,0,5,5,state);
		for (PartyMember pm : party) {
			stw.add(new PartyMemberSelectMenuItem(pm,item,user,state));
		}
		addMenuItem(stw);
	}
}
