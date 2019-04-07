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
	private List<List<MenuItem>> selections2D;
	private String output = "";
	private int currentOpenX;
	private int currentOpenY;
	private int spacingXFactor = 1;
	private int spacingYFactor = 1;
	protected int stepForwardX = 64;
	protected int stepForwardY = 32;
	private int dimX = 1;
	private int dimY = 0;
	private List<MenuItem> selections = new ArrayList<MenuItem>();
	private String orientation = "vertical";
	private int overallIndex = 0;
	private int selectedIndexX = 0;
	private int selectedIndexY = 0;
	private String t = "";
	private boolean focused = false;
	private boolean killWhenComplete;
	
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
		if (orientation.equals("horizontal")) {
			dimX = 0;
			dimY = 1;
		} else if (orientation.equals("vertical")) {
			dimX = 1;
			dimY = 0;
		}
		currentOpenX = this.x + this.TEXT_START_X;
		currentOpenY = this.y+ this.TEXT_START_Y;
	}
	
	public void add(MenuItem m) {
		
		switch (orientation) {
			case "vertical":
				if (currentOpenY > this.y + this.getHeight()*2) {
					currentOpenY = this.y + this.TEXT_START_Y;
					currentOpenX += stepForwardX;
					dimX++;
				} 
					m.setX(currentOpenX);
					m.setY(currentOpenY);
					selections.add(m);
					currentOpenY += stepForwardY;
					if (dimX == 1) {
						dimY += 1;
					}
				break;
			case "horizontal" :
				if (currentOpenX > this.x + this.getWidth()*2) {
					currentOpenX = this.x + this.TEXT_START_X;
					currentOpenY += stepForwardY;
					dimY++;
				} 
					m.setX(currentOpenX);
					m.setY(currentOpenY);
					selections.add(m);
					currentOpenX += stepForwardX;
					if (dimY == 1) {
						dimX += 1;
					}
				break;
		}
		
	}
	
	public String generateOptions() {
		String t = "";
		for (MenuItem m : selections) {
			t += m.getText() + "[NEWLINE]";
		}
		return t;
	}
	
	public void updateIndex(String direction) {
		switch (orientation) {
		case "vertical" : 
				switch(direction) {
				case "D" : 
					selectedIndexY++;
					overallIndex = dimY * selectedIndexX + selectedIndexY;
					if (selectedIndexY >= dimY) {
						selectedIndexY = 0;
						overallIndex = dimY * selectedIndexX + selectedIndexY;
					}
					break;
				case "U" :
					selectedIndexY--;
					overallIndex = dimY * selectedIndexX + selectedIndexY;
					if (selectedIndexY < 0) {
						selectedIndexY = dimY-1;
						overallIndex = dimY * selectedIndexX + selectedIndexY;
					}
					break;
				case "R" :
					selectedIndexX++;
					overallIndex = dimY * selectedIndexX + selectedIndexY;
					if (overallIndex >= selections.size()) {
						selectedIndexX = 0;
						selectedIndexY = selectedIndexY % dimY;
						overallIndex = dimY * selectedIndexX + selectedIndexY;
					}
					break;
				case "L" :
					selectedIndexX--;
					overallIndex = dimY * selectedIndexX + selectedIndexY;
					if (overallIndex < 0) {
						selectedIndexX = (selections.size()-1) / dimY;
						selectedIndexY = selectedIndexY % dimY;
						overallIndex = dimY * selectedIndexX + selectedIndexY;
					}
					break;
				}
			if (overallIndex > selections.size()-1) {
				overallIndex = selections.size()-1;
				selectedIndexX = overallIndex / dimY;
				selectedIndexY = overallIndex % dimY;
			}
			break;
			case "horizontal" : 
				System.out.println(overallIndex);
				switch(direction) {
					case "D" : 
						selectedIndexY++;
						overallIndex = dimX * selectedIndexY + selectedIndexX;
						if (overallIndex >= selections.size()) {
							selectedIndexY = 0;
							overallIndex = dimX* selectedIndexY + selectedIndexX;
						}
						break;
					case "U" :
						selectedIndexY--;
						overallIndex = dimX * selectedIndexY + selectedIndexX;
						if (selectedIndexY < 0) {
							selectedIndexY = dimY-1;
							overallIndex = dimX * selectedIndexY + selectedIndexX;
							if (overallIndex > selections.size()-1) {
								selectedIndexY = dimY-2;
								overallIndex = dimX * selectedIndexY + selectedIndexX;
							}
						}
						break;
					case "R" :
						selectedIndexX++;
						overallIndex = dimX * selectedIndexY + selectedIndexX;
						if (selectedIndexX > dimX-1) {
							selectedIndexX = 0;
							overallIndex = dimX * selectedIndexY + selectedIndexX;
						}
						if (overallIndex >= selections.size()) {
							selectedIndexX = dimX-1;
							selectedIndexY = dimY-2;
							overallIndex = dimX * selectedIndexY + selectedIndexX;
						}
						break;
					case "L" :
						selectedIndexX--;
						overallIndex = dimX * selectedIndexY + selectedIndexX;
						if (selectedIndexX < 0) {
							selectedIndexX = dimX-1;
							overallIndex = dimX * selectedIndexY + selectedIndexX;
						}
						break;
				}
				if (overallIndex > selections.size()-1) {
					overallIndex = selections.size()-1;
					selectedIndexX = overallIndex % dimX;
					selectedIndexY = overallIndex / dimX;
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
		String t = selections.get(overallIndex).prepareToExecute();
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
		for (MenuItem m : selections) {
			int curX =m.getX() + scale * TEXT_START_X;
			char[] chars = m.getText().toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				
				mw.renderTile(curX,m.getY(),
						(int) state.charList.getCharObjects().get(c).getDw()*scale,
						(int) state.charList.getCharObjects().get(c).getDh()*scale,
						state.charList.getCharObjects().get(c).getDx(),
						state.charList.getCharObjects().get(c).getDy(),
						state.charList.getCharObjects().get(c).getDw(),
						state.charList.getCharObjects().get(c).getDh());
				curX += 1*scale +  state.charList.getCharObjects().get(c).getDw()*scale;
			}
		}
	}
	
	public void drawCursor(MainWindow m) {
//		selectedIndexX = overallIndex / dimY;
//		selectedIndexY = overallIndex % dimY;
		CharacterData cursor = this.m.charList.getCharObjects().get('@');
		m.renderTile(this.text.getX() + selectedIndexX*stepForwardX,  this.text.getY() + selectedIndexY*stepForwardY,
				(int)cursor.getDw()*4,(int)cursor.getDh()*4,
				cursor.getDx(),cursor.getDy(),
				cursor.getDw()*2,cursor.getDh()*2);
	}
	
	public String execute() {
		selections.get(overallIndex).prepareToExecute();
		state.getMenuStack().peek().setToRemove(this);
		return null;
	}
	
	@Override
	public void handleInput(InputController input) {
			if (input.getSignals().get("UP")) {
				updateIndex("U");
			} else if (input.getSignals().get("DOWN")) {
				updateIndex("D");
			} else if (input.getSignals().get("RIGHT")) {
				updateIndex("R");
			}else if (input.getSignals().get("LEFT")) {
				updateIndex("L");
			} else if (input.getSignals().get("CONFIRM")) {
				update();
			}
	}
}
