package actionmenu.psimenu;

import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import actionmenu.goodsmenu.InvisibleMenuItem;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import gamestate.psi.PSIClassification;
import gamestate.psi.PSIFamily;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class PSIMenuSelectMember extends Menu {
	private SelectionTextWindow psiSTW;
	private SelectionTextWindow classificationWindow;
	private ArrayList<PartyMember> party;
	private ArrayList<SelectionTextWindow> psiLists;
	private InvisibleMenuItem invisSelectItem;
	private int index;
	private ArrayList<TextLabel> labels = new ArrayList<TextLabel>();
	private boolean lockedIn;
	
	public PSIMenuSelectMember(StartupNew state, ArrayList<PartyMember> party, int index) {
		super(state);
		this.party = party;
		this.index = index;
		this.lockedIn = true;
	}
	
	public PSIMenuSelectMember(StartupNew state, ArrayList<PartyMember> party) {
		super(state);
		this.party = party;
		index = 0;
	}
	
	public void update(InputController input) {
		this.setToRemove(psiSTW);
		if (!lockedIn) {
			index = invisSelectItem.getIndex();
		}
		
		PartyMember a = party.get(index);
		int classId = classificationWindow.getSelectedIndex();
		ArrayList<PSIFamily> psiFams = state.psiClassList.getPSIClassifications().get(classId).getFamilies();
		psiSTW = new PSISelectionTextWindow(192,0,12,4,state,a);
		psiSTW.createGrid(5,5);
		psiSTW.setDrawOnly(true);
		labels = new ArrayList<TextLabel>();
		this.addToMenuItems(psiSTW);
		long knownPSI = a.getKnownPSI();
		
		
		int x = 160;
		int y = 16;
		boolean atLeastOne = false;;
		int i = 0;
		int j = 0;
		for (PSIFamily pf : psiFams) {
			j = 0;
			for (PSIAttack attack : pf.getStages()) {
				psiSTW.setCurrentOpen(x,y);
				long comparator = (long) Math.pow(2,attack.getId());
				if ((comparator & knownPSI) == comparator) {
					atLeastOne = true;
					psiSTW.add(new PSIAttackMenuItem(attack,0,0,state,party,index),j,i);
				}
				x += 48;
				j++;
			}
			if (atLeastOne) {
				TextLabel tl = new TextLabel(pf.getName(),psiSTW.getX() + 16,y,state);
				labels .add(tl);
				addToMenuItems(tl);
				atLeastOne = false;
			}
			x = 160;
			y += 32;
			i++;
		}
		if (invisSelectItem.getCanLoadInventory()) {
			//create the same menu, but where the selections are reversed, ie you cant select the classification
			PSIPickMenu ppm = new PSIPickMenu(state,party,index,psiFams);
			ppm.createMenu();
			state.getMenuStack().push(ppm);
		}
	}
	
	public void createMenu() {
		classificationWindow = new SelectionTextWindow(0,0,4,4,state);
		addMenuItem(classificationWindow);
		for (PSIClassification psic : state.psiClassList.getPSIClassifications()) {
			classificationWindow.add(new ClassificationMenuItem(state,psic));
		}
		PartyMember pm = party.get(0);
		psiSTW = new PSISelectionTextWindow(192,0,12,4,state,pm);

		PartyMember a = party.get(index);
		int classId = classificationWindow.getSelectedIndex();
		ArrayList<PSIFamily> psiFams = state.psiClassList.getPSIClassifications().get(classId).getFamilies();
		psiSTW.createGrid(5,5);
		psiSTW.setDrawOnly(true);
		labels = new ArrayList<TextLabel>();
		this.addToMenuItems(psiSTW);
		long knownPSI = a.getKnownPSI();
		
		
		int x = 160;
		int y = 16;
		boolean atLeastOne = false;;
		int i = 0;
		int j = 0;
		for (PSIFamily pf : psiFams) {
			j = 0;
			for (PSIAttack attack : pf.getStages()) {
				psiSTW.setCurrentOpen(x,y);
				long comparator = (long) Math.pow(2,attack.getId());
				if ((comparator & knownPSI) == comparator) {
					atLeastOne = true;
					psiSTW.add(new PSIAttackMenuItem(attack,0,0,state,party,index),j,i);
				}
				x += 48;
				j++;
			}
			if (atLeastOne) {
				TextLabel tl = new TextLabel(pf.getName(),psiSTW.getX() + 16,y,state);
				labels .add(tl);
				addToMenuItems(tl);
				atLeastOne = false;
			}
			x = 160;
			y += 32;
			i++;
		}
		invisSelectItem = new InvisibleMenuItem(state,party.size());
		addMenuItem(invisSelectItem);
	}
}
