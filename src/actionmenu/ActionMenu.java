package actionmenu;
import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.PartyMember;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class ActionMenu extends Menu {
	private PauseMenuSelectionWindow stw;
	private ArrayList<PartyMember> party;
	private TextLabel moneyLabel;
	private TextWindow buttonLabel;
	
	public ActionMenu(StartupNew m, ArrayList<PartyMember> party) {
		super(m);
		this.party = party;
		// TODO Auto-generated constructor stub
	}
	
	public void createMenu() {
		state.setSFX("openstart.wav");
		state.playSFX();
		stw = new PauseMenuSelectionWindow(state,party);
		addMenuItem(stw);
		state.getMenuStack().push(this);
		moneyLabel = new TextLabel("$" + state.getGameState().getFundsOnHand(),832,24,state);
		addMenuItem(moneyLabel);
		buttonLabel = new TextWindow(true,"Test",state.getMainWindow().getScreenWidth() - 400,0,2,0,state);
		addMenuItem(buttonLabel);
	}
	
	public void update(InputController input) {
		moneyLabel.setText("$" + state.getGameState().getFundsOnHand());
		buttonLabel.setText(stw.getSelectedItem().getText()); 
	}
	
}
