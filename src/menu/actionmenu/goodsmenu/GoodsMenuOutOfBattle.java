package menu.actionmenu.goodsmenu;

import java.util.ArrayList;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.actionmenu.PlayerInfoWindow;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.SystemState;
import system.controller.InputController;

public class GoodsMenuOutOfBattle extends PlayerInfoWindow {
	private ArrayList<SelectionTextWindow> inventories;
	private TextWindow descWindow;
	private boolean selling;
	
	public void reloadActionOnPop() {
//		inventories = new ArrayList<SelectionTextWindow>();
		int j = 0;
		for (SelectionTextWindow stw:  inventories) {
//			SelectionTextWindow base = new SelectionTextWindow(0,0,0,0,state);
			stw.clearSelections();
			stw.setCurrentOpen(stw.getTextStartX(),stw.getTextStartY());
//			stw.setTextStart(base.getTextStartX()+96,base.getTextStartY());
			stw.setSteps(680,0);
			stw.setDrawOnly(false);
			PartyMember pm = party.get(j++);
			for (Item item : pm.getItemsList()) {
				if (item.getId() == 0) {
					continue;
				}
				stw.add(new GoodsSelectMenuItem(item,index,state,party,selling));
			}
		}
		if (!state.inBattle) {
			menuTitle.setText("Goods");
		}
	}
	
	public GoodsMenuOutOfBattle(SystemState state, ArrayList<PartyMember> party) {
		this(state,party,false);
	}
	
	public GoodsMenuOutOfBattle(SystemState state, ArrayList<PartyMember> party,boolean selling) {
		super(state,party);
		this.selling = selling;
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
				stw.add(new GoodsSelectMenuItem(item,index,state,party,selling));
			}
			index++;
			inventories.add(stw);
			descWindow = new TextWindow(true," ",256,128+11*64,20,1,state);
			//add a TexturedMenuItem that draws the item's picture
			addMenuItem(descWindow);
		}
		index = 0;
		if (!state.inBattle) {
			menuTitle.setText("Goods");
		}
		
	}
	
	public void update(InputController input) {
		
		if (!backShouldExit && input.getSignals().get("BACK")) {
			inventories.get(index).setDrawOnly(true);
			backShouldExit = true;
			menuItems.add(invisSelectItem);
		}
		
		if (!state.inBattle) {
			index = invisSelectItem.getIndex();
			super.update(input);
		} else {
			index = state.battleMenu.getIndex()-1;
		}
		
		if (party.get(index).getItemsList().get(0).getId() != 0) {
			descWindow.setText(((GoodsSelectMenuItem) inventories.get(index).getSelectedItem()).getItem().getDescription());
		}
		
		
		menuItems.removeAll(inventories);
		this.addToMenuItems(inventories.get(index));
		
		if (!state.inBattle) {
			if (invisSelectItem.getCanLoadInventory()) {
				if (party.get(index).getItemsList().get(0).getId() != 0) {
					invisSelectItem.setCanLoadInventory(false);
					inventories.get(index).setDrawOnly(false);
					backShouldExit = false;
					menuItems.remove(invisSelectItem);
				}
			}
		} else {
			inventories.get(index).setDrawOnly(false);
		}
	}
	
}
