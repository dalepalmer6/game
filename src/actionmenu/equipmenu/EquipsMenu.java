package actionmenu.equipmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.GoodsSelectMenuItem;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.Menu;
import menu.StartupNew;

public class EquipsMenu extends Menu {
	private int index;
	private ArrayList<PartyMember> party;
	private TextLabel weaponLabel;
	private TextLabel bodyLabel;
	private TextLabel headLabel;
	private TextLabel otherLabel;
	public EquipsMenu(StartupNew m, ArrayList<PartyMember> party, int i) {
		super(m);
		index = i;
		this.party = party;
	}

	public void createMenu() {
//		SelectionTextWindow stw = new SelectionTextWindow(0,0,15,15,state);
		menuItems.clear();
		PartyMember pm = party.get(index);
		SelectionTextWindow stw = new EquipmentSelectionTextWindow(state,pm);
		int startY = stw.getTextStartY();
		int startX = stw.getTextStartX();
		int stepY = stw.getStepForwardY();
		stw.add(new WeaponEditMenuItem(state,pm.getEquips().get(0),pm));
		stw.add(new BodyEditMenuItem(state,pm.getEquips().get(1),pm));
		stw.add(new HeadEditMenuItem(state,pm.getEquips().get(2),pm));
		stw.add(new OtherEditMenuItem(state,pm.getEquips().get(3),pm));
		addMenuItem(stw);
		weaponLabel = new TextLabel("Weapon",startX,startY,state);
		bodyLabel = new TextLabel("Body",startX,startY + stepY,state);
		headLabel = new TextLabel("Head",startX,startY + 2*stepY,state);;
		otherLabel = new TextLabel("Other",startX,startY + 3*stepY,state);;
		addMenuItem(weaponLabel);
		addMenuItem(bodyLabel);
		addMenuItem(headLabel);
		addMenuItem(otherLabel);
	}
}
