package battlesystem.options;

import menu.windows.SelectionTextWindow;
import system.SystemState;

public class PSIAttackSelectionTextWindow extends BattleSelectionTextWindow {
	public PSIAttackSelectionTextWindow(SystemState m) {
		super("horizontal",300,0,20,20,m);
		stepForwardX = 250;
		stepForwardY = 32;
	}
}
