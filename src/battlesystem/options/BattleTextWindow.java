package battlesystem.options;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;
import global.InputController;
import menu.StartupNew;

public class BattleTextWindow extends TextWindowWithPrompt {
	public BattleTextWindow(String s, int x, int y, int width, int height, StartupNew m) {
		super(s, x,y,width,height,m);
	}
	
	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		if (input.getSignals().get("CONFIRM")) {
			next();
		}
	}
	
	public void next() {
		//if there is more text, move the text up, and print until the next line or whatever the case may be
		//for now, just drop the box.
		text.setFreeze(false);
		if (text.getDrawState()) {
			state.getMenuStack().peek().setToRemove(this);
			((BattleMenu) state.getMenuStack().peek()).setGetNextPrompt();
		}
//		
	}
}
