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
	private boolean drawOnly;
	private String output = "";
	protected int currentOpenX;
	private int currentOpenY;
	protected int stepForwardX = 64;
	protected int stepForwardY = 32;
	private int dimX = 0;
	private int dimY = 0;
//	protected List<MenuItem> selections = new ArrayList<MenuItem>();
	private String orientation = "vertical";
	private String t = "";
	private boolean focused = false;
	private boolean killWhenComplete;
	private int selectedY = 0;
	private int selectedX = 0;
	private boolean canPopMenuStack = true;
	
	public void setCurrentOpen(int x, int y) {
		this.currentOpenX = this.x + x;
		this.currentOpenY = this.y + y;
	}
	
	public void clearSelections() {
		selections.clear();
	}
	
	public int getSelectedIndex() {
		return selectedY*dimX + selectedX;
	}
	
	public MenuItem getSelectedItem() {
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
		currentOpenX = this.x + this.TEXT_START_X + 16;
		currentOpenY = this.y+ this.TEXT_START_Y;
		ArrayList<MenuItem> firstRow = new ArrayList<MenuItem>(1);
		selections = new ArrayList<ArrayList<MenuItem>>();
		selections.add(firstRow);
	}
	
	public void createGrid(int x, int y) {
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
		m.setX(currentOpenX);
		m.setY(currentOpenY);
		switch (orientation) {
		case "vertical":
			if (currentOpenY > this.y + this.getHeight()*2) {
				currentOpenY = this.y + this.TEXT_START_Y;
				currentOpenX += stepForwardX;
//				dimX++;
			}
			m.setX(currentOpenX);
			m.setY(currentOpenY);
			selections.get(dimY-1).add(m);
			dimY++;
			if (selections.size() < dimY) {
				selections.add(new ArrayList<MenuItem>());
			}
			currentOpenY += stepForwardY;
			break;
		case "horizontal" :
			if (currentOpenX > this.x + this.getWidth()*2) {
				currentOpenX = this.x + this.TEXT_START_X;
				currentOpenY += stepForwardY;
				selections.add(new ArrayList<MenuItem>());
				dimY++;
			}
			m.setX(currentOpenX);
			m.setY(currentOpenY);
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
				   while (selections.get(selectedY).size() <= selectedX || selections.get(selectedY).get(selectedX) == null) {
					   updateIndex("D");
					}
					break;
		case "U" : 	selectedY--;
					if (selectedY < 0)  {
						selectedY = selections.size()-1;
					}
					while (selections.get(selectedY).size() <= selectedX || selections.get(selectedY).get(selectedX) == null) {
						updateIndex("U");
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
		for (ArrayList<MenuItem> mis : selections) {
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
	
	public void drawCursor(MainWindow m) {
//		selectedIndexX = overallIndex / dimY;
//		selectedIndexY = overallIndex % dimY;
		if (!drawOnly) {
			CharacterData cursor = this.m.charList.getCharObjects().get('@');
//			m.renderTile(cursorStartPositionX() + selectedIndexX*stepForwardX,  cursorStartPositionY() + selectedIndexY*stepForwardY,
//					(int)cursor.getDw()*4,(int)cursor.getDh()*4,
//					cursor.getDx(),cursor.getDy(),
//					cursor.getDw()*2,cursor.getDh()*2);
			m.renderTile(selections.get(selectedY).get(selectedX).getX() - 16,  selections.get(selectedY).get(selectedX).getY(),
					(int)cursor.getDw()*4,(int)cursor.getDh()*4,
					cursor.getDx(),cursor.getDy(),
					cursor.getDw()*2,cursor.getDh()*2);
		}
		
	}
	
	public String execute() {
		selections.get(selectedY).get(selectedX).prepareToExecute();
		state.getMenuStack().peek().setToRemove(this);
		return null;
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
	
}
