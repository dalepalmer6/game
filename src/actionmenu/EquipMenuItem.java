package actionmenu;

import java.util.ArrayList;

import actionmenu.equipmenu.EquipmentSelectionTextWindows;
import actionmenu.equipmenu.EquipsMenu;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class EquipMenuItem extends TexturedMenuItem {
	private ArrayList<PartyMember> party;
	
	public EquipMenuItem(StartupNew m, ArrayList<PartyMember> party, int ty) {
		super("Equip",0,ty,96,96,m,"menu.png",46,0,14,14);
		// TODO Auto-generated constructor stub
		this.party = party;
		targetY = 0;
	}
	
	public String execute() {
//		state.setSFX("window.wav");
//		state.playSFX();
//		EquipmentSelectionTextWindows estw = new EquipmentSelectionTextWindows(state,party);
//		estw.createMenu();
		EquipsMenu em = new EquipsMenu(state, party);
		state.getMenuStack().push(em);
		return null;
	}

}
