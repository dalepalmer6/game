package battlesystem.options;

import font.SelectionTextWindow;
import menu.StartupNew;

public class PSIAttackSelectionTextWindow extends BattleSelectionTextWindow {
	public PSIAttackSelectionTextWindow(StartupNew m) {
		super("horizontal",300,0,20,20,m);
		stepForwardX = 250;
		stepForwardY = 32;
	}
}
