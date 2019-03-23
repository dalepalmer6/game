package global;

import java.util.List;

import canvas.Drawable;

import java.util.ArrayList;

import menu.Menu;
import menu.MenuItem;

public class MenuStack {
	private List<Menu> stack;
	
	public MenuStack() {
		this.stack = new ArrayList<Menu>();
	}

	public void push(Menu m) {
		stack.add(m);
	}
	
	public Menu pop() {
		if (stack.size() != 0) {
			return stack.remove(stack.size()-1);
		}
		return null;
	}
	
	public Menu peek() {
		if (stack.size() != 0) {
			return stack.get(stack.size()-1);
		}
		return null;
	}
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	public Menu popAndAddMenuItems() {
		Menu m = pop();
		return m;
	}
	
	public int size() {
		return stack.size();
	}
}
