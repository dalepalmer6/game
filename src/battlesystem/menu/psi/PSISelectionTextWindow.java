package battlesystem.menu.psi;

import java.util.ArrayList;

import gamestate.partymembers.PartyMember;
import gamestate.psi.PSIFamily;
import menu.MenuItem;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class PSISelectionTextWindow extends SelectionTextWindow {
	
	PartyMember activeMember;
	ArrayList<PSIFamily> families;
	
	public PSISelectionTextWindow(int x, int y, int width, int height, SystemState s, PartyMember m) {
		super("horizontal",x,y,width,height,s);
		currentOpenX = 224; // fool around with this to get the right spacing between this and the headers for equipment
		activeMember = m;
	}
	
	public void updateAnim() {
		if (!setFirstCursorPos && selections.get(0).size() != 0 && !drawOnly && !isEmpty) {
			setFirstCursorPos = true;
			featherMenuItem.setX(selections.get(selectedY).get(selectedX).getX());
			featherMenuItem.setY(selections.get(selectedY).get(selectedX).getY());
		}
		
		if (callUpdate) {
			callUpdate = false;
			execute();
		}
		super.updateAnim();
		if (grid) {
			while (selectedY >= yEnd) {
				yStart++;
				yEnd++;
				setFirstCursorPos = false;
			}
			while (selectedY < yStart) {
				yStart--;
				yEnd--;
				setFirstCursorPos = false;
			}
			while (yEnd > selections.size()) {
				yStart = 0;
				yEnd = 4;
				setFirstCursorPos = false;
			}
			while (yStart < 0) {
				yEnd = selections.size();
				yStart = yEnd - 4;
				setFirstCursorPos = false;
			}
			double y = startY;
			for (int i = yStart; i < yEnd; i++) {
				double x = this.x + startX;
				
				ArrayList<MenuItem> mis = selections.get(i);
				for (MenuItem mi : mis) {
					if (mi != null) {
						mi.setX((x += stepForwardX));
						mi.setY(this.y + y);
					}
				}
				y += stepForwardY;
			}
		}
//		featherMenuItem.setX(selections.get(selectedY).get(selectedX).getX());
//		featherMenuItem.setY(selections.get(selectedY).get(selectedX).getY());
		featherMenuItem.updateAnim();
		
		for (ArrayList<MenuItem> list : selections) {
			for (MenuItem i : list) {
				if (i != null) {
					if (getSelectedItem() == i) {
						i.setHovered(true);
					} else {
						i.setHovered(false);
					}
					if (i.getWidthOfText() > width*4+32) {
						width = i.getWidthOfText();
					}
				}
			}
		}
	}
	
}
