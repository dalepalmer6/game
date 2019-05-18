package actionmenu.goodsmenu;

import java.util.ArrayList;

import actionmenu.PlayerInfoWindow;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import global.InputController;
import menu.StartupNew;

public class GoodsMenuOutOfBattle extends PlayerInfoWindow {
	private ArrayList<SelectionTextWindow> inventories;
	private TextWindow descWindow;
	
	public GoodsMenuOutOfBattle(StartupNew state, ArrayList<PartyMember> party) {
		super(state,party);
		inventories = new ArrayList<SelectionTextWindow>();
		for (int i = 0; i < party.size(); i++) {
			PartyMember pm = party.get(i);
			SelectionTextWindow stw = new SelectionTextWindow("horizontal",256,128,20,9,state);
			stw.setCurrentOpen(stw.getTextStartX()+96,stw.getTextStartY());
			stw.setTextStart(stw.getTextStartX()+96,stw.getTextStartY());
			stw.setSteps(680,0);
			stw.setDrawOnly(true);
			for (Item item : pm.getItemsList()) {
				if (item.getId() == 0) {
					continue;
				}
				stw.add(new GoodsSelectMenuItem(item,index,state,party));
			}
			inventories.add(stw);
			descWindow = new TextWindow(true," ",256,128+11*64,20,1,state);
			//add a TexturedMenuItem that draws the item's picture
			addMenuItem(descWindow);
		}
		menuTitle.setText("Goods");
	}
	
	public void update(InputController input) {
		super.update(input);
		if (!backShouldExit && input.getSignals().get("BACK")) {
			inventories.get(index).setDrawOnly(true);
			backShouldExit = true;
			menuItems.add(invisSelectItem);
		}
		
		index = invisSelectItem.getIndex();
		if (party.get(index).getItemsList().get(0).getId() != 0) {
			descWindow.setText(((GoodsSelectMenuItem) inventories.get(index).getSelectedItem()).getItem().getDescription());
		}
		
		menuItems.removeAll(inventories);
		this.addToMenuItems(inventories.get(index));
		
		if (invisSelectItem.getCanLoadInventory()) {
			if (party.get(index).getItemsList().get(0).getId() != 0) {
				invisSelectItem.setCanLoadInventory(false);
				inventories.get(index).setDrawOnly(false);
				backShouldExit = false;
				menuItems.remove(invisSelectItem);
			}
		}
	}
	
}
