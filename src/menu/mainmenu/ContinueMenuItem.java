package menu.mainmenu;

import gamestate.GameState;
import global.MenuStack;
import menu.Animation;
import menu.AnimationFadeToBlack;
import menu.AnimationMenu;
import menu.MenuItem;
import menu.StartupNew;

public class ContinueMenuItem extends MenuItem {
	private static String text = "Continue";
	
	public ContinueMenuItem(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		System.out.println("Loading a previous game.");
		AnimationMenu an = new AnimationMenu(state);
		an.createAnimMenu();
//		state.needToPop = true;
		state.getMenuStack().push(an);
//		GameState gs = new GameState(state);
//		state.setGameState(gs);

		return null;
	}
	
}
