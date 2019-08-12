package actionmenu.psimenu;

import java.util.ArrayList;

import actionmenu.PlayerInfoWindow;
import battlesystem.menu.psi.PSIAttackMenuItem;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.PartyMember;
import gamestate.elements.UsableOutOfBattle;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import gamestate.elements.psi.PSIAttackUsableOutOfBattle;
import global.InputController;
import menu.StartupNew;

public class PSIMenuOutOfBattle extends PlayerInfoWindow {
	private ArrayList<SelectionTextWindow> inventories;
	private TextWindow descWindow;
	
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
			for (PSIAttack psi : pm.getKnownPSIList()) {
				if (psi instanceof UsableOutOfBattle) {
					stw.add(new PSIAttackMenuItem(psi,psi.getFamily() + " " + psi.getStage(),0,0,state,party,index));
				}
			}
		}
		if (!state.inBattle) {
			menuTitle.setText("PSI");
		}
	}
	
	public PSIMenuOutOfBattle(StartupNew state, ArrayList<PartyMember> party) {
		this(state,party,false);
	}
	
	public PSIMenuOutOfBattle(StartupNew state, ArrayList<PartyMember> party,boolean selling) {
		super(state,party);
		inventories = new ArrayList<SelectionTextWindow>();
		for (int i = 0; i < party.size(); i++) {
			PartyMember pm = party.get(i);
			SelectionTextWindow stw = new SelectionTextWindow("horizontal",256,128,20,9,state);
			stw.setCurrentOpen(stw.getTextStartX()+96,stw.getTextStartY());
			stw.setTextStart(stw.getTextStartX()+96,stw.getTextStartY());
			stw.setSteps(680,0);
			stw.setDrawOnly(true);
			for (PSIAttack psi : pm.getKnownPSIList()) {
				if (psi instanceof UsableOutOfBattle) {
					stw.add(new PSIAttackMenuItem(psi,psi.getFamily() + " " + psi.getStage(),0,0,state,party,index));
				}
					
			}
			index++;
			inventories.add(stw);
			descWindow = new TextWindow(true," ",256,128+11*64,20,1,state);
			//add a TexturedMenuItem that draws the item's picture
			addMenuItem(descWindow);
		}
		index = 0;
		if (!state.inBattle) {
			menuTitle.setText("PSI");
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
		
		if (party.get(index).getItemsList().get(0).getId() != 0 && !inventories.get(index).drawOnly()) {
			descWindow.setText(((PSIAttackMenuItem) inventories.get(index).getSelectedItem()).getPSI().getDescription());
		} else {
			descWindow.setText("");
		}
		
		
		menuItems.removeAll(inventories);
		this.addToMenuItems(inventories.get(index));
		
		if (inventories.get(index).isEmpty()) {
			//add a menu item to the center saying "No Known Skills"
			inventories.get(index).setDrawOnly(true);
			return;
		}
		
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
