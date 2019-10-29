package menu.gameovermenu;

import font.DialogTextWindow;
import menu.AnimationMenu;
import menu.Menu;
import menu.StartupNew;

public class GameOverMenu extends Menu {
	private String GAME_OVER_STRING = "@Well [NINTEN], it seems like you went in over your head.[PROMPTINPUT]@Did you want to give it another shot?"
			+ "[CHOOSE]"
			+ "[CHOICE]Yes[TEXT]@That's my boy! Off you go!"
			+ "[CHOICE]No[TEXT]@I see. Well, try to get some rest then.";
	
	public GameOverMenu(StartupNew s) {
		super(s);
		
		//draw the entity in the center of the screen
		//state.getGameState().getPartyMembers().get(0);
		
		DialogTextWindow dtw = new DialogTextWindow(GAME_OVER_STRING,"Dad",s);
		addMenuItem(dtw);
	}
	
	@Override
	public void onPop() {
		state.getMenuStack().clear();
		AnimationMenu an = new AnimationMenu(state);
		an.createAnimMenu();
		state.getMenuStack().push(an);
	}
	
	@Override
	public void doDoneFadeOutAction() {
		// TODO Auto-generated method stub
		state.setShouldFadeIn();
		state.getGameState().reloadInitialMap();
		state.setDrawAllMenus(false);
	}
	
}
