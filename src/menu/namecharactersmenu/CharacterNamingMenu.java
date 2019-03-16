package menu.namecharactersmenu;

import menu.BackButton;
import menu.Menu;
import menu.TextBox;

public class CharacterNamingMenu extends Menu{
	
	public CharacterNamingMenu() {
		super();
		//add all buttons and menuitems
		addMenuItem(new TextBox("Name the main character!"));
		addMenuItem(new BackButton());
		addMenuItem(new PromptForCharacterNameButton("Ness",10,10));
		addMenuItem(new PromptForCharacterNameButton("Paula",10,60));
		addMenuItem(new PromptForCharacterNameButton("Jeff",10,110));
		addMenuItem(new PromptForCharacterNameButton("Poo",10,160));
		addMenuItem(new PromptForCharacterNameButton("King",10,210));
		addMenuItem(new PromptForCharacterNameButton("Steak",110,10));
		addMenuItem(new PromptForCharacterNameButton("Rockin",110,60));
	}
}
