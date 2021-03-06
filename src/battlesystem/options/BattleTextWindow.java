package battlesystem.options;

import battlesystem.menu.BattleMenu;
import menu.windows.TextWindowWithPrompt;
import system.MotherSystemState;
import system.SystemState;
import system.controller.InputController;

public class BattleTextWindow extends TextWindowWithPrompt {
	private boolean loadAnimOnExit;
	private boolean completeActionOnExit;
	private boolean pollForActionsOnExit;
	protected boolean getNext;
	private boolean getResultsOnExit;
	private String textString;
	private boolean setKillEntity;
	private boolean setNeedMenu;
	
	public BattleTextWindow(String s, int x, int y, int width, int height, SystemState m) {
		super(s, x,y,width,height,m);
		textString = s;
	}
	
	public void loadAnimOnExit() {
		loadAnimOnExit = true;
	}
	
	public String getText() {
		return textString;
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
		MotherSystemState state = (MotherSystemState) this.state;
		text.setFreeze(false);
		if (text.getDrawState()) {
			state.getMenuStack().peek().setToRemove(this);
			BattleMenu bm = state.battleMenu;
			if (setKillEntity) {
				bm.killDeadEntity();
			}
			if (getNext) {
				bm.setGetNext();
			}
			if (setNeedMenu) {
				bm.setNeedMenu();
			}
			if (loadAnimOnExit) {
				bm.setGetAnimation(loadAnimOnExit);
			} else if (pollForActionsOnExit) {
				bm.setGetNextPrompt();
				if (bm.turnStackIsEmpty()) {
					bm.setPollForActions();
				}
			} 
			else if (getResultsOnExit) {
				bm.setGetResultText();
			}
		}
	}

	public void setPollForActionsOnExit() {
		pollForActionsOnExit = true;
	}
	
	public void setCompleteOnExit() {
		completeActionOnExit = true;
	}
	

	public void setGetNext() {
		// TODO Auto-generated method stub
		getNext = true;
	}
	
	public boolean getGetNext() {
		return getNext;
	}

	public void setGetResultsOnExit() {
		// TODO Auto-generated method stub
		getResultsOnExit = true;
	}

	public void onCompleteKillEntity() {
		// TODO Auto-generated method stub
		setKillEntity = true;
	}

	public void setNeedMenuOnExit() {
		// TODO Auto-generated method stub
		setNeedMenu = true;
	}
}
