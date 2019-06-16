package menu;

import java.util.ArrayList;

import canvas.Controllable;
import gamestate.GameState;
import global.InputController;

public class IntroWindowMenu extends Menu {

	public IntroWindowMenu(StartupNew state) {
		super(state);
	} 
	
	@Override
	public void doOnEmpty() {
		super.doOnEmpty();
		state.createNewGameGameState();
		AnimationMenu an = new AnimationMenu(state);
		an.createAnimMenu();
//		state.needToPop = true;
		state.getMenuStack().push(an);
	}
	
}
