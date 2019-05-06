package actionmenu.goodsmenu;

import java.util.ArrayList;

import canvas.Controllable;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import global.InputController;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class InventorySelectionTextWindows extends Menu implements Controllable {
	private ArrayList<PartyMember> party;
	private ArrayList<SelectionTextWindow> inventories;
	private int index;
	private InvisibleMenuItem invisSelectItem;
	
	public InventorySelectionTextWindows(StartupNew m,ArrayList<PartyMember> party) {
		super(m);
		this.party=party;
	}

	public void update(InputController input) {
		this.setToRemove(inventories.get(index));
		index = invisSelectItem.getIndex();
		this.addToMenuItems(inventories.get(index));
		if (invisSelectItem.getCanLoadInventory()) {
			GoodsMenu gm = new GoodsMenu(state,party,index);
			gm.createMenu();
			state.getMenuStack().push(gm);
		}
	}
	
	public void createMenu() {
		SelectionTextWindow stw=null;
		inventories = new ArrayList<SelectionTextWindow>();
		for (int i = 0; i < party.size(); i++) {
			PartyMember pm = party.get(i);
			stw = new SelectionTextWindow(0,0,15,15,state);
			stw.setDrawOnly(true);
			for (Item item : pm.getItemsList()) {
				stw.add(new GoodsSelectMenuItem(item,index,state,party));
			}
			inventories.add(stw);
		}
		invisSelectItem = new InvisibleMenuItem(state,party.size());
		addMenuItem(invisSelectItem);
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		
	}
	
}
