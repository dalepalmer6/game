package actionmenu.psimenu;

import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.PartyMember;
import gamestate.elements.psi.PSIAttack;
import gamestate.psi.PSIClassification;
import gamestate.psi.PSIFamily;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class PSIPickMenu extends Menu {
	private int classId;
	PartyMember partyMember;
	private int indexInParty;
	private SelectionTextWindow psiSTW;
	private SelectionTextWindow classificationWindow;
	private TextWindow ppConsumptionWindow;
	private ArrayList<TextLabel> labels;
	private TextWindow descWindow;
	private ArrayList<PartyMember> party;
	private int index;
	private ArrayList<PSIFamily> psiFamilies;
	
	// PSIPickMenu(state,index,party,classId);
//	public PSIPickMenu(StartupNew state, int index, ArrayList<PartyMember> party, int classId) {
	public PSIPickMenu(StartupNew state, SelectionTextWindow classes, SelectionTextWindow psistw, ArrayList<TextLabel> labels) {
		super(state);
		psiSTW = psistw;
		classificationWindow = classes;
		psiSTW.setDrawOnly(false);
		classificationWindow.setDrawOnly(true);
		this.labels = labels;
	}
	
	
	public PSIPickMenu(StartupNew state, ArrayList<PartyMember> party, int index, ArrayList<PSIFamily> psiFams) {
		// TODO Auto-generated constructor stub
		super(state);
		this.partyMember = party.get(index);
		this.index = index;
		this.party = party;
		this.psiFamilies = psiFams;
	}


	public void update(InputController input) {
		this.setToRemove(descWindow);
		this.setToRemove(ppConsumptionWindow);
		String desc = ((PSIAttackMenuItem) psiSTW.getSelectedItem()).getPSI().getDescription();
		String ppUse = "Cost: " + ((PSIAttackMenuItem) psiSTW.getSelectedItem()).getPSI().getPPConsumption();
		ppConsumptionWindow = new TextWindow(true,ppUse,classificationWindow.getX(),classificationWindow.getY() + (classificationWindow.getHeight()+32)*4,2,1,state);
		descWindow = new TextWindow(true,desc,ppConsumptionWindow.getX() + (ppConsumptionWindow.getWidth()+32)*2,classificationWindow.getY()+(classificationWindow.getHeight()+32)*4,(classificationWindow.getWidth() + psiSTW.getWidth() - 32)*4/32 - (ppConsumptionWindow.getWidth()-32)*4/32,2,state);
		
		addToMenuItems(descWindow);
		addToMenuItems(ppConsumptionWindow);
	}
	
	public void createMenu() {
		addMenuItem(classificationWindow);
		addMenuItem(psiSTW);
		for (TextLabel tl : labels) {
			addMenuItem(tl);
		}
	}
	
//	public void createMenu() {
//		classificationWindow = new SelectionTextWindow(0,0,4,4,state);
//		classificationWindow.setDrawOnly(true);
//		addMenuItem(classificationWindow);
//		for (PSIClassification psic : state.psiClassList.getPSIClassifications()) {
//			classificationWindow.add(new ClassificationMenuItem(state,psic));
//		}
//		psiSTW = new PSISelectionTextWindow(192,0,12,4,state,partyMember);
////		int classId = classificationWindow.getSelectedIndex();
////		ArrayList<PSIFamily> psiFams = state.psiClassList.getPSIClassifications().get(classId).getFamilies();
//		psiSTW.createGrid(5,5);
//		psiSTW.setDrawOnly(false);
//		labels = new ArrayList<TextLabel>();
//		this.addMenuItem(psiSTW);
//		long knownPSI = partyMember.getKnownPSI();
//		
//		
//		int x = 160;
//		int y = 16;
//		boolean atLeastOne = false;;
//		int i = 0;
//		int j = 0;
//		for (PSIFamily pf : psiFamilies) {
//			j = 0;
//			for (PSIAttack attack : pf.getStages()) {
//				psiSTW.setCurrentOpen(x,y);
//				long comparator = (long) Math.pow(2,attack.getId());
//				if ((comparator & knownPSI) == comparator) {
//					atLeastOne = true;
//					psiSTW.add(new PSIAttackMenuItem(attack,0,0,state,party,index),j,i);
//				}
//				x += 48 * 2;
//				j++;
//			}
//			if (atLeastOne) {
//				TextLabel tl = new TextLabel(pf.getName(),psiSTW.getX() + 16,y,state);
//				labels .add(tl);
//				addMenuItem(tl);
//				atLeastOne = false;
//			}
//			x = 160;
//			y += 32 * 2;
//			i++;
//		}
//	}
}
