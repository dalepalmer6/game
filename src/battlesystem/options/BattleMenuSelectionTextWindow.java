package battlesystem.options;

import java.util.ArrayList;

import actionmenu.EquipMenuItem;
import actionmenu.GoodsMenuItem;
import actionmenu.PSIMenuItem;
import actionmenu.PauseMenuSelectionWindow;
import actionmenu.StatusMenuItem;
import battlesystem.BattleMenu;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import global.InputController;
import menu.StartupNew;

public class BattleMenuSelectionTextWindow extends PauseMenuSelectionWindow {
	private int index;
	
	public BattleMenuSelectionTextWindow(StartupNew m, ArrayList<PartyMember> party, int index) {
		super(m, party);
		this.index = index;
		createButtons();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void createButtons() {
		clearSelections();
		Bash bashButton = new Bash(state,party.get(index),0);
//		actionMenu.add(bashButton);
		GoodsMenuItem goodsButton = new GoodsMenuItem(state,party.get(index),0);
//		actionMenu.add(goodsButton);
		PSIMenuItem psiButton = new PSIMenuItem(state,party.get(index),0);
//		actionMenu.add(psiButton);
		Defend statusButton = new Defend(state,party.get(index),0);
//		actionMenu.add(statusButton);
		currentOpenX = 256;
		currentOpenY = (int) (y+16);
		add(bashButton);
		currentOpenX = 352;
		currentOpenY = (int) (y+16);
		add(goodsButton);
		currentOpenX = 448;
		currentOpenY = (int) (y+16);
		add(psiButton);
		currentOpenX = 544;
		currentOpenY = (int) (y+16);
		add(statusButton);
	}
	
	public void getPreviousWindow() {
		BattleMenu m = state.battleMenu;
		m.popWindowStackAndRemoveMI();
	}
	
	@Override
	public void handleInput(InputController input) {
			if (input.getSignals().get("UP")) {
				updateIndex("U");
			} else if (input.getSignals().get("DOWN")) {
				updateIndex("D");
			} else if (input.getSignals().get("RIGHT")) {
				updateIndex("R");
			}else if (input.getSignals().get("LEFT")) {
				updateIndex("L");
			} else if (input.getSignals().get("CONFIRM")) {
				update();
			} else if (input.getSignals().get("BACK")) {
				getPreviousWindow();
			}

	}

}
