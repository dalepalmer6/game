package menu.actionmenu;

import java.util.ArrayList;

import gamestate.partymembers.PartyMember;
import menu.TexturedMenuItem;
import menu.actionmenu.equipmenu.EquipsMenu;
import system.MotherSystemState;
import system.SystemState;

public class EquipMenuItem extends TexturedMenuItem {
	private ArrayList<PartyMember> party;
	
	public EquipMenuItem(SystemState m, ArrayList<PartyMember> party, int ty) {
		super("Equip",0,ty,14*4,14*4,m,"menu.png",46,0,14,14);
		// TODO Auto-generated constructor stub
		this.party = party;
		targetY = 0;
	}
	
	public String execute() {
		MotherSystemState state = (MotherSystemState) this.state;
//		state.setSFX("window.wav");
//		state.playSFX();
//		EquipmentSelectionTextWindows estw = new EquipmentSelectionTextWindows(state,party);
//		estw.createMenu();
		EquipsMenu em = new EquipsMenu(state, party);
		state.getMenuStack().push(em);
		return null;
	}

}
