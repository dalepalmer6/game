package menu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import battlesystem.options.EnemyOption;
import canvas.Controllable;
import canvas.Drawable;
import font.SelectionTextWindow;
import font.TextWindow;
import global.InputController;
import global.MenuStack;
import menu.namecharactersmenu.PromptForCharacterNameButton;

public class Menu implements MenuInterface {
	private String title = "Welcome to Game!";
	protected List<MenuItem> menuItems;
	protected List<DrawableObject> drawables = new ArrayList<DrawableObject>();
	private String id;
	protected StartupNew state;
	protected String input;
	private Controllable focused;
	protected ArrayList<MenuItem> needToAdd = new ArrayList<MenuItem>();
	protected ArrayList<MenuItem> needToRemove = new ArrayList<MenuItem>();
	
	public List<DrawableObject> getDrawableObjects() {
		return drawables;
	}
	
	public boolean isSwirl() {
		return false;
	}
	
	public void addToMenuItems(MenuItem m) {
		// TODO Auto-generated method stub
		needToAdd.add(m);
	}
	
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
		menuItems = new ArrayList<MenuItem>();
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
	
	public List<MenuItem> getMenuItems() {
		return this.menuItems;
	}

	public void addMenuItem(DrawableObject d) {
		if (!drawables.contains(d)) {
			drawables.add(d);
		}
	}
	
	@Override
	public void addMenuItem(MenuItem m) {
		// TODO Auto-generated method stub
		if (!menuItems.contains(m)) {
			menuItems.add(m);
		}
		
	}

	public StartupNew getState() {
		return state;
	}
	public void updateAll(InputController input) {
		// TODO Auto-generated method stub
//		for (int i = menuItems.size()-1; i >= 0; i--) {
//			if ( menuItems.get(i) instanceof Controllable) {
//				((Controllable) menuItems.get(i)).handleInput(input);
//			}
//			break;
//		}
		if (input.getSignals().get("BACK") && state.getMenuStack().peek() != state.battleMenu) {
			state.getMenuStack().pop();
		}
		
		needToAdd = new ArrayList<MenuItem>();
		needToRemove = new ArrayList<MenuItem>();
		for (DrawableObject e : menuItems) {
			if (e instanceof Controllable) {
				((Controllable) e).handleInput(input);
			}
		}
		
				for (MenuItem i : menuItems) {
					i.updateAnim();
				}
		
		update(input);
		menuItems.removeAll(needToRemove);
		menuItems.addAll(needToAdd);
		if (this.menuItems.isEmpty()) {
//			state.needToPop = true;
			state.setToRemove(this);
		}
	}
	
	public void update(InputController input) {
	}

	public void setToRemove(MenuItem e) {
		// TODO Auto-generated method stub
		needToRemove.add(e);
	}

	public void removeMenuItem(SelectionTextWindow stw) {
		// TODO Auto-generated method stub
		menuItems.remove(stw);
	}

}
