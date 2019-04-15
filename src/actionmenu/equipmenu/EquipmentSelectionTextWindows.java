package actionmenu.equipmenu;

import java.util.ArrayList;
import java.util.Set;

import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import actionmenu.goodsmenu.InvisibleMenuItem;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class EquipmentSelectionTextWindows extends Menu {
	private ArrayList<PartyMember> party;
	private ArrayList<SelectionTextWindow> inventories;
	private int index;
	private InvisibleMenuItem invisSelectItem;
	private TextLabel weaponLabel;
	private TextLabel bodyLabel;
	private TextLabel headLabel;
	private TextLabel otherLabel;
	
	public EquipmentSelectionTextWindows(StartupNew m,ArrayList<PartyMember> party) {
		super(m);
		this.party=party;
	}

	public void update() {
		this.setToRemove(inventories.get(index));
		index = invisSelectItem.getIndex();
		addToMenuItems(inventories.get(index));
		addToMenuItems(weaponLabel);
		addToMenuItems(bodyLabel);
		addToMenuItems(headLabel);
		addToMenuItems(otherLabel);
		if (invisSelectItem.getCanLoadInventory()) {
			EquipsMenu gm = new EquipsMenu(state,party,index);
			gm.createMenu();
			state.getMenuStack().push(gm);
		}
	}
	
	public void createMenu() {
		SelectionTextWindow stw=null;
		menuItems.clear();
		inventories = new ArrayList<SelectionTextWindow>();
		for (int i = 0; i < party.size(); i++) {
			PartyMember pm = party.get(i);
			stw = new EquipmentSelectionTextWindow(state,pm);
			stw.setDrawOnly(true);
			stw.add(new WeaponEditMenuItem(state,pm.getEquips().get(0),pm));
			stw.add(new BodyEditMenuItem(state,pm.getEquips().get(1),pm));
			stw.add(new HeadEditMenuItem(state,pm.getEquips().get(2),pm));
			stw.add(new OtherEditMenuItem(state,pm.getEquips().get(3),pm));
			inventories.add(stw);
		}
		invisSelectItem = new InvisibleMenuItem(state,party.size());
		int startX = stw.getTextStartX();
		int startY = stw.getTextStartY();
		int stepY = stw.getStepForwardY();
		weaponLabel = new TextLabel("Weapon",startX,startY,state);
		bodyLabel = new TextLabel("Body",startX,startY + stepY,state);
		headLabel = new TextLabel("Head",startX,startY + 2*stepY,state);;
		otherLabel = new TextLabel("Other",startX,startY + 3*stepY,state);;
		addMenuItem(invisSelectItem);
		
		
	}
}
