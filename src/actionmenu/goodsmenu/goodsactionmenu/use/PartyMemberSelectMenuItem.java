package actionmenu.goodsmenu.goodsactionmenu.use;

import java.util.ArrayList;

import gamestate.PartyMember;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import menu.MenuItem;
import menu.StartupNew;

/*When selecting a party member to USE something on, PSI or Goods*/
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
		String result = "";
		state.setClearMenuStack();
		ArrayList<PartyMember> partyMems = new ArrayList<PartyMember>();
		if (!(item instanceof PSIAttack)) {
			result += "user.getName() + \"pulled out \" + item.getName() + \" and used it on \" + pm.getName() + \"[PROMPTINPUT]\"";
			partyMems.add(pm);
			result += item.useOutOfBattle(user,partyMems);
			state.setResultOfMenuToDisplay(result);
		} else if (item instanceof PSIAttack) {
			result += user.getName() + " tried " + item.getName() + ".[PROMPTINPUT]";
			partyMems.add(pm);
			result += item.useOutOfBattle(user,partyMems);
			state.setResultOfMenuToDisplay(result);
		}
		return null;
	}
	
}
