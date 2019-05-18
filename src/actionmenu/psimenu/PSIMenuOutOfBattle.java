package actionmenu.psimenu;

import java.util.ArrayList;

import actionmenu.PlayerInfoWindow;
import actionmenu.equipmenu.TextLabel;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import actionmenu.goodsmenu.InvisibleMenuItem;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.psi.PSIAttack;
import gamestate.psi.PSIClassification;
import gamestate.psi.PSIFamily;
import global.InputController;
import menu.StartupNew;

public class PSIMenuOutOfBattle extends PlayerInfoWindow {
	private SelectionTextWindow psiSTW;
	private SelectionTextWindow classificationWindow;
//	private ArrayList<SelectionTextWindow> psiLists;
	private ArrayList<TextLabel> labels = new ArrayList<TextLabel>();
	private boolean lockedIn;
	private PartyMember currentPartyMember;
	private int classId;
	
	public PSIMenuOutOfBattle(StartupNew state, ArrayList<PartyMember> party) {
		super(state,party);
		this.party = party;
		this.index = 0;
		this.lockedIn = true;
		reload();
		addMenuItem(invisSelectItem);
		menuItems.add(psiSTW);
		addMenuItem(classificationWindow);
		menuItems.addAll(labels);
		
	}
	
	public void update(InputController input) {
		super.update(input);
		if (!backShouldExit && input.getSignals().get("BACK")) {
			psiSTW.setDrawOnly(true);
			classificationWindow.setDrawOnly(false);
			backShouldExit = true;
			menuItems.add(invisSelectItem);
		}
//		boolean updateSTW = false;
//		if (invisSelectItem.getIndex() != index) {
//			updateSTW = true;
//		}
		
		index = invisSelectItem.getIndex();
		if (party.get(index).getItemsList().get(0).getId() != 0) {
//			descWindow.setText(((PSIAttackMenuItem) psiSTW.getSelectedItem()).getPSI().getDescription());
		}
		
		for (TextLabel tl : labels) {
			setToRemove(tl);
		}
		
		menuItems.remove(psiSTW);
		enumPSIForMember();
		
//		this.addToMenuItems(psiSTW);
		
		if (invisSelectItem.getCanLoadInventory()) {
			if (party.get(index).getItemsList().get(0).getId() != 0) {
				invisSelectItem.setCanLoadInventory(false);
				classificationWindow.setDrawOnly(true);
				psiSTW.setDrawOnly(false);
				backShouldExit = false;
				menuItems.remove(invisSelectItem);
			}
		}
		
		for (TextLabel tl : labels) {
			addToMenuItems(tl);
		}
	}
	
	@Override
	public void reload() {
		super.reload();
		classificationWindow = new SelectionTextWindow(256,128,3,4,state);
		addMenuItem(classificationWindow);
		for (PSIClassification psic : state.psiClassList.getPSIClassifications()) {
			classificationWindow.add(new ClassificationMenuItem(state,psic));
		}
		PartyMember pm = party.get(0);
		psiSTW = new PSISelectionTextWindow(256+192,128,15,10,state,pm);
		
		enumPSIForMember();
		
		invisSelectItem = new InvisibleMenuItem(state,party.size());
		addToMenuItems(invisSelectItem);
	}
	
	public void enumPSIForMember() {
		currentPartyMember = party.get(index);
		classId = classificationWindow.getSelectedIndex();
		ArrayList<PSIFamily> psiFams = state.psiClassList.getPSIClassifications().get(classId).getFamilies();
		psiSTW.clearSelections();
		psiSTW.createGrid(5,5);
		if (backShouldExit) {
			psiSTW.setDrawOnly(true);
		}
		
		labels = new ArrayList<TextLabel>();
		this.addToMenuItems(psiSTW);
		long knownPSI = currentPartyMember.getKnownPSI();

		int x = 288;
		int y = 32;
		boolean atLeastOne = false;;
		int i = 0;
		int j = 0;
		for (PSIFamily pf : psiFams) {
			j = 0;
			for (PSIAttack attack : pf.getStages()) {
				psiSTW.setCurrentOpen(x+=32*2,y);
				long comparator = (long) Math.pow(2,attack.getId());
				if ((comparator & knownPSI) == comparator) {
					atLeastOne = true;
					psiSTW.add(new PSIAttackMenuItem(attack,0,0,state,party,index),j,i);
				}
				
				j++;
			}
			if (atLeastOne) {
				TextLabel tl = new TextLabel(pf.getName(),psiSTW.getX() + psiSTW.getTextStartX() + 16,psiSTW.getY()+y,state);
				labels .add(tl);
				addToMenuItems(tl);
				atLeastOne = false;
			}
			x = 288;
			y += 32*2;
			i++;
		}
	}
	
}
