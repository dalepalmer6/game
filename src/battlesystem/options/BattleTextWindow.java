package battlesystem.options;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;
import global.InputController;
import menu.StartupNew;

public class BattleTextWindow extends TextWindowWithPrompt {
	private boolean loadAnimOnExit;
	private boolean completeActionOnExit;
	private boolean pollForActionsOnExit;

	public BattleTextWindow(String s, int x, int y, int width, int height, StartupNew m) {
		super(s, x,y,width,height,m);
	}
	
	public void loadAnimOnExit() {
		loadAnimOnExit = true;
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
			BattleMenu bm = ((BattleMenu) state.getMenuStack().peek());
			
			if (loadAnimOnExit) {
				bm.setGetAnimation(loadAnimOnExit);
			} else if (completeActionOnExit) {
//				bm.getCurrentActiveBattleAction().setComplete();
//				bm.setGetNextPrompt();
			} else if (pollForActionsOnExit) {
				bm.setGetNextPrompt();
				if (bm.turnStackIsEmpty()) {
					bm.setPollForActions();
				}
			}
		}
//		
	}

	public void setPollForActionsOnExit() {
		pollForActionsOnExit = true;
	}
	
	public void setCompleteOnExit() {
		completeActionOnExit = true;
	}
}
