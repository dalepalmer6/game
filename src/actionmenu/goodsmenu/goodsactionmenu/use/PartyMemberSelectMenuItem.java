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
	private String actionToDo;
	public PartyMemberSelectMenuItem(String act, PartyMember pm, Item item, PartyMember user, StartupNew state) {
		super(pm.getName(),0,0,state);
		this.pm = pm;
		this.item = item;
		this.user = user;
		actionToDo = act;
	}
	
	public String execute() {
		String result = "";
		if (actionToDo.equals("give")) {
			pm.setItem(item,pm.getOpenInventorySpace());
			user.consumeItem(item);
			state.getMenuStack().pop();
			state.getMenuStack().pop();
		}
		if (actionToDo.equals("use")) {
			state.setClearMenuStack();
			ArrayList<PartyMember> partyMems = new ArrayList<PartyMember>();
			if (!(item instanceof PSIAttack)) {
				partyMems.add(pm);
				result += item.useOutOfBattle(user,partyMems);
				state.setResultOfMenuToDisplay(result);
			} else if (item instanceof PSIAttack) {
				result += "[PLAYSFX_psicast.wav]" + user.getName() + " tried " + item.getName() + ".[PROMPTINPUT]";
				partyMems.add(pm);
				result += item.useOutOfBattle(user,partyMems);
				state.setResultOfMenuToDisplay(result);
			}
		}
		return null;
	}
	
}
