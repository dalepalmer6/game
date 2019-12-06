package menu.actionmenu.equipmenu;

import gamestate.partymembers.PartyMember;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class EquipmentSelectionTextWindow extends SelectionTextWindow {
	PartyMember activeMember;
	public EquipmentSelectionTextWindow(SystemState s, PartyMember m) {
		super("vertical",0,0,12,4,s);
		currentOpenX = 160; // fool around with this to get the right spacing between this and the headers for equipment
		activeMember = m;
	}
	
	public void updateAnim() {
		for (int i = 0; i < activeMember.getEquips().size(); i++) {
//			selections.get(i).setText(activeMember.getEquips().get(i).getName());//only sets the name, not sure if it matters
		}
	}
	
}
