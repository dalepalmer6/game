package menu.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import font.CharList;
import menu.MenuItem;
import menu.atmmenu.DigitScroller;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.MainWindow;
import system.SystemState;
import system.interfaces.Drawable;

public class TextEngine implements Drawable{
	protected String textString;
	protected String parsedString;
	protected int drawStart = 0;
	protected int drawUntil = 1;
	protected boolean drawAll;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected HashMap<Integer, String> controlCodes = new HashMap<Integer,String>();
	protected CharList charList;
	protected boolean textChanged;
	protected boolean freeze;
	protected int saveStart;
	protected boolean done;
	protected boolean dramaticPause;
	protected int lengthToWait;
	protected double tickCount;
	protected double ticksPerFrame = 1d;
//	protected double oldTicksPerFrame;
//	protected boolean differentParseRate;
	protected double textRate =2d;
	protected SystemState state;
	protected boolean ignoreControlCodes = false;
	protected TextWindow renderWindow;
	protected int targetY;
	protected boolean waitingForDecision;
	protected int choice;
	protected String[] choices;
	protected SelectionTextWindow choiceWindow;
	protected boolean isSingleString;
	protected double shakeApplyY = 0;
	protected int drawingY;
	protected ArrayList<String> flags;
	protected boolean waitingForNumberInput;
	protected DigitScroller digitScroller;
	protected boolean waitForAudio;
	
	public HashMap<Integer,String> getControlCodes() {
		return controlCodes;
	}
	
	public void append(String s) {
		parsedString += s;
	}
	
	public void setRenderWindow(TextWindow tw) {
		renderWindow = tw;
	}
	
	public void setIgnoreCodes() {
		ignoreControlCodes = true;
	}
	
	public void setTextChanged(boolean f) {
		this.textChanged = f;
	}
	
	public void incrementDrawUntil() {
		if (parsedString != null && !drawAll) {
			if (waitingForDecision || waitingForNumberInput) {
				return;
			}
			if (drawUntil < parsedString.length()) {
				drawUntil+=2;
				if (drawUntil >= parsedString.length() || controlCodes.get(drawUntil-1) != null) {
					drawUntil--;
				}
			}
		}
	}
	
	//for the editor only
	public TextEngine(String s, int x, int y,int width, int height, CharList cd) {
		this(false,s,x,y,width,height,cd);
	}
	
	public TextEngine(boolean shouldDrawAll, String s, int x, int y,int width, int height, CharList cd) {
		this.textString = s;
		this.x = x;
		this.y = y;
		this.drawingY = y;
		this.targetY = y;
		this.width = width;
		this.height = height;
		this.charList = cd;
		this.drawAll = shouldDrawAll;
		flags = new ArrayList<String>();
	}
	
	public ArrayList<String> getFlags() {
		return flags;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return drawingY;
	}
	
	public void setText(String s) {
		this.textString = s;
		setTextChanged(true);
	}
	
	public String getParsedText() {
		return this.parsedString;
	}
	
	public void parse() {
		if (!ignoreControlCodes) {
			parseIfs();
			parseCodes();
			reparseForWidths();
			reparseForHeights();
		}
	}
	
	public void parseCodes() {
		
	}
	
