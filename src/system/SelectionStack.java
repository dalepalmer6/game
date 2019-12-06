package system;

import java.util.ArrayList;
import java.util.List;

import menu.Menu;
import menu.MenuItem;

public class SelectionStack {
	private List<MenuItem> stack;
	
	public SelectionStack() {
		this.stack = new ArrayList<MenuItem>();
	}

	public void push(MenuItem m) {
		stack.add(m);
	}
	
	public MenuItem pop() {
		return stack.remove(stack.size()-1);
	}
	
	public MenuItem peek() {
		if (stack.size() == 0) {
			return null;
		}
		return stack.get(stack.size()-1);
	}
	
	public MenuItem popAndAddMenuItems() {
		MenuItem m = pop();
		return m;
	}
	
	public int size() {
		return stack.size();
	}
}
