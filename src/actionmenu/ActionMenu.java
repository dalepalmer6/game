package actionmenu;
import java.util.ArrayList;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import menu.Menu;
import menu.StartupNew;

public class ActionMenu extends Menu {
	private SelectionTextWindow stw;
	private ArrayList<PartyMember> party;
	
	public ActionMenu(StartupNew m, ArrayList<PartyMember> party) {
		super(m);
		this.party = party;
		// TODO Auto-generated constructor stub
	}
	
	public void createMenu() {
		state.setSFX("window.wav");
		state.playSFX();
		stw = new SelectionTextWindow(0,0,8,5,state);
			stw.add(new TalkToMenuItem(state,stw));
			stw.add(new GoodsMenuItem(state,party));
			stw.add(new EquipMenuItem(state,party));
			stw.add(new PSIMenuItem(state,party));
			stw.add(new StatusMenuItem(state,party));
			stw.add(new CheckMenuItem(state));
		addMenuItem(stw);
		state.getMenuStack().push(this);
	}
	
}
