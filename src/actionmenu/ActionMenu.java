package actionmenu;
import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.PartyMember;
import global.InputController;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class ActionMenu extends Menu {
	private PauseMenuSelectionWindow stw;
	private ArrayList<PartyMember> party;
	private TextLabel moneyLabel;
	private TextWindow buttonLabel;
	
	public ActionMenu(StartupNew m, ArrayList<PartyMember> party) {
		super(m);
		this.party = party;
		canRemove = false;
		// TODO Auto-generated constructor stub
	}
	
	public void onPop() {
		super.onPop();
		stw.setDrawOnly(true);
		for (MenuItem i : menuItems) {
			i.setTargetPosY(-128);
		}
		state.setSFX("closestart.wav");
	}
	
	public void createMenu() {
		state.setSFX("openstart.wav");
		state.playSFX();
		MenuItem backingBar = new BackingBar(state);
		backingBar.setY(-128);
		backingBar.setTargetPosY(0);
		addMenuItem(backingBar);
		stw = new PauseMenuSelectionWindow(state,party);
		stw.setTargetPosY(0);
		addMenuItem(stw);
		state.getMenuStack().push(this);
		moneyLabel = new TextLabel("$" + state.getGameState().getFundsOnHand(),832,-128+16,state);
		moneyLabel.setTargetPosY(16);
		addMenuItem(moneyLabel);
		buttonLabel = new TextWindow(true,"Test",128+128*10,-128,2,0,state);
		buttonLabel.setTargetPosY(0);
		addMenuItem(buttonLabel);
	}
	
	public void update(InputController input) {
		moneyLabel.setText("$" + state.getGameState().getFundsOnHand());
		buttonLabel.setText(stw.getSelectedItem().getText()); 
		if (canRemove) {
//			input.reset();
			state.getMenuStack().pop();
		}
	}
	
}
