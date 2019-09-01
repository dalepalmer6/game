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
	protected PauseMenuSelectionWindow stw;
	protected ArrayList<PartyMember> party;
	protected TextLabel moneyLabel;
	protected TextWindow buttonLabel;
	protected BackingBar backingBar;
	protected boolean doAnim;
	
	public ActionMenu(StartupNew m, ArrayList<PartyMember> party) {
		super(m);
		this.party = party;
		canRemove = false;
		doAnim = true;
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
	
	public void createSTW() {
		stw = new PauseMenuSelectionWindow(state,party);
	}
	
	public void createMenu() {
		state.setSFX("openstart.wav");
		state.playSFX();
		backingBar = new BackingBar(state);
		addMenuItem(backingBar);
		createSTW();
		addMenuItem(stw);
		state.getMenuStack().push(this);
		moneyLabel = new TextLabel("$" + state.getGameState().getFundsOnHand(),832,-128+16,state);
		addMenuItem(moneyLabel);
		buttonLabel = new TextWindow(true,"Test",128+128*10,-128,2,0,state);
		addMenuItem(buttonLabel);
//		if (doAnim) {
			backingBar.setY(-128);
			backingBar.setTargetPosY(0);
			moneyLabel.setTargetPosY(16);
			buttonLabel.setTargetPosY(0);
			stw.setTargetPosY(0);
//		} else {
//			
//		}
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
