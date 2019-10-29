package font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import battlesystem.FeatherMenuItem;
import canvas.Controllable;
import canvas.MainWindow;
import global.InputController;
import menu.MenuItem;
import menu.StartupNew;

public class SelectionTextWindow extends TextWindow implements Controllable{
	protected ArrayList<ArrayList<MenuItem>> selections;
	protected boolean drawOnly;
	protected String output = "";
	protected int currentOpenX;
	protected int currentOpenY;
	protected int TEXT_START_X = 40;
	protected int stepForwardX = 128;
	protected int stepForwardY = 64;
	protected int dimX = 0;
	protected int dimY = 0;
	protected String orientation = "vertical";
	protected String t = "";
	protected boolean focused = false;
	protected boolean killWhenComplete;
	protected int selectedY = 0;
	protected int selectedX = 0;
	protected boolean canPopMenuStack = true;
	protected boolean maxDimYReached;
	protected int index = 0;
	protected int yStart = 0;
	protected int yEnd = 0;
	protected boolean maxHeightReached;
	protected boolean grid;
	protected boolean callUpdate = false;
	protected MenuItem featherMenuItem;
	protected boolean setFirstCursorPos = false;
	protected boolean isEmpty;
	protected int startX;
	protected int startY;
	private int viewY;
	
	public boolean isEmpty() {
		return isEmpty;
	}
	
	public void setTargetPosY(int newY) {
		targetY = newY;
		for (ArrayList<MenuItem> i : selections) {
			for (MenuItem mi : i) {
				if (mi != null) {
					mi.setTargetPosY(newY);
				}
			}
		}
	}
	
