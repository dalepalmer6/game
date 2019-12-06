package menu.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import battlesystem.BattleEntity;
import battlesystem.Enemy;
import battlesystem.menu.BattleMenu;
import font.CharList;
import font.CharacterData;
import gamestate.entities.EnemyEntity;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.animation.AnimationMenu;
import menu.animation.SwirlAnimation;
import menu.atmmenu.ATMMenu;
import menu.atmmenu.DigitScroller;
import menu.shopmenu.ShopMenu;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.MainWindow;
import system.SystemState;
import system.controller.InputController;
import system.interfaces.Controllable;
import system.interfaces.Drawable;

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
	private SystemState state;
	private boolean ignoreControlCodes = false;
	private TextWindow renderWindow;
	private int targetY;
	private boolean waitingForDecision;
	private int choice;
	private String[] choices;
	private SelectionTextWindow choiceWindow;
	private boolean isSingleString;
	private double shakeApplyY = 0;
	private int drawingY;
	private ArrayList<String> flags;
	private boolean waitingForNumberInput;
	private DigitScroller digitScroller;
	private boolean waitForAudio;
	
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
	public Text(String s, int x, int y,int width, int height, CharList cd) {
		this(false,s,x,y,width,height,cd);
	}
	
	public Text(boolean shouldDrawAll, String s, int x, int y,int width, int height, CharList cd) {
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
		char[] stringArray = parsedString.toCharArray();
		String controlCode = "";
		int indexOfCode = 0;
		boolean foundChoose = false;
		boolean parsingControlCode = false;
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray[i] == ',') {
				controlCode = "WAIT10";
				controlCodes.put(i+1,controlCode);
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
				String controlCodesList = controlCode;
				if (controlCodes.containsKey(indexOfCode)) {
					controlCodesList = controlCodes.get(indexOfCode) + "," + controlCode;
				}
				if (controlCode.startsWith("CHOOSE")) {
//					if (!ignoreControlCodes) {
						foundChoose = true;
//					}
//					return;
				}
				if (controlCode.startsWith("_") && !foundChoose) {
					int indexInString = Integer.parseInt(controlCode.substring(1),16);
					String newline = "[NEWLINE]";
					if (indexOfCode == 0) {
						newline = "";
					}
					parsedString = parsedString.replaceFirst(Pattern.quote("[" + controlCode + "]"), newline + state.textData.get(indexInString));
					textString = parsedString;
					parsingControlCode = false;
					i-=controlCode.length()+2;
					stringArray = parsedString.toCharArray();
					continue;
				}
				boolean replacementCode = false;
				String replacement = "";
				if (controlCode.equals("MONEYSIGN")) {
					replacementCode = true;
					parsedString = parsedString.replaceFirst(Pattern.quote("[" + controlCode + "]"), "\\$");
					parsingControlCode = false;
					i-=controlCode.length()+2;
					stringArray = parsedString.toCharArray();
				}
				if (controlCode.equals("NINTEN")) {
					replacementCode = true;
					replacement =state.namesOfCharacters[0];
				}
				if (controlCode.equals("ANA")) {
					replacementCode = true;
					replacement =state.namesOfCharacters[1];
				}
				if (controlCode.equals("LOID")) {
					replacementCode = true;
					replacement =state.namesOfCharacters[2];
				}
				if (controlCode.equals("TEDDY")) {
					replacementCode = true;
					replacement = state.namesOfCharacters[3];
				}
				if (controlCode.equals("FAVFOOD")) {
					replacementCode = true;
					replacement = state.namesOfCharacters[4];
				}
				if (controlCode.equals("WINDOWARG")) {
					replacementCode = true;
					replacement = "" + state.getGameState().getWindowArgument();
				}
				if (controlCode.equals("DADDEPOSITED")) {
					replacementCode = true;
					replacement = "" + state.getGameState().getFundsDeposited();
				}
				if (controlCode.equals("BANKFUNDS")) {
					replacementCode = true;
					replacement = "" + state.getGameState().getFundsInBank();
				}
				
				if (replacementCode) {
					parsedString = parsedString.replaceFirst(Pattern.quote("[" + controlCode + "]"), replacement);
					parsingControlCode = false;
					i-=controlCode.length()+2;
					stringArray = parsedString.toCharArray();
					continue;
				}
				controlCodes.put(indexOfCode,controlCodesList);
				if (controlCode.startsWith("NUMSCROLL_")) {
					parsedString = parsedString.substring(0,parsedString.indexOf("[" + controlCode +"]"));
					return;
				}
				parsedString = parsedString.replaceFirst(Pattern.quote("[" + controlCode + "]"), "");
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
	
	//TODO - CLEAN UP IN HERE - ALOT OF CODE CAN BE MOVED TO AN UPDATE METHOD
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
//				if (key >= parsedString.length()) {
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
			if (controlCodes.containsKey(i)) {
				String[] controls = controlCodes.get(i).split(",");
				String newListOfControlCodes = "";
				for (String control : controls) {
					if (control.equalsIgnoreCase("NEWLINE") || control.equalsIgnoreCase("NEWLINE_TOO_LONG")) {
//						curX = x + (int) charList.getCharObjects().get('@').getDw() + 2;
						curX = x;
						curY +=64;
						if (chars[i] == '@') {
							//draw it in the margin
							drawChar(m,i,'@',curX-4*4,curY);
						}
						newListOfControlCodes += "," + control;
					}
					if (control.equalsIgnoreCase("PROMPTINPUT")) {
						saveStart = i;
						drawUntil = i;
						freeze = true;
						GL11.glColor3f(255,255,255);
						return;
					}
					if (control.startsWith("WAIT")) {
						dramaticPause = true;
						lengthToWait = Integer.parseInt(control.substring(4));
					}
					if (control.startsWith("ATMWITHDRAW")) {
						state.getMenuStack().pop();
						ATMMenu atm = new ATMMenu(state);
						atm.createWithdraw();
						state.getMenuStack().push(atm);
					}
					if (control.startsWith("ATMDEPOSIT")) {
						state.getMenuStack().pop();
						ATMMenu atm = new ATMMenu(state);
						atm.createDeposit();
						state.getMenuStack().push(atm);
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
//						drawStart = i;
						drawStart = 0;
						drawUntil = 1;
						parsedString = parsedString.substring(i);
						int whiteSpaceRemoved = 0;
						controlCodes.put(i,controlCodes.get(i).replaceFirst(Pattern.quote("," + control),""));
						while (parsedString.substring(0,1).equals(" ")) {
							parsedString = parsedString.substring(1);
							whiteSpaceRemoved++;
						}
						if (parsedString.length() == 0) {
							done = true;
							return;
						}
						HashMap<Integer,String> newCCs = new HashMap<Integer,String>();
						for (int x : controlCodes.keySet()) {
							String controlCode = controlCodes.get(x);
							if (controlCode.contains("NEWLINE_TOO_LONG")) {
								controlCode = controlCode.replaceAll(Pattern.quote("NEWLINE_TOO_LONG"),"");
								parsedString = parsedString.substring(0,x - i - whiteSpaceRemoved) + " " + parsedString.substring(x - i - whiteSpaceRemoved);
							}
							if (controlCode.contains("SHIFTTEXTUP")) {
								controlCode = controlCode.replaceAll(Pattern.quote("SHIFTTEXTUP"),"");
							}
							newCCs.put(x - i - whiteSpaceRemoved,controlCode);
						}
						
						i = 0;
						
						controlCodes = newCCs;
						
						reparseForWidths();
						reparseForHeights();
						
						state.saveCurrentDialogMenu();
						
						state.saveAudio();
						String enemyListString = control.substring(12);
						BattleMenu bm = new BattleMenu(state);
						ArrayList<EnemyEntity> list = new ArrayList<EnemyEntity>();
						ArrayList<Enemy> battleEnemyList = new ArrayList<Enemy>();
						if (enemyListString.equals("WINDOWARG")) {
							try {
								battleEnemyList.add(state.enemies.get(state.getGameState().getWindowArgument()));
								list.add(new EnemyEntity(0,0,0,0,state,battleEnemyList));
							} catch(ArrayIndexOutOfBoundsException e) {
								
							}
						} else {
							String[] enemyList = enemyListString.split("_");
							for (int j = 0; j < enemyList.length; j++) {
								battleEnemyList.add(state.enemies.get(Integer.parseInt(enemyList[j])));
							}
							EnemyEntity test = new EnemyEntity(1200, 1200, 24*4,32*4,state,battleEnemyList);
							list.add(test);
						}
						SwirlAnimation anim = new SwirlAnimation(state,list);
						AnimationMenu menu = new AnimationMenu(state);
						menu.createAnimMenu(anim);
						state.getMenuStack().push(menu);
						anim.createAnimation();
//						bm.startBattle(list);
					}
					if (control.startsWith("PLAYSFX_")) {
						String audio = control.substring(8);
						state.setSFX(audio);
					}
					if (control.startsWith("SETBGM_")) {
						String audio = control.substring(7);
						state.saveAudio();
						state.setBGM(audio);
						state.setRestoreAudioWhenDone();
						waitForAudio = true;
					}
					if (control.startsWith("STOPBGM")) {
						state.stopBGM();
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
//						int itemId = Integer.parseInt(control.substring(12));
						String itemId = control.substring(12);
						if (itemId.contains("_")) {
							//have a specified index
							String[] split = itemId.split("_");
							int id = Integer.parseInt(split[0]);
							int memberId = Integer.parseInt(split[1]);
							state.getGameState().getPartyMembers().get(memberId).consumeItem(id);
						} else {
							int id = Integer.parseInt(control.substring(12));
							for (PartyMember mi : state.getGameState().getPartyMembers()) {
								int index;
								while ((index = mi.hasItem(id)) != -1) {
									mi.setItem(state.items.get(0),index);
								}
							}
						}
					}
					if (control.startsWith("ADDMONEY_")) {
						int money = -Integer.parseInt(control.substring(9));
						state.getGameState().spendFunds(money); //we use the negative value
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
					if (control.startsWith("CHOOSE") && !ignoreControlCodes) {
						waitingForDecision = true;
//						String[] split = textString.split(Pattern.quote("[CHOOSE]"));
						choices = textString.split(Pattern.quote("[CHOICE]"));
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
					if (control.startsWith("WARP_")) {
						state.getMenuStack().pop();
						String vars = control.substring(5);
						String[] varsSplit = vars.split("_");
						state.getGameState().createWarp(varsSplit[0],Integer.parseInt(varsSplit[1])*4,4*Integer.parseInt(varsSplit[2]));
					}
					if (control.startsWith("NUMSCROLL_")) {
						int digitSize = Integer.parseInt(control.substring(10));
						waitingForNumberInput = true;
						digitScroller = new DigitScroller(state,digitSize);
						
						state.getMenuStack().peek().addMenuItem(digitScroller);
					}
//					if (control.startsWith("WINDOWARG")) {
//						int arg = state.getGameState().getWindowArgument();
//					}
					if (control.startsWith("HAVEFUNDS_")) {
						int amount = Integer.parseInt(control.substring(9));
						if (state.getGameState().getFundsOnHand() < amount) {
							
						}
					}
					if (control.startsWith("DEPOSITFUNDS_")) {
						int amount = 0;
						if (control.substring(13).equals("")) {
							amount = state.getGameState().getWindowArgument();
						} else {
							amount = Integer.parseInt(control.substring(13));
						}
						state.getGameState().depositFunds(amount);
					}
					if (control.startsWith("WITHDRAWFUNDS_")) {
						int amount = 0;
						if (control.substring(14).equals("")) {
							amount = state.getGameState().getWindowArgument();
						} else {
							amount = Integer.parseInt(control.substring(14));
						}
						state.getGameState().withdrawFunds(amount);
					}
					if (control.startsWith("SHOP_")) {
						state.getMenuStack().pop();
						ShopMenu sm = new ShopMenu(state, control.substring(5));
						sm.loadShopItems();
						state.getMenuStack().push(sm);
					}
					if (control.equals("SELLWINDOW")) {
						((ShopMenu) state.getMenuStack().peek()).addSellWindow();
					}
					if (control.equals("BUYWINDOW")) {
						((ShopMenu) state.getMenuStack().peek()).addBuyWindow();
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		this.x = (int) x;
	}
	
	public void setY(double y) {
//		this.y = y;
		drawingY = (int) y;
	}

	public int getDrawUntil() {
		// TODO Auto-generated method stub
		return drawUntil;
	}

	public boolean getDoneState() {
		// TODO Auto-generated method stub
		return done || freeze || dramaticPause || waitingForDecision || waitingForNumberInput || waitForAudio;
	}
	
	public boolean getDone() {
		return done;
	}

	public void setTextRate(double d) {
		// TODO Auto-generated method stub
		this.textRate = d;
	}

	public void setState(SystemState state) {
		this.state = state;
	}

	public void setAsSingleString() {
		// TODO Auto-generated method stub
		isSingleString = true;
	}

	public void setShakingY(double applyShake) {
		// TODO Auto-generated method stub
		drawingY = (int) (shakeApplyY + y);
		shakeApplyY = applyShake;
	}

	public void draw(MainWindow m) {
		// TODO Auto-generated method stub
		draw(m,0,0,0);
	}

	public void setParsedString() {
		// TODO Auto-generated method stub
		parsedString = textString;
	}

	public int getTargetY() {
		// TODO Auto-generated method stub
		return targetY;
	}

	public void shift() {
		// TODO Auto-generated method stub
		
	}

	public void setTargetY(int i) {
		// TODO Auto-generated method stub
		targetY = i;
	}
	
	
	
}