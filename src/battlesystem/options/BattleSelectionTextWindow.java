package battlesystem.options;

import battlesystem.BattleMenu;
import font.SelectionTextWindow;
import global.InputController;
import menu.StartupNew;

public class BattleSelectionTextWindow extends SelectionTextWindow {

	public BattleSelectionTextWindow(String orient, int x, int y, int width, int height, StartupNew m) {
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
