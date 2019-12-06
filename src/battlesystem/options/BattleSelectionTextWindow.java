package battlesystem.options;

import battlesystem.menu.BattleMenu;
import menu.windows.SelectionTextWindow;
import system.SystemState;
import system.controller.InputController;

public class BattleSelectionTextWindow extends SelectionTextWindow {

	public BattleSelectionTextWindow(String orient, int x, int y, int width, int height, SystemState m) {
		super(orient,x, y, width, height, m);
		// TODO Auto-generated constructor stub
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
