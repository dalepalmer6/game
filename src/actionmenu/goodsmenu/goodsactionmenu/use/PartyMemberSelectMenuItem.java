package actionmenu.goodsmenu.goodsactionmenu.use;

import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class PartyMemberSelectMenuItem extends MenuItem {
	private PartyMember pm;
	private Item item;
	private PartyMember user;
	
	public PartyMemberSelectMenuItem(PartyMember pm, Item item, PartyMember user, StartupNew state) {
		super(pm.getName(),0,0,state);
		this.pm = pm;
		this.item = item;
		this.user = user;
	}
	
	public String execute() {
		item.useOutOfBattle(user,pm);
		System.out.println("Using item " + item.getName() +  " by " + user.getName() + " on " + pm.getName());
		state.setClearMenuStack();
		state.setResultOfMenuToDisplay("Using item " + item.getName() +  " by " + user.getName() + " on " + pm.getName());
		return null;
	}
	
}
