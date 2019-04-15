package actionmenu;

import java.util.ArrayList;

import actionmenu.equipmenu.EquipmentSelectionTextWindows;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;

public class EquipMenuItem extends MenuItem {
	private ArrayList<PartyMember> party;
	
	public EquipMenuItem(StartupNew m, ArrayList<PartyMember> party) {
		super("Equip",0,0,m);
		// TODO Auto-generated constructor stub
		this.party = party;
	}
	
	public String execute() {
		state.setSFX("window.wav");
		state.playSFX();
		EquipmentSelectionTextWindows estw = new EquipmentSelectionTextWindows(state,party);
		estw.createMenu();
		state.getMenuStack().push(estw);
		return null;
	}

}
