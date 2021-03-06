package menu;

import java.util.ArrayList;
import java.util.List;

import menu.windows.SelectionTextWindow;
import system.MotherSystemState;
import system.SystemState;
import system.controller.InputController;
import system.interfaces.Controllable;

public class Menu implements MenuInterface {
	private String title = "Welcome to Game!";
	protected List<MenuItem> menuItems;
	protected List<DrawableObject> drawables = new ArrayList<DrawableObject>();
	private String id;
	protected SystemState state;
	protected String input="";
	protected ArrayList<MenuItem> needToAdd = new ArrayList<MenuItem>();
	protected ArrayList<MenuItem> needToRemove = new ArrayList<MenuItem>();
	protected boolean backShouldExit = true;
	protected boolean canRemove = true;
	private boolean popping;
//	private int targetX;
//	private int targetY;
	
	
	
	public List<DrawableObject> getDrawableObjects() {
		return drawables;
	}
	
	public boolean isSwirl() {
		return false;
	}
	
	public void addToMenuItems(MenuItem m) {
		// TODO Auto-generated method stub
//		if (!menuItems.contains(m))
			needToAdd.add(m);
	}
	
	public void setCanRemove(boolean b) {
		this.canRemove = b;
	}
	
	public boolean canRemove() {
		return canRemove;
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
	public Menu(SystemState m) {
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

	public void reloadActionOnPop() {
		
	}
	
	public SystemState getState() {
		return state;
	}
	
	public boolean getPopping() {
		return popping;
	}
	
	public void pop() {
		state.getMenuStack().popStack();
	}
	
	public void onPop() {
		popping = true;
		//TODO what was this supposed to do??
		if (canRemove) {
			state.getMenuStack().pop();
		}
	}
	
	public void updateAll(InputController input) {
		// TODO Auto-generated method stub
		if (backShouldExit) {
			MotherSystemState state = (MotherSystemState) this.state;
			if (input.getSignals().get("BACK") && state.getMenuStack().peek() != state.battleMenu) {
				onPop();
			}
		}
		
		
		needToAdd = new ArrayList<MenuItem>();
		needToRemove = new ArrayList<MenuItem>();
		for (DrawableObject e : menuItems) {
			if (e instanceof Controllable) {
				((Controllable) e).handleInput(input);
			}
		}
		
		for (MenuItem i : menuItems) {
			if (i == null) {
				continue;
			}
			i.updateAnim();
		}
		
		update(input);
		menuItems.removeAll(needToRemove);
		menuItems.addAll(needToAdd);
		if (this.menuItems.isEmpty()) {
//			state.needToPop = true;
			doOnEmpty();
		}
	}
	
	public void doOnEmpty() {
		state.setToRemove(this);
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

	public void doDoneFadeOutAction() {
		// TODO Auto-generated method stub
		
	}

	public boolean getCanUpdateGameState() {
		// TODO Auto-generated method stub
		return false;
	}

}
