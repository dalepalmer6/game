package menu.namecharactersmenu;

import canvas.Drawable;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class CharacterNamingMenu extends Menu{
	private int curX = 10;
	private int curY = 10;
	
	public CharacterNamingMenu(StartupNew m) {
		super(m);
	}
	
	@Override
	public MenuItem createMenuItem(String text) {
		MenuItem mi = new PromptForCharacterNameButton(text,curX,curY, state);
		return mi;
	}
	
}
