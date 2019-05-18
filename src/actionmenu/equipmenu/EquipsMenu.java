package actionmenu.equipmenu;

import java.util.ArrayList;

import actionmenu.PlayerInfoWindow;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import actionmenu.statusmenu.TextWindowWithLabels;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.EntityStats;
import gamestate.PartyMember;
import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import global.InputController;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class EquipsMenu extends PlayerInfoWindow {
	private TextLabel weaponLabel;
	private TextLabel bodyLabel;
	private TextLabel headLabel;
	private TextLabel otherLabel;
	private ArrayList<TextLabel> labels;
	private TextWindow descWindow;
	private ArrayList<SelectionTextWindow> inventories;
	private SelectionTextWindow stats;
	private boolean needEquipmentSTW;
	private SelectionTextWindow inventoryEquips;
	private ArrayList<TextLabel> diffLabels;
	private ArrayList<TextLabel> statsAndVals;
	
	public EquipsMenu(StartupNew m, ArrayList<PartyMember> party) {
		super(m,party);
		reload();
		addMenuItem(descWindow);
		addMenuItem(stats);
	}
	
	public void setNeedEquipmentSTW(boolean b) {
		needEquipmentSTW = b;
	}

	public void update(InputController input) {
		super.update(input);
		if (!backShouldExit && input.getSignals().get("BACK")) {
			needEquipmentSTW = true;
			inventories.get(index).setDrawOnly(false);
			inventoryEquips = null;
//			menuItems.remove(menuItems.size()-1);
			backShouldExit = true;
			menuItems.add(invisSelectItem);
		}
		
		if (statsAndVals != null) {
			menuItems.removeAll(statsAndVals);
		}
	
		statsAndVals = new ArrayList<TextLabel>();
		statsAndVals.add(new TextLabel("HP",288,160,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("HP") + "",544,160,state));
		statsAndVals.add(new TextLabel("PP",288,224,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("PP") + "",544,224,state));
		statsAndVals.add(new TextLabel("Offense",288,288,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("ATK") + "",544,288,state));
		statsAndVals.add(new TextLabel("Defense",288,352,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("DEF") + "",544,352,state));
		statsAndVals.add(new TextLabel("IQ",288,416,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("IQ") + "",544,416,state));
		statsAndVals.add(new TextLabel("Speed",288,480,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("SPD") + "",544,480,state));
		statsAndVals.add(new TextLabel("Vitality",288,544,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("VIT") + "",544,544,state));
		statsAndVals.add(new TextLabel("Guts",288,610,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("GUTS") + "",544,610,state));
		statsAndVals.add(new TextLabel("Luck",288,676,state));
		statsAndVals.add(new TextLabel(party.get(index).getStats().getStat("LUCK") + "",544,676,state));
		
		if (inventoryEquips != null) {
			EquipmentItem item = (EquipmentItem) ((EquipmentSelectMenuItem)inventoryEquips.getSelectedItem()).getItem();
			getHypotheticalStats(item);
		}
		
		for (TextLabel tl : statsAndVals) {
			addMenuItem(tl);
		}
		
		////enumerate the stats in the window
		
		
		party.get(index).getStats().getStat("ATK");
		party.get(index).getStats().getStat("DEF");
		party.get(index).getStats().getStat("IQ");
		party.get(index).getStats().getStat("SPD");
		party.get(index).getStats().getStat("VIT");
		party.get(index).getStats().getStat("GUTS");
		party.get(index).getStats().getStat("LUCK");
		
		SelectionTextWindow stw = inventories.get(index);
		stw.clearSelections();
		PartyMember pm = party.get(index);
		ArrayList<Item> items = pm.getEquips();
		stw.setCurrentOpen(stw.getTextStartX()+96,stw.getTextStartY() + 64);
//		stw.setTextStart(stw.getTextStartX()+32,stw.getTextStartY());
		stw.add(new WeaponEditMenuItem(state,items.get(0),pm,stw));
		stw.add(new BodyEditMenuItem(state,items.get(1),pm,stw));
		stw.add(new HeadEditMenuItem(state,items.get(2),pm,stw));
		stw.add(new OtherEditMenuItem(state,items.get(3),pm,stw));
		
		
		index = invisSelectItem.getIndex();
		if (party.get(index).getItemsList().get(0).getId() != 0) {
			descWindow.setText(((EquipmentEditMenuItem) inventories.get(index).getSelectedItem()).getItem().getDescription());
		}
		
		menuItems.removeAll(inventories);
		menuItems.removeAll(labels);
		if (needEquipmentSTW) {
			this.addToMenuItems(inventories.get(index));
			for (TextLabel tl : labels) {
				addToMenuItems(tl);
			}
		}
		
		if (invisSelectItem.getCanLoadInventory()) {
//			if (party.get(index).getItemsList().get(0).getId() != 0) {
				invisSelectItem.setCanLoadInventory(false);
				inventories.get(index).setDrawOnly(true);
				needEquipmentSTW = false;
				backShouldExit = false;
				menuItems.remove(invisSelectItem);
//			}
		}
	}
	
	public void getHypotheticalStats(EquipmentItem item) {
		PartyMember pm = party.get(index);
		EntityStats newVals = pm.seeStatDiffs(item);
		//add the labels for this
		
		statsAndVals.add(new TextLabel(newVals.getStat("HP") + "",800,160,state));
		statsAndVals.add(new TextLabel(newVals.getStat("PP") + "",800,224,state));
		statsAndVals.add(new TextLabel(newVals.getStat("ATK") + "",800,288,state));
		statsAndVals.add(new TextLabel(newVals.getStat("DEF") + "",800,352,state));
		statsAndVals.add(new TextLabel(newVals.getStat("IQ") + "",800,416,state));
		statsAndVals.add(new TextLabel(newVals.getStat("SPD") + "",800,480,state));
		statsAndVals.add(new TextLabel(newVals.getStat("VIT") + "",800,544,state));
		statsAndVals.add(new TextLabel(newVals.getStat("GUTS") + "",800,610,state));
		statsAndVals.add(new TextLabel(newVals.getStat("LUCK") + "",800,676,state));
		
	}

	public void reload() {
		inventoryEquips = null;
		for (MenuItem mi : menuItems) {
			setToRemove(mi);
		}
		super.reload();
		stats = new SelectionTextWindow(256,128,10,9,state);
		stats.setDrawOnly(true);
		addToMenuItems(stats);
		
		// TODO Auto-generated method stub
		SelectionTextWindow stw = new SelectionTextWindow(256+1280,128,10,9,state);
		inventories = new ArrayList<SelectionTextWindow>();
		for (int i = 0; i < party.size(); i++) {
			PartyMember pm = party.get(i);
			stw = new SelectionTextWindow(256+640,128,10,9,state);
			stw.setCurrentOpen(stw.getTextStartX()+96,stw.getTextStartY() + 64);
			stw.setTextStart(stw.getTextStartX()+32,stw.getTextStartY());
			stw.setSteps(0,128);
//			stw.setDrawOnly(true);
			
			stw.add(new WeaponEditMenuItem(state,pm.getEquips().get(0),pm,stw));
			stw.add(new BodyEditMenuItem(state,pm.getEquips().get(1),pm,stw));
			stw.add(new HeadEditMenuItem(state,pm.getEquips().get(2),pm,stw));
			stw.add(new OtherEditMenuItem(state,pm.getEquips().get(3),pm,stw));
		
			inventories.add(stw);
		}
		descWindow = new TextWindow(true," ",256,128+11*64,20,1,state);
		//add a TexturedMenuItem that draws the item's picture
		addToMenuItems(descWindow);
		menuTitle.setText("Goods");
		int startY = stw.getTextStartY();
		int startX = stw.getTextStartX()+16;
		int stepY = stw.getStepForwardY();
		weaponLabel = new TextLabel("Weapon",stw.getX() + startX,stw.getY() + startY,state);
		bodyLabel = new TextLabel("Body",stw.getX() + startX,stw.getY() + startY + stepY,state);
		headLabel = new TextLabel("Head",stw.getX() + startX,stw.getY() + startY + 2*stepY,state);
		otherLabel = new TextLabel("Other",stw.getX() + startX,stw.getY() + startY + 3*stepY,state);
		labels = new ArrayList<TextLabel>();
		labels.add(weaponLabel);
		labels.add(bodyLabel);
		labels.add(headLabel);
		labels.add(otherLabel);
		for (TextLabel tl : labels) {
			addToMenuItems(tl);
		}
//		menuItems.addAll(labels);
		needEquipmentSTW = true;
	}

	public void setSearchInventorySTW(SelectionTextWindow stw) {
		// TODO Auto-generated method stub
		inventoryEquips = stw;
		addToMenuItems(inventoryEquips);
	}
}
