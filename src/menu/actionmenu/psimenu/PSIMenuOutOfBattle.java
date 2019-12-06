package menu.actionmenu.psimenu;

import java.util.ArrayList;

import battlesystem.menu.psi.PSIAttackMenuItem;
import gamestate.elements.UsableOutOfBattle;
import gamestate.elements.psi.PSIAttack;
import gamestate.partymembers.PartyMember;
import menu.actionmenu.PlayerInfoWindow;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.MotherSystemState;
import system.SystemState;
import system.controller.InputController;

public class PSIMenuOutOfBattle extends PlayerInfoWindow {
	private ArrayList<SelectionTextWindow> inventories;
	private TextWindow descWindow;
	
	public void reloadActionOnPop() {
//		inventories = new ArrayList<SelectionTextWindow>();
		MotherSystemState state = (MotherSystemState) this.state;
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
	
	public PSIMenuOutOfBattle(MotherSystemState state, ArrayList<PartyMember> party) {
		this(state,party,false);
	}
	
	public PSIMenuOutOfBattle(MotherSystemState state, ArrayList<PartyMember> party,boolean selling) {
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
		MotherSystemState state = (MotherSystemState) this.state;
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
