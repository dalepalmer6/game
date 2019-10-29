package battlesystem.menu.psi;

import java.util.ArrayList;

import actionmenu.PlayerInfoWindow;
import actionmenu.equipmenu.TextLabel;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import actionmenu.goodsmenu.InvisibleMenuItem;
import battlesystem.menu.InBattleWindow;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.psi.PSIAttack;
import gamestate.psi.PSIClassification;
import gamestate.psi.PSIFamily;
import global.InputController;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class PSIMenuInBattle extends InBattleWindow {
	private PSISelectionTextWindow psiSTW;
	private SelectionTextWindow classificationWindow;
	private ArrayList<TextLabel> labels = new ArrayList<TextLabel>();
	private int classId;
	private MenuItem lastClassWindowSelection;
	private int index;
	
	public PSIMenuInBattle(StartupNew state, PartyMember pm) {
		super(state);
		this.party = new ArrayList<PartyMember>();
		this.party.add(pm);
		this.index = 0;
		reload();
	}
	
	public void update(InputController input) {
		int x = 0;
		int maxY = 0;
		for (TextLabel tl : labels) {
			setToRemove(tl);
		}
		if (labels.size() <= psiSTW.getYEnd()) {
			maxY = labels.size();
		} else {
			maxY = psiSTW.getYEnd();
		}
		for (int i = psiSTW.getYStart(); i < maxY; i++) {
			MenuItem mi = labels.get(i);
			mi.setY(psiSTW.getY() + 32 + (x++)*48);
			addToMenuItems(mi);
		}
		
		if (!state.inBattle) {
			super.update(input);
		}
		
		if (!backShouldExit && input.getSignals().get("BACK")) {
			psiSTW.setDrawOnly(true);
			classificationWindow.setDrawOnly(false);
			backShouldExit = true;
		}
		
		index = state.battleMenu.getIndex()-1;
		
		if (lastClassWindowSelection != classificationWindow.getSelectedItem()) {
			menuItems.remove(psiSTW);
			enumPSIForMember();
		}
		
		lastClassWindowSelection = classificationWindow.getSelectedItem();

	}
	
	public void enterPSIMenu() {
//		invisSelectItem.setCanLoadInventory(false);
		classificationWindow.setDrawOnly(true);
		psiSTW.setDrawOnly(false);
		backShouldExit = false;
	}
	
	public void exitPSIMenu() {
		
	}
	
	public void reload() {
		menuItems.remove(classificationWindow);
		classificationWindow = new SelectionTextWindow(state.getMainWindow().getScreenWidth()/2 - (16/2)*72,32,2,3,state);
		addMenuItem(classificationWindow);
		classificationWindow.setSteps(48*2,24*2);
		for (PSIClassification psic : state.psiClassList.getPSIClassifications()) {
			classificationWindow.add(new ClassificationMenuItem(state,psic));
		}
		PartyMember pm = party.get(0);
		psiSTW = new PSISelectionTextWindow(state.getMainWindow().getScreenWidth()/2 - (8/2)*72,32,12,3,state,pm);
		psiSTW.setYSelectionSize(5);
		psiSTW.createGrid(5,15,288,psiSTW.getTextStartY());
		
//		enumPSIForMember();

	}
	
	public void enumPSIForMember() {
		this.pm = party.get(0);
		classId = classificationWindow.getSelectedIndex();
		ArrayList<PSIFamily> psiFams = state.psiClassList.getPSIClassifications().get(classId).getFamilies();
		psiSTW.clearSelections();
		psiSTW.createGrid(5,15,288,psiSTW.getTextStartY());
		if (backShouldExit) {
			psiSTW.setDrawOnly(true);
		}
		addToMenuItems(psiSTW);
		labels = new ArrayList<TextLabel>();
		
		psiSTW.setSteps(48*2,24*2);
		long knownPSI = this.pm.getKnownPSI();

		int x = 88;
		int y = psiSTW.getTextStartY();
		boolean atLeastOne = false;
		int i = 0;
		int j = 0;
		for (PSIFamily pf : psiFams) {
			j = 0;
			for (PSIAttack attack : pf.getStages()) {
				long comparator = (long) Math.pow(2,attack.getId());
				if ((comparator & knownPSI) == comparator) {
					atLeastOne = true;
					psiSTW.add(new PSIAttackMenuItem(attack,0,0,state,party,index),j,i);
				}
				j++;
			}
			if (atLeastOne) {
				TextLabel tl = new TextLabel(pf.getName(),(int)psiSTW.getX() + x,(int)psiSTW.getY()+y,state);
				labels.add(tl);
				addToMenuItems(tl);
				atLeastOne = false;
			}
			y += 24*2;
			i++;
		}
		
		if (psiSTW.isEmpty()) {
			psiSTW.setDrawOnly(true);
		}
		
	}
}
