package menu.intromenu;

import java.util.ArrayList;

import gamestate.GameState;
import menu.Menu;
import menu.animation.AnimationMenu;
import system.SystemState;
import system.controller.InputController;
import system.interfaces.Controllable;

public class IntroWindowMenu extends Menu {

	public IntroWindowMenu(SystemState state) {
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