	public void setFreeze(boolean c) {
		freeze = c;
		drawStart = saveStart;
		if (!c) {
			if (controlCodes.get(drawStart) != null) {
				controlCodes.put(drawStart,controlCodes.get(drawStart).replace("PROMPTINPUT","NEWLINE"));
				
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getDrawStart() {
		return drawStart;
	}
	
	public void reparseForWidths() {
		int currentWidth = 0;
		int currentWordWidth = 0;
		int indexOfLastSpace = 0;
		boolean addToWidths = true;
		char[] stringArray = parsedString.toCharArray();
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray.length == 0) {
				continue;
			} 
			addToWidths = true;
			if (controlCodes.containsKey(i)){
				String thisControlCode = controlCodes.get(i);
				if (thisControlCode.contains("NEWLINE") || thisControlCode.contains("NEWLINE_TOO_LONG") || thisControlCode.contains("PROMPTINPUT")) {
					currentWordWidth = 0;
					currentWidth = 0;
					indexOfLastSpace = i;
					continue;
				}
				
			}
			if (stringArray[i] == ' ') {
				currentWidth += currentWordWidth;
				currentWordWidth = 0;
				indexOfLastSpace = i;
			}  
			if (isSingleString) {
				this.width = currentWidth + currentWordWidth;
			}
			else if (currentWidth + currentWordWidth > this.width*4 + 32) {
				String controlCode = "NEWLINE_TOO_LONG";
				if (controlCodes.containsKey(indexOfLastSpace)) {
					controlCode = "NEWLINE_TOO_LONG," + controlCodes.get(indexOfLastSpace);
				}
				controlCodes.put(indexOfLastSpace,controlCode);
				
				parsedString = parsedString.substring(0,indexOfLastSpace) + parsedString.substring(indexOfLastSpace+1); 
				stringArray = parsedString.toCharArray();
				//fix the control codes that are greater than i
				HashMap<Integer, String> newCtrlCodes = new HashMap<Integer,String>();
				for (int key : controlCodes.keySet()) {
					if (key > indexOfLastSpace) {
						String val = controlCodes.get(key);
						newCtrlCodes.put(key-1,val);
					}
					else if (key == indexOfLastSpace) {
						String val = controlCodes.get(key);
						newCtrlCodes.put(key,val);
					} else {
						String val = controlCodes.get(key);
						newCtrlCodes.put(key,val);
					}
				}
				controlCodes = newCtrlCodes;
				
				currentWidth = currentWordWidth;
				currentWordWidth = 0;
				addToWidths = false;
			}
			if (addToWidths) {
				currentWordWidth += 4*charList.getCharObjects().get(parsedString.toCharArray()[i]).getDw() + 4*1;
			}
		}
	}
	
	public void reparseForHeights() {
		int curY = y;
		//count the number of NEWLINES in the controlCodes,
		Object[] controlCodesKeys =  controlCodes.keySet().toArray();
		Arrays.sort(controlCodesKeys);
		for (Object key : controlCodesKeys) {
			String[] controlCodesAtKey = controlCodes.get(key).split(",");
			String controlCodesReplace = "";
			for (int i = 0; i < controlCodesAtKey.length; i++) {
				String cc = controlCodesAtKey[i];
//				if (cc.equals("PROMPTINPUT")) {
//					//note that currently this clears the text window
//					curY = y;
//				}
				if (cc.equals("NEWLINE") || cc.equals("NEWLINE_TOO_LONG") || cc.equals("PROMPTINPUT")) {
					curY+=64;
					if (curY + 64 > y + (this.height + 16) * 4) {
						cc = cc + ",SHIFTTEXTUP";
						curY -= 64;
					}
				}
				controlCodesReplace = controlCodesReplace + "," +cc;
			}
			controlCodes.put((Integer) key,controlCodesReplace);
		}
	}
	
	public boolean getDrawState() {
		return done;
	}
	
	
	
	public void drawChar(MainWindow m,int i,char c, int curX,int curY) {
		if (charList.getCharObjects().get(c) == null) {
			return;
		}
		if (renderWindow != null) {
			if (curY < renderWindow.getY()) {
				return;
			}
		}
		
		m.renderTile(curX,curY,
				(int) charList.getCharObjects().get(c).getDw()*4,
				(int) charList.getCharObjects().get(c).getDh()*4,
				charList.getCharObjects().get(c).getDx(),
				charList.getCharObjects().get(c).getDy(),
				charList.getCharObjects().get(c).getDw(),
				charList.getCharObjects().get(c).getDh());
		
		if (i >= parsedString.length()-1) {
			done = true;
		}
	}
	
	public void parseIfs() {
		String newlyParsed = "";
		int startPos = 0;
		int endPos = parsedString.length();
		char[] stringArray = parsedString.toCharArray();
		boolean parsingControlCode = false;
		String controlCode = "";
		int indexOfCode = 0;
		for (int i = 0; i < stringArray.length; i++) {
			if (!parsingControlCode && stringArray[i] != '[' && stringArray[i] != ']') {
				newlyParsed += stringArray[i];
			}
			if (stringArray[i] == '[') {
				//start a new control code
				controlCode = "";
				indexOfCode = i;
				parsingControlCode = true;
				continue;
			}
			
			if (stringArray[i] != ']' && parsingControlCode) {
				controlCode += stringArray[i];
			} 
			if (stringArray[i] == ']' && parsingControlCode) {
				parsingControlCode = false;
				stringArray = parsedString.toCharArray();
				if (controlCode.startsWith("FLAGISSET_")) {
					String flagName = controlCode.substring(10);
					if (!flags.contains(flagName)) {
						flags.add(flagName);
					}
					
					if (!state.getGameState().getFlag(flagName)) {
						String toRemove = parsedString.substring(parsedString.indexOf("[" + controlCode + "]"),parsedString.indexOf("[ELSE]")+6);
						i-=controlCode.length()+2;
						parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
						parsedString = parsedString.replaceFirst("\\[ENDIF\\]","");
					} else {
						String toRemove = parsedString.substring(parsedString.indexOf("[ELSE]"),parsedString.indexOf("[ENDIF]")+7);
						parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
						parsedString = parsedString.replace("[" + controlCode + "]","");
						i-=controlCode.length()+2;
					}
					stringArray = parsedString.toCharArray();
				}
				if (controlCode.startsWith("HAVEFUNDSINBANK_")) {
					int amount = 0;
					if (controlCode.substring(16).equals("")) {
						amount = state.getGameState().getWindowArgument();
					} else {
						amount = Integer.parseInt(controlCode.substring(16));
					}
					
					if (state.getGameState().getFundsInBank() < amount) {
						String toRemove = parsedString.substring(parsedString.indexOf("[" + controlCode + "]"),parsedString.indexOf("[ELSE]")+6);
						i-=controlCode.length()+2;
						parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
						parsedString = parsedString.replaceFirst("\\[ENDIF\\]","");
					} else {
						String toRemove = parsedString.substring(parsedString.indexOf("[ELSE]"),parsedString.indexOf("[ENDIF]")+7);
						parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
						parsedString = parsedString.replace("[" + controlCode + "]","");
						i-=controlCode.length()+2;
					}
					stringArray = parsedString.toCharArray();
				}
				if (controlCode.startsWith("HAVEFUNDSONHAND_")) {
					int amount = 0;
					if (controlCode.substring(16).equals("")) {
						amount = state.getGameState().getWindowArgument();
					} else {
						amount = Integer.parseInt(controlCode.substring(16));
					}
					
					if (state.getGameState().getFundsOnHand() < amount) {
						String toRemove = parsedString.substring(parsedString.indexOf("[" + controlCode + "]"),parsedString.indexOf("[ELSE]")+6);
						i-=controlCode.length()+2;
						parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
						parsedString = parsedString.replaceFirst("\\[ENDIF\\]","");
					} else {
						String toRemove = parsedString.substring(parsedString.indexOf("[ELSE]"),parsedString.indexOf("[ENDIF]")+7);
						parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
						parsedString = parsedString.replace("[" + controlCode + "]","");
						i-=controlCode.length()+2;
					}
					stringArray = parsedString.toCharArray();
				}
			}
		}
		return;
	}
	
	
	public static void initDrawText(MainWindow m) {
		m.setTexture("img\\font.png");
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}

	public void update() {
//		if (differentParseRate) {
//			oldTicksPerFrame = ticksPerFrame;
//			ticksPerFrame *= 2;
//		}
		if (waitForAudio) {
			freeze = true;
			if (state.getBGMEnded()) {
				state.resetBGMEnded();
				waitForAudio = false;
			}
		}
		if (drawingY > targetY) {
			drawingY-=8;
		}
		if (!drawAll && !dramaticPause) {
			this.tickCount += ticksPerFrame;
			MenuItem it;
			if (digitScroller != null) {
				if (digitScroller.isDone()) {
					waitingForNumberInput = false;
					String[] split = textString.split(Pattern.quote("[NUMSCROLL_" + digitScroller.getDigitCount() +"]"));
					parsedString = "";
					textString = split[1];
					parsedString = textString;
					controlCodes = new HashMap<Integer,String>();
					parse();
					drawUntil = -2;
					drawingY = y;
					targetY = y;
					digitScroller = null;
				}
			}
			
			if ((it = state.getSelectionStack().peek()) instanceof DecisionMenuItem) {
				waitingForDecision = false;
				state.getSelectionStack().pop();
				state.getMenuStack().peek().removeMenuItem(choiceWindow);
				choice =  ((DecisionMenuItem) it).getIndex();
				parsedString = "";
				String splitString[] = choices[choice].split(Pattern.quote("[TEXT]"));
				textString = splitString[1];
				parsedString = textString;
				controlCodes = new HashMap<Integer,String>();
				parse();
				drawUntil = -2;
				drawingY = y;
				targetY = y;
			}
			if (tickCount % textRate == 0 && !waitForAudio) {
				incrementDrawUntil();
			}
		} else if (dramaticPause) {
			if (lengthToWait > 0) {
				lengthToWait--;
			} else {
				dramaticPause = false;
			}
			return;
		}
	}

	public void setX(double x) {
		this.x = (int) x;
	}
	
	public void setY(double y) {
		drawingY = (int) y;
	}

	public int getDrawUntil() {
		return drawUntil;
	}

	public boolean getDoneState() {
		return done || freeze || dramaticPause || waitingForDecision || waitingForNumberInput || waitForAudio;
	}
	
	public boolean getDone() {
		return done;
	}

	public void setTextRate(double d) {
		this.textRate = d;
	}

	public void setState(SystemState state) {
		this.state = state;
	}

	public void setAsSingleString() {
		isSingleString = true;
	}

	public void setShakingY(double applyShake) {
		drawingY = (int) (shakeApplyY + y);
		shakeApplyY = applyShake;
	}

	public void draw(MainWindow m) {
		draw(m,0,0,0);
	}
	
	//TODO - CLEAN UP IN HERE - 
	//ALOT OF CODE CAN BE MOVED TO AN UPDATE METHOD AND SHOULDNT BE DONE DURING A DRAW
	public void draw(MainWindow m, int r, int g, int b) {
		GL11.glColor3f(r,g,b);
		initDrawText(m);
		// TODO Auto-generated method stub
		if (parsedString == null || textChanged) {
			this.parsedString = textString;
			controlCodes.clear();

			parse();

			textChanged = false;

			if (drawAll) {
				drawUntil = parsedString.length();
			}

			for (int key : controlCodes.keySet()) {
				//					if (key >= parsedString.length()) {
				if (key > parsedString.length()) {
					controlCodes.remove(key);
					break;
				}
			}
		}
		int curX = x;
		int curY = drawingY;
		int scale = 4;
		char[] chars = parsedString.toCharArray();
		for (int i = 0; i < drawUntil+1; i++) {
			if (parsedString.equals("")) {
				parsedString = " ";
			}
			if (i >= chars.length) {
				continue;
			}
			if (i == 0 && chars[i] == '@') {
				drawChar(m,i,'@',curX-4*4,curY);
			}
			handleControlCodes(i, m, curX, curY, chars, scale);
			if (i < drawUntil) {
				if (chars.length == 0) {
					return;
				}
				if (chars[i] != '@') {
					if (charList.getCharObjects().get(chars[i]) == null) {
						GL11.glColor3f(255,255,255);
						return;
					}
					drawChar(m,i,chars[i],curX,curY);
					curX += charList.getCharObjects().get(chars[i]).getDw()*4;
				}

			}
		}
		GL11.glColor3f(255,255,255);
	}

	public void handleControlCodes(int i, MainWindow m, int curX, int curY, char[] chars, int scale) {
		
	}
	
	public void setParsedString() {
		parsedString = textString;
	}

	public int getTargetY() {
		return targetY;
	}

	public void shift() {
		
		
	}

	public void setTargetY(int i) {
		targetY = i;
	}
	
	
	
}