	public void setYSelectionSize(int end) {
		viewY = end;
		yEnd = viewY;
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
				yEnd = viewY;
				setFirstCursorPos = false;
			}
			while (yStart < 0) {
				yEnd = selections.size();
				yStart = yEnd - viewY;
				setFirstCursorPos = false;
			}
			double y = this.y + startY;
			for (int i = yStart; i < yEnd; i++) {
				double x = this.x + startX;
				
				ArrayList<MenuItem> mis = selections.get(i);
				for (MenuItem mi : mis) {
					if (mi != null) {
						mi.setX(x);
						x += stepForwardX;
						mi.setY(y);
					}
				}
				y += stepForwardY;
			}
		}
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
	
	public void setTextStart(int x, int y) {
		TEXT_START_X = x;
		TEXT_START_Y = y;
	}
	
	public void setSteps(int dx, int dy) {
		if (dx != 0) {
			stepForwardX = dx;
		}
		if (dy != 0) {
			stepForwardY = dy;
		}
	}
	
	public void setCurrentOpen(double x, double y) {
		this.currentOpenX = (int) (this.x + x);
		this.currentOpenY = (int) (this.y + y);
	}
	
	public void resetIndex() {
		selectedX = 0;
		selectedY = 0;
	}
	
	public void clearSelections() {
		isEmpty = true;
		selections.clear();
		currentOpenX = (int) (this.x + this.TEXT_START_X + 16);
		currentOpenY = (int) (this.y + this.TEXT_START_Y);
		index = 0;
		dimX = 1;
		dimY = 1;
		ArrayList<MenuItem> firstRow = new ArrayList<MenuItem>(1);
		selections = new ArrayList<ArrayList<MenuItem>>();
		selections.add(firstRow);
	}
	
	public int getSelectedIndex() {
		return selectedY*dimX + selectedX;
	}
	
	public MenuItem getSelectedItem() {
		
		while (selectedY >= selections.size()) {
			updateIndex("U");
		}
		while (selectedX >= selections.get(selectedY).size()) {
			updateIndex("L");
		}
		if (selections.get(selectedY).get(selectedX) == null) {
			selectedY = 0;
			selectedX = 0;
		}
		try {
			return selections.get(selectedY).get(selectedX);
		} catch(ArrayIndexOutOfBoundsException e) {
			selectedY = 0;
			selectedX = 0;
		}
		return selections.get(selectedY).get(selectedX);
	}
	
	public int getTextStartX() {
		return TEXT_START_X;
	}
	
	public int getTextStartY() {
		return TEXT_START_Y;
	}
	
	public int getStepForwardY() {
		return stepForwardY;
	}
	
	public void setFocused(boolean b) {
		focused = b;
	}
	
	public boolean getFocused() {
		return focused;
	}
	
	public SelectionTextWindow(int x, int y, int width, int height, StartupNew m) {
		this("vertical",x,y,width,height,m);
	}
	
	public SelectionTextWindow(String orientation, int x, int y, int width, int height, StartupNew m) {
		super(true,"", x,y,width,height,m);
		this.orientation = orientation;
		dimX = 1;
		dimY = 1;
		currentOpenX = (int) (this.x + this.TEXT_START_X);
		currentOpenY = (int) (this.y + this.TEXT_START_Y);
		ArrayList<MenuItem> firstRow = new ArrayList<MenuItem>(1);
		selections = new ArrayList<ArrayList<MenuItem>>();
		selections.add(firstRow);
		featherMenuItem = new FeatherMenuItem("",-32,-32,64,64,state,"battlechoicefeather.png",0,0,16,16);
		isEmpty = true;
//		addMenuItem(featherMenuItem);
	}
	
	public void createGrid(int x, int y, int sx, int sy) {
		grid = true;
		this.startX = sx;
		this.startY = sy;
		selections = new ArrayList<ArrayList<MenuItem>>();
		ArrayList<MenuItem> row;
		for (int i = 0; i < y; i++) {
			row = new ArrayList<MenuItem>();
			for (int j = 0; j < x; j++) {
				row.add(null);
			}
			selections.add(row);
		}
	}
	
	public void add(MenuItem m, int x, int y) {
		//adds a MenuItem at a particular index, to create dynamic STW with different row/col lengths
		m.setX(currentOpenX);
		m.setY(currentOpenY);
		selections.get(y).set(x,m);
		if (m != null) {
			isEmpty = false;
		}
		
	}
	
	public void add(MenuItem m) {
//		m.setX(currentOpenX);
//		m.setY(currentOpenY);
		if (m != null) {
			isEmpty = false;
		}
		switch (orientation) {
		case "vertical":
			if (currentOpenY > this.y + this.getHeight()*4 + (2*TILE_SIZE)) {
				currentOpenY = (int) (this.y + this.TEXT_START_Y);
				currentOpenX += stepForwardX;
				dimX++;
				index = 0;
				maxDimYReached = true;
			}
			if (m != null) {
				m.setX(currentOpenX);
				m.setY(currentOpenY);
			}
			
			selections.get(index++).add(m);
			if (!maxDimYReached) {
				dimY++;
			}
			
			if (selections.size() < dimY) {
				selections.add(new ArrayList<MenuItem>());
			}
			currentOpenY += stepForwardY;
			break;
		case "horizontal" :
			if (currentOpenX > this.x + this.getWidth()*4) {
				currentOpenX = (int) (this.x + this.TEXT_START_X);
				currentOpenY += stepForwardY;
				selections.add(new ArrayList<MenuItem>());
				dimY++;
			}
			if (m != null) {
				m.setX(currentOpenX);
				m.setY(currentOpenY);
			}
			if (currentOpenY > this.y + this.getHeight()*4 && !maxHeightReached) {
				maxHeightReached = true;
				yStart = 0;
				yEnd = dimY;
			}
			selections.get(dimY-1).add(m);
			currentOpenX += stepForwardX;
			break;
	}
		
	}
	
	public void updateIndex(String direction) {
		switch(direction) {
		case "D" : 	selectedY++;
				   if (selectedY >= selections.size()) {
					   selectedY = 0;
				   }
				   while (selections.get(selectedY).size() <= selectedX) {
					   updateIndex("D");
				   }
				   while (selections.get(selectedY).get(selectedX) == null) {
					   boolean atLeastOne = false;
						for (MenuItem i : selections.get(selectedY)) {
							if (i != null) {
								atLeastOne = true;
								break;
							}
						}
						if (!atLeastOne) {
							updateIndex("D");
							return;
						}
					   updateIndex("R");
				   }
					break;
		case "U" : 	selectedY--;
					if (selectedY < 0)  {
						selectedY = selections.size()-1;
					}
					while (selections.get(selectedY).size() <= selectedX) {
						updateIndex("U");
					}
					while (selections.get(selectedY).get(selectedX) == null) {
						   boolean atLeastOne = false;
							for (MenuItem i : selections.get(selectedY)) {
								if (i != null) {
									atLeastOne = true;
									break;
								}
							}
							if (!atLeastOne) {
								updateIndex("U");
								return;
							}
						   updateIndex("R");
					   }
					break;			
		case "L" : selectedX--;
					if (selectedX < 0) {
						selectedX = selections.get(selectedY).size()-1;
					}
					while (selections.get(selectedY).get(selectedX) == null) {
						updateIndex("L");
					}
					break;
		case "R" : selectedX++; 
					if (selectedX >= selections.get(selectedY).size()) {
						selectedX = 0;
					}
					while (selections.get(selectedY).get(selectedX) == null) {
						updateIndex("R");
					}
					break;
		}
		featherMenuItem.setX(selections.get(selectedY).get(selectedX).getX());
		featherMenuItem.setY(selections.get(selectedY).get(selectedX).getY());
	}
	
	public void appendOutput(String s) {
		output += s;
	}
	
	public void setOutput(String s) {
		output = s;
	}
	
	public String getOutput() {
		return output;
	}
	
	
	public void setKillWhenComplete() {
		killWhenComplete = true;
	}
	
	public void update() {
		
		String t = selections.get(selectedY).get(selectedX).prepareToExecute();
		
		if (killWhenComplete) {
			state.getMenuStack().peek().setToRemove(this);
			selectedX = 0;
			selectedY = 0;
		}
		if (t != null) {
			appendOutput(t);
			state.setOutputFromSelect(output);
			System.out.println(output);
		}
		
	}
	public void draw(MainWindow m) {
		// TODO Auto-generated method stub
		drawWindow(m);
		drawSelections(m);
		if (!drawOnly) {
			featherMenuItem.draw(m);
		}
		
	}
	
	public void drawSelections(MainWindow mw) {
		Text.initDrawText(mw);
		if (!grid) {
			yStart = 0;
			yEnd = selections.size();
		}
		for (int i = yStart; i < yEnd; i++) {
			ArrayList<MenuItem> mis = selections.get(i);
			for (MenuItem m : mis) {
				if (m == null) {
					continue;
				}
				m.draw(mw);
			}
		}
	}
	
	public int cursorStartPositionX() {
		if (selections.size() == 0) {
			return -100;
		}
		return (int) selections.get(0).get(0).getX();
	}
	
	public int cursorStartPositionY() {
		if (selections.size() == 0) {
			return -100;
		}
		return (int) selections.get(0).get(0).getY();
	}
	
	public int getYStart() {
		return yStart;
	}
	
	public int getYEnd() {
		return yEnd;
	}
	
	public void drawCursor(MainWindow m) {
//		selectedIndexX = overallIndex / dimY;
//		selectedIndexY = overallIndex % dimY;
//		if (!drawOnly) {
			CharacterData cursor = this.m.charList.getCharObjects().get('@');
//			m.renderTile(cursorStartPositionX() + selectedIndexX*stepForwardX,  cursorStartPositionY() + selectedIndexY*stepForwardY,
//					(int)cursor.getDw()*4,(int)cursor.getDh()*4,
//					cursor.getDx(),cursor.getDy(),
//					cursor.getDw()*2,cursor.getDh()*2);
			m.renderTile(getSelectedItem().getX() - 16,  getSelectedItem().getY(),
					(int)cursor.getDw()*8,(int)cursor.getDh()*8,
					cursor.getDx(),cursor.getDy(),
					cursor.getDw()*2,cursor.getDh()*2);
//		}
		
	}
	
	public String execute() {
		MenuItem mi = selections.get(selectedY).get(selectedX);
		if (mi != null) {
			mi.prepareToExecute();
		}
		
		if (killWhenComplete) {
			state.getMenuStack().peek().setToRemove(this);
		}
		
		return null;
	}
	
	public void killMe() {
		state.getMenuStack().peek().removeMenuItem(this);
	}
	
	public void setDrawOnly(boolean b) {
		drawOnly = b;
	}
	
	public boolean drawOnly() {
		return drawOnly;
	}
	
	@Override
	public void handleInput(InputController input) {
		if (!drawOnly) {
//			state.setHoldable(false);
			if (input.getSignals().get("UP")) {
				updateIndex("U");
				state.setSFX("cursverti.wav");
				state.playSFX();
			} else if (input.getSignals().get("DOWN")) {
				updateIndex("D");
				state.setSFX("cursverti.wav");
				state.playSFX();
			} else if (input.getSignals().get("RIGHT")) {
				updateIndex("R");
				state.setSFX("curshoriz.wav");
				state.playSFX();
			}else if (input.getSignals().get("LEFT")) {
				updateIndex("L");
				state.setSFX("curshoriz.wav");
				state.playSFX();
			} else if (input.getSignals().get("CONFIRM")) {
				callUpdate = true;
			} 
//			else if (input.getSignals().get("BACK")) {
//				state.getMenuStack().pop();
//			}
		}
	}

	public int getCurrentOpenY() {
		// TODO Auto-generated method stub
		return currentOpenY;
	}
	
}
