package font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import battlesystem.BattleMenu;
import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import gamestate.BattleEntity;
import gamestate.Enemy;
import gamestate.EnemyEntity;
import gamestate.PartyMember;
import global.InputController;
import menu.MenuItem;
import menu.ShopMenu;
import menu.StartupNew;

public class Text implements Drawable{
	private String textString;
	private String parsedString;
	protected int drawStart = 0;
	protected int drawUntil = 1;
	protected boolean drawAll;
	private int x;
	private int y;
	private int width;
	private int height;
	private HashMap<Integer, String> controlCodes = new HashMap<Integer,String>();
	private CharList charList;
	private boolean textChanged;
	private boolean freeze;
	private int saveStart;
	private boolean done;
	private boolean dramaticPause;
	private int lengthToWait;
	private double tickCount;
	private double ticksPerFrame = 1d;
	private double oldTicksPerFrame;
	private boolean differentParseRate;
	private double textRate =2d;
	private StartupNew state;
	private boolean ignoreControlCodes = false;
	private TextWindow renderWindow;
	private int targetY;
	private boolean waitingForDecision;
	private int choice;
	private String[] choices;
	private SelectionTextWindow choiceWindow;
	private boolean isSingleString;
	
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
			if (waitingForDecision) {
				return;
			}
			if (drawUntil < parsedString.length()) {
				drawUntil++;
			}
		}
	}
	public Text(String s, int x, int y,int width, int height, CharList cd) {
		this(false,s,x,y,width,height,cd);
	}
	
	public Text(boolean shouldDrawAll, String s, int x, int y,int width, int height, CharList cd) {
		this.textString = s;
		this.x = x;
		this.y = y;
		this.targetY = y;
		this.width = width;
		this.height = height;
		this.charList = cd;
		this.drawAll = shouldDrawAll;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setText(String s) {
		this.textString = s;
		setTextChanged(true);
	}
	
	public String getParsedText() {
		return this.parsedString;
	}
	
	public void parse() {
		parseIfs();
		parseCodes();
		reparseForWidths();
		reparseForHeights();
	}
	
	public void parseCodes() {
		char[] stringArray = parsedString.toCharArray();
		String controlCode = "";
		int indexOfCode = 0;
		boolean parsingControlCode = false;
		for (int i = 0; i < stringArray.length; i++) {
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
				String controlCodesList = controlCode;
				if (controlCodes.containsKey(indexOfCode)) {
					controlCodesList = controlCodes.get(indexOfCode) + "," + controlCode;
				}
				controlCodes.put(indexOfCode,controlCodesList);
				parsedString = parsedString.replaceFirst("\\[" + controlCode + "\\]", "");
				i -= (controlCode.length()+2);
				stringArray = parsedString.toCharArray();
				parsingControlCode = false;
				continue;
			}
		}
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
		//implement text that can scroll, and be prompted next
		int currentWidth = 0;
		int currentHeight = 0;
		int currentWordWidth = 0;
		int indexOfLastSpace = 0;
		boolean addToWidths = true;
		char[] stringArray = parsedString.toCharArray();
		for (int i = 0; i < stringArray.length; i++) {
			addToWidths = true;
			if (controlCodes.containsKey(i)){
				currentWordWidth = 0;
				currentWidth = 0;
				indexOfLastSpace = i;
				continue;
			}
			if (stringArray[i] == ' ') {
				currentWidth += currentWordWidth;
				currentWordWidth = 0;
				indexOfLastSpace = i;
			}  
			if (isSingleString) {
				this.width = currentWidth + currentWordWidth;
			}
			else if (currentWidth + currentWordWidth > this.width) {
				String controlCode = "NEWLINE";
				if (controlCodes.containsKey(indexOfLastSpace)) {
					controlCode = "NEWLINE," + controlCodes.get(indexOfLastSpace);
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
				currentWordWidth += charList.getCharObjects().get(parsedString.toCharArray()[i]).getDw() + 1;
			}
		}
	}
	
	public void reparseForHeights() {
		int curY = y;
		char[] stringArray = parsedString.toCharArray();
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
				if (cc.equals("NEWLINE") || cc.equals("PROMPTINPUT")) {
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
	
	@Override
	public void draw(MainWindow m) {
		initDrawText(m);
		// TODO Auto-generated method stub
		if (parsedString == null || textChanged) {
			this.parsedString = textString;
			controlCodes.clear();
			if (!ignoreControlCodes) {
				parse();
			}
			
			textChanged = false;
			
			if (drawAll) {
				drawUntil = parsedString.length();
			}
			
			for (int key : controlCodes.keySet()) {
				if (key >= parsedString.length()) {
					controlCodes.remove(key);
					break;
				}
			}
		}
		int curX = x;
		int curY = y;
		int scale = 4;
		char[] chars = parsedString.toCharArray();
		for (int i = 0; i < drawUntil+1; i++) {
			
//			if (i == 0 && c != '@') {
////				curX += charList.getCharObjects().get(c).getDw()*scale;
//			}
			if (controlCodes.containsKey(i)) {
				String[] controls = controlCodes.get(i).split(",");
				String newListOfControlCodes = "";
				for (String control : controls) {
					if (control.equalsIgnoreCase("NEWLINE")) {
//						curX = x + (int) charList.getCharObjects().get('@').getDw() + 2;
						curX = x;
						curY +=64;
						newListOfControlCodes += ",NEWLINE";
					}
					if (control.equalsIgnoreCase("PROMPTINPUT")) {
						saveStart = i;
						drawUntil = i;
						freeze = true;
						return;
					}
					if (control.startsWith("WAIT")) {
						dramaticPause = true;
						lengthToWait = Integer.parseInt(control.substring(4));
					}
					if (control.startsWith("SC_")) {
						//draw the symbol here
						String specCharName = control.substring(3);
						CharacterData specChar = charList.getSpecChar(specCharName);
						m.renderTile(curX,curY,
								(int) specChar.getDw()*scale,
								(int) specChar.getDh()*scale,
								specChar.getDx(),
								specChar.getDy(),
								specChar.getDw(),
								specChar.getDh());
						curX += scale + specChar.getDw()*scale;
						newListOfControlCodes += "," + control;
					}
					if (control.startsWith("STARTBATTLE_")) {
						String enemyListString = control.substring(12);
						String[] enemyList = enemyListString.split("_");
						BattleMenu bm = new BattleMenu(state);
						ArrayList<Enemy> battleEnemyList = new ArrayList<Enemy>();
						for (int j = 0; j < enemyList.length; j++) {
							battleEnemyList.add(state.enemies.get(enemyList[j]));
						}
						EnemyEntity test = new EnemyEntity(1200, 1200, 24*4,32*4,state,battleEnemyList);
						ArrayList<EnemyEntity> list = new ArrayList<EnemyEntity>();
						list.add(test);
						bm.startBattle(list);
					}
					if (control.startsWith("SETBGM_")) {
						String audio = control.substring(7);
						state.setBGM(audio);
						state.setAudioOverride(true);
					}
					if (control.startsWith("ENDBGM")) {
						state.setAudioOverride(false);
					}
					if (control.startsWith("ADDMEMBER_")) {
						//if the party member is not in the party, add them play the jingle
						String partyKey = control.substring(10);
						if (state.getGameState().addPartyMember(partyKey)) {
							state.setSFX("eb_newchar.wav");
						};
					}
					if (control.startsWith("ADDITEM_")) {
						int itemId = Integer.parseInt(control.substring(8));
						for (PartyMember mi : state.getGameState().getPartyMembers()) {
							int space = mi.getOpenInventorySpace();
							if (space != -1) {
								mi.setItem(state.items.get(itemId),space);
								break;
							}
						}
					}
					if (control.startsWith("CONSUMEITEM_")) {
						int itemId = Integer.parseInt(control.substring(12));
						for (PartyMember mi : state.getGameState().getPartyMembers()) {
							int index;
							while ((index = mi.hasItem(itemId)) != -1) {
								mi.setItem(state.items.get(0),index);
							}
						}
					}
					if (control.startsWith("SETFLAG_")) {
						String flagName = control.substring(8);
						state.getGameState().setFlag(flagName);
					}
					if (control.startsWith("SET_TRAIN_RIDE_")) {
						String[] xy = control.substring(15).split(":");
						int x = Integer.parseInt(xy[0]);
						int y = Integer.parseInt(xy[1]);
						state.getGameState().setTrainRide(x,y);
					}
					if (control.startsWith("SHIFTTEXTUP")) {
						targetY -= 64;
					}
					if (control.startsWith("CHOOSE")) {
						waitingForDecision = true;
						String[] split = textString.split(Pattern.quote("[CHOOSE]"));
						choices = split[1].split(Pattern.quote("[CHOICE]"));
						String[] choiceLabels;
//						drawUntil-=1;
						choiceWindow = new SelectionTextWindow(500,500,2,choices.length,state);
						for (int s = 1; s < choices.length; s++) {
							choiceLabels = choices[s].split(Pattern.quote("[TEXT]"));
							DecisionMenuItem mi = new DecisionMenuItem(choiceLabels[0],s,state);
							choiceWindow.add(mi);
						}
						state.getMenuStack().peek().addMenuItem(choiceWindow);
					}
					if (control.startsWith("SHOP_")) {
						state.getMenuStack().pop();
						ShopMenu sm = new ShopMenu(state, control.substring(5));
						sm.loadShopItems();
						state.getMenuStack().push(sm);
					}
					if (control.startsWith("SET_ITEM_TO_BUY_")) {
						int id = Integer.parseInt(control.substring(16));
						state.setItemToBuy(state.items.get(id));
					}
					if (control.startsWith("SAVE_PROGRESS")) {
						state.getGameState().setFlag("saveGame");
					}
				}
				controlCodes.put(i,newListOfControlCodes);
			}
			if (i < drawUntil) {
				char c = chars[i];
				if (charList.getCharObjects().get(c) == null) {
					continue;
				}
				if (renderWindow != null) {
					if (curY < renderWindow.getY()) {
						continue;
					}
				}
				
				m.renderTile(curX,curY,
						(int) charList.getCharObjects().get(c).getDw()*scale,
						(int) charList.getCharObjects().get(c).getDh()*scale,
						charList.getCharObjects().get(c).getDx(),
						charList.getCharObjects().get(c).getDy(),
						charList.getCharObjects().get(c).getDw(),
						charList.getCharObjects().get(c).getDh());
				curX += scale + charList.getCharObjects().get(c).getDw()*scale;
				if (i >= parsedString.length()-1) {
					done = true;
				}
			}
			
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
					if (!state.getGameState().getFlag(flagName)) {
						//goto [ELSE]
//						int endifIndex = parsedString.indexOf("[ENDIF]");
//						if (parsedString.indexOf("[ELSE]") > endifIndex) {
//							//there is no accompanying else to the if
//							String toRemove = parsedString.substring(parsedString.indexOf("[" + controlCode + "]"),parsedString.indexOf("[ENDIF]")+7);
//							toRemove = toRemove.replace("[","\\[");
//							toRemove = toRemove.replace("]","\\]");
//							i-=controlCode.length()+2;
//							parsedString = parsedString.replaceFirst(toRemove,"");
//						} 
//						else {
							String toRemove = parsedString.substring(parsedString.indexOf("[" + controlCode + "]"),parsedString.indexOf("[ELSE]")+6);
//							toRemove = toRemove.replace("[","\\[");
//							toRemove = toRemove.replace("]","\\]");
							i-=controlCode.length()+2;
							parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
//						}
						parsedString = parsedString.replaceFirst("\\[ENDIF\\]","");
//						parsedString = parsedString.substring(parsedString.lastIndexOf("[ELSE]")+6);
					} else {
//						int endifIndex = parsedString.indexOf("[ENDIF]");
//						if (parsedString.indexOf("[ELSE]") > endifIndex) {
//							//there is no accompanying else to the if
//							parsedString = parsedString.replaceFirst("\\[ENDIF\\]","");
//						} 
//						else {
							String toRemove = parsedString.substring(parsedString.indexOf("[ELSE]"),parsedString.indexOf("[ENDIF]")+7);
//							toRemove = toRemove.replace("[","\\[");
//							toRemove = toRemove.replace("]","\\]");
							parsedString = parsedString.replaceFirst(Pattern.quote(toRemove),"");
//						}
						parsedString = parsedString.replace("[" + controlCode + "]","");
						i-=controlCode.length()+2;
//						parsedString = parsedString.substring(parsedString.indexOf("]")+1,parsedString.lastIndexOf("[ELSE]"));
					}
					stringArray = parsedString.toCharArray();
//					parseIfs();
//					break;
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
		// TODO Auto-generated method stub
//		if (differentParseRate) {
//			oldTicksPerFrame = ticksPerFrame;
//			ticksPerFrame *= 2;
//		}
		if (y > targetY) {
			y-=8;
		}
		if (!drawAll && !dramaticPause) {
			this.tickCount += ticksPerFrame;
			MenuItem it;
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
				drawUntil = 0;
				
			}
			if (tickCount % textRate == 0) {
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

	public void setX(int x) {
		// TODO Auto-generated method stub
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public int getDrawUntil() {
		// TODO Auto-generated method stub
		return drawUntil;
	}

	public boolean getDoneState() {
		// TODO Auto-generated method stub
		return done || freeze || dramaticPause || waitingForDecision;
	}
	
	public boolean getDone() {
		return done;
	}

	public void setTextRate(double d) {
		// TODO Auto-generated method stub
		this.textRate = d;
	}

	public void setState(StartupNew state) {
		this.state = state;
	}

	public void setAsSingleString() {
		// TODO Auto-generated method stub
		isSingleString = true;
	}

	
	
	
}