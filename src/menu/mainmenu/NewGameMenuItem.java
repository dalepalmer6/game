package menu.mainmenu;

import canvas.Drawable;
import font.SelectionTextWindow;
import gamestate.GameState;
import gamestate.Player;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;
import menu.namecharactersmenu.CharacterNamingMenu;

public class NewGameMenuItem extends MenuItem{
	private static String text = "New Game";
	
	public NewGameMenuItem(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		System.out.println("New Game Selected.");
		
//		state.getMenuStack().pop();
//		GameState gs = new GameState(state);
//		state.setGameState(gs);
		
		//push a new menu onto the stack
		Menu m = new CharacterNamingMenu(state);
			SelectionTextWindow STW = new SelectionTextWindow("vertical",300,300,5,10,state);			
				STW.add(m.createMenuItem("Ness"));
				STW.add(m.createMenuItem("Paula"));
				STW.add(m.createMenuItem("Jeff"));
				STW.add(m.createMenuItem("Poo"));
				STW.add(m.createMenuItem("King"));
				STW.add(m.createMenuItem("Steak"));
				STW.add(m.createMenuItem("Rockin"));
				STW.add(new BackButton(state));
		m.addMenuItem(STW);
		state.getMenuStack().push(m);
		return null;
	}

}
