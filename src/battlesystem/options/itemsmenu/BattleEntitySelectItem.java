package battlesystem.options.itemsmenu;

import battlesystem.BattleMenu;
import gamestate.BattleEntity;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class BattleEntitySelectItem extends MenuItem{
	private BattleEntity target;
	private Item item;
	
	public BattleEntitySelectItem(BattleEntity p, Item i, StartupNew m) {
		super(p.getName(),0,0,m);
		// TODO Auto-generated constructor stub
		target = p;
		item = i;
	}

	public String execute() {
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		m.getCurrentAction().setTarget(target);
		m.getCurrentAction().setIndexOfUse(item);
		m.setDoneAction();
		return null;
	}
	
	
}
