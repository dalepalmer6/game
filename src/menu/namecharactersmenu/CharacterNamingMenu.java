package menu.namecharactersmenu;

import global.GlobalVars;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.TextBox;

public class CharacterNamingMenu extends Menu{
	private int curX = 10;
	private int curY = 10;
	private int numButtons;
	public CharacterNamingMenu() {
		super();
		//add all buttons and menuitems
		addMenuItem(new TextBox("Name the main character!"));
		addMenuItem(new BackButton());
		addToView("Ness");
		addToView("Paula");
		addToView("Jeff");
		addToView("Poo");
		addToView("King");
		addToView("Steak");
		addToView("Rockin");
	}
	
	public void addToView(String text) {
		MenuItem mi = new PromptForCharacterNameButton(text,curX,curY);
		updateXY(mi.getWidth(), mi.getHeight()); 
		numButtons++;
		addMenuItem(mi);
	}
	
	public void updateXY(int width, int height) {
		curX += width;
		if (curX + width > GlobalVars.mainWindow.getScreenWidth()) {
			curX = 10;
			updateCurY(height);
		} 
	}
	
	public void updateCurY(int height) {
		curY += height;
		if (curY + height > GlobalVars.mainWindow.getScreenHeight()) {
			curX = 10;
			curY = 10;
			System.err.println("Too many items on screen. Need new page.");
			//needs logic if y overflows as well, create another page
		} 
	}
}
