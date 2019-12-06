package menu.actionmenu.goodsmenu.goodsactionmenu.use;

import java.util.ArrayList;

import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import system.SystemState;

/*When selecting a party member to USE something on, PSI or Goods*/
public class PartyMemberSelectMenuItem extends MenuItem {
	private PartyMember pm;
	private Item item;
	private PartyMember user;
	private String actionToDo;
	public PartyMemberSelectMenuItem(String act, PartyMember pm, Item item, PartyMember user, SystemState state) {
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
