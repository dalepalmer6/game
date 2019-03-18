package menu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import canvas.Drawable;
import global.MenuStack;

public class Menu implements MenuInterface {
	private String title = "Welcome to Game!";
	private List<Drawable> menuItems;
	private String id;
	protected StartupNew state;
	
	public Menu(StartupNew m) {
		setState(m);
		menuItems = new ArrayList<Drawable>();
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

	public void setState(StartupNew state) {
		this.state = state;
	}

}
