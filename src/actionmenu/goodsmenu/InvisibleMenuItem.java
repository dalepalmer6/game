package actionmenu.goodsmenu;

import menu.MenuItem;
import menu.StartupNew;
import canvas.Controllable;
import canvas.MainWindow;
import global.InputController;

public class InvisibleMenuItem extends MenuItem implements Controllable {
	private int index;
	private int maxIndex;
	private boolean canLoadInventory = false;
	public InvisibleMenuItem(StartupNew m,int max) {
		super("",0,0, m);
		maxIndex = max;
		index = 0;
		// TODO Auto-generated constructor stub
	}
	
	public void draw(MainWindow m) {}
	
	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		if (input.getSignals().get("LEFT")) {
			index--;
			if (index < 0) {
				index = maxIndex-1;
			}
		} else if (input.getSignals().get("RIGHT")) {
			index++;
			if (index >= maxIndex) {
				index = 0;
			}
		} 
//		else if (input.getSignals().get("BACK")) {
//			state.getMenuStack().pop();
//		} 
		else if (input.getSignals().get("CONFIRM")) {
			//create the goods menu using this inventory
			canLoadInventory = true;
		} else {
			canLoadInventory = false;
		}
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setCanLoadInventory(boolean b) {
		canLoadInventory = b;
	}
	
	public boolean getCanLoadInventory() {
		return canLoadInventory;
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	}

}
