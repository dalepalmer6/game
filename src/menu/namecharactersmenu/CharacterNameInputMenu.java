package menu.namecharactersmenu;

import font.SelectionTextWindow;
import font.TextWindow;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;
import menu.SubmitButton;

public class CharacterNameInputMenu extends Menu {
	private String input = "";
	private SelectionTextWindow STW;		
	private TextWindow TW;
	
	public void setInput(String s) {
		input =s;
	}
	
	public void appendInput(String s) {
		input +=s;
	}
	
	public String getInput() {
		return input;
	}
	public CharacterNameInputMenu(StartupNew m) {
		super(m);
	}
	
	public void update() {
		TW.setText(input);
	}
	
	public void create() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SelectionTextWindow STW = new SelectionTextWindow(300,300,30,8,state);		
		
		TextWindow TW = new TextWindow(true,input,300,220,5,1,state); 
		int x = 0;
		int y = 0;
		for (char key : alphabet.toCharArray()) {
			STW.add(createMenuItem(key,x,y));
			x += 32;
			if (x > 32 * 16) {
				y += 32;
				x = 0;
			}
		}
		STW.add(new BackButton(state));
		STW.add(new SubmitButton(state));
		addMenuItem(TW);
		addMenuItem(STW);
		
		this.TW = TW;
		this.STW = STW;
	}
	
	public MenuItem createMenuItem(char c, int x , int y) {
		MenuItem mi = new CharacterInputButton(c,x,y,state);
		return mi;
	}
}
