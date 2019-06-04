package font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import canvas.Controllable;
import canvas.MainWindow;
import global.InputController;
import menu.MenuItem;
import menu.StartupNew;

public class SelectionTextWindow extends TextWindow implements Controllable{
	protected ArrayList<ArrayList<MenuItem>> selections;
	protected boolean drawOnly;
	private String output = "";
	protected int currentOpenX;
	protected int currentOpenY;
	protected int TEXT_START_X = 40;
	protected int stepForwardX = 128;
	protected int stepForwardY = 64;
	private int dimX = 0;
	private int dimY = 0;
	private String orientation = "vertical";
	private String t = "";
	private boolean focused = false;
	protected boolean killWhenComplete;
	protected int selectedY = 0;
	protected int selectedX = 0;
	private boolean canPopMenuStack = true;
	private boolean maxDimYReached;
	private int index = 0;
	private int yStart = 0;
	private int yEnd = 4;
	private boolean maxHeightReached;
	private boolean grid;
	
	public void updateAnim() {
		super.updateAnim();
		if (grid) {
			while (selectedY >= yEnd) {
				yStart++;
				yEnd++;
			}
			while (selectedY < yStart) {
				yStart--;
				yEnd--;
			}
			while (yEnd > selections.size()) {
				yStart = 0;
				yEnd = 4;
			}
			while (yStart < 0) {
				yEnd = selections.size();
				yStart = yEnd - 4;
			}
			int y = 32;
			for (int i = yStart; i < yEnd; i++) {
				int x = 288;
				
				ArrayList<MenuItem> mis = selections.get(i);
				for (MenuItem mi : mis) {
					if (mi != null) {
						mi.setX(this.x + (x += 64));
						mi.setY(this.y + y);
					}
				}
				y += 64;
			}
		}
		
		
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
	
	public void setCurrentOpen(int x, int y) {
		this.currentOpenX = this.x + x;
		this.currentOpenY = this.y + y;
	}
	
	public void resetIndex() {
		selectedX = 0;
		selectedY = 0;
	}
	
	public void clearSelections() {
		selections.clear();
		currentOpenX = this.x + this.TEXT_START_X + 16;
		currentOpenY = this.y + this.TEXT_START_Y;
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
		currentOpenX = this.x + this.TEXT_START_X;
		currentOpenY = this.y + this.TEXT_START_Y;
		ArrayList<MenuItem> firstRow = new ArrayList<MenuItem>(1);
		selections = new ArrayList<ArrayList<MenuItem>>();
		selections.add(firstRow);
	}
	
	public void createGrid(int x, int y) {
		grid = true;
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
		selections.get(y).add(x,m);
	}
	
	public void add(MenuItem m) {
//		m.setX(currentOpenX);
//		m.setY(currentOpenY);
		switch (orientation) {
		case "vertical":
			if (currentOpenY > this.y + this.getHeight()*4 + (2*TILE_SIZE)) {
				currentOpenY = this.y + this.TEXT_START_Y;
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
				currentOpenX = this.x + this.TEXT_START_X;
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
		drawCursor(m);
	}
	
	public void drawSelections(MainWindow mw) {
		Text.initDrawText(mw);
		int scale = 2;
//		int i = yStart;
//		for (ArrayList<MenuItem> mis : selections) {
		if (!grid) {
			yStart = 0;
			yEnd = selections.size();
		}
		for (int i = yStart; i < yEnd; i++) {
			ArrayList<MenuItem> mis = selections.get(i);
//			if (i++ > yEnd) {
//				break;
//			}
			for (MenuItem m : mis) {
				if (m == null) {
					continue;
				}
				m.draw(mw);
				
//				int curX =m.getX() + scale * TEXT_START_X;
//				char[] chars = m.getText().toCharArray();
//				for (int i = 0; i < chars.length; i++) {
//					char c = chars[i];
//					
//					mw.renderTile(curX,m.getY(),
//							(int) state.charList.getCharObjects().get(c).getDw()*scale,
//							(int) state.charList.getCharObjects().get(c).getDh()*scale,
//							state.charList.getCharObjects().get(c).getDx(),
//							state.charList.getCharObjects().get(c).getDy(),
//							state.charList.getCharObjects().get(c).getDw(),
//							state.charList.getCharObjects().get(c).getDh());
//					curX += 1*scale +  state.charList.getCharObjects().get(c).getDw()*scale;
//				}
			}
		}
	}
	
	public int cursorStartPositionX() {
		if (selections.size() == 0) {
			return -100;
		}
		return selections.get(0).get(0).getX();
	}
	
	public int cursorStartPositionY() {
		if (selections.size() == 0) {
			return -100;
		}
		return selections.get(0).get(0).getY();
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
		if (!drawOnly) {
			CharacterData cursor = this.m.charList.getCharObjects().get('@');
//			m.renderTile(cursorStartPositionX() + selectedIndexX*stepForwardX,  cursorStartPositionY() + selectedIndexY*stepForwardY,
//					(int)cursor.getDw()*4,(int)cursor.getDh()*4,
//					cursor.getDx(),cursor.getDy(),
//					cursor.getDw()*2,cursor.getDh()*2);
			m.renderTile(getSelectedItem().getX() - 16,  getSelectedItem().getY(),
					(int)cursor.getDw()*8,(int)cursor.getDh()*8,
					cursor.getDx(),cursor.getDy(),
					cursor.getDw()*2,cursor.getDh()*2);
		}
		
	}
	
	public String execute() {
		selections.get(selectedY).get(selectedX).prepareToExecute();
		state.getMenuStack().peek().setToRemove(this);
		return null;
	}
	
	public void killMe() {
		state.getMenuStack().peek().removeMenuItem(this);
	}
	
	public void setDrawOnly(boolean b) {
		drawOnly = b;
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
				update();
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
