package actionmenu.psimenu;

import java.util.ArrayList;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.psi.PSIFamily;
import menu.StartupNew;

public class PSISelectionTextWindow extends SelectionTextWindow {
	
	PartyMember activeMember;
	ArrayList<PSIFamily> families;
	public PSISelectionTextWindow(int x, int y, int width, int height, StartupNew s, PartyMember m) {
		super("horizontal",x,y,width,height,s);
		currentOpenX = 224; // fool around with this to get the right spacing between this and the headers for equipment
		activeMember = m;
	}
	
}
