package battlesystem.options;

import font.SelectionTextWindow;
import menu.StartupNew;

public class BattleMenuSelectionTextWindow extends BattleSelectionTextWindow {
	public BattleMenuSelectionTextWindow(int x, int y, int width, int height, StartupNew m) {
		super("horizontal",x,y,width,height,m);
		stepForwardX = 120;
		stepForwardY = 64;
	}
}
