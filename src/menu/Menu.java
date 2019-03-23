package menu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import canvas.Drawable;
import global.MenuStack;
import menu.namecharactersmenu.PromptForCharacterNameButton;

public class Menu implements MenuInterface {
	private String title = "Welcome to Game!";
	protected List<Drawable> menuItems;
	private String id;
	protected StartupNew state;
	protected String input;
	public void setInput(String s) {
		input =s;
	}
	
	public void appendInput(String s) {
		input += s;
	}

	public String getInput() {
		return input;
	}
	public Menu(StartupNew m) {
		this.state = m;
		menuItems = new ArrayList<Drawable>();
	}
	public MenuItem createMenuItem(String text) {
		MenuItem mi = new MenuItem(text,0,0, state);
		return mi;
	}
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String s) {
		this.title = s;
	}
	
	public void setId(String name) {
		this.id = name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public List<Drawable> getMenuItems() {
		return this.menuItems;
	}

	@Override
	public void addMenuItem(Drawable m) {
		// TODO Auto-generated method stub
		menuItems.add(m);
	}

	public StartupNew getState() {
		return state;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

}
