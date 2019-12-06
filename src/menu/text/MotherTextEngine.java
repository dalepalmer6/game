package menu.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import battlesystem.Enemy;
import font.CharList;
import font.CharacterData;
import gamestate.entities.EnemyEntity;
import gamestate.partymembers.PartyMember;
import menu.animation.AnimationMenu;
import menu.animation.SwirlAnimation;
import menu.atmmenu.ATMMenu;
import menu.atmmenu.DigitScroller;
import menu.shopmenu.ShopMenu;
import menu.windows.SelectionTextWindow;
import system.MainWindow;
import system.MotherSystemState;
import system.SystemState;
import system.characters.PartyCharacter;

public class MotherTextEngine extends TextEngine {

	public MotherTextEngine(boolean shouldDrawAll, String s, int x, int y, int width, int height, CharList cd) {
		super(shouldDrawAll, s, x, y, width, height, cd);
	}

	protected MotherSystemState state;

	public void setState(SystemState state) {
		this.state = (MotherSystemState) state;
	}
	
	@Override
	public void handleControlCodes(int i, MainWindow m, int curX, int curY, char[] chars, int scale) {
		if (controlCodes.containsKey(i)) {
			String[] controls = controlCodes.get(i).split(",");
			String newListOfControlCodes = "";
			for (String control : controls) {
				if (control.equalsIgnoreCase("NEWLINE") || control.equalsIgnoreCase("NEWLINE_TOO_LONG")) {
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
					//							drawStart = i;
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
					//							bm.startBattle(list);
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
					//							int itemId = Integer.parseInt(control.substring(12));
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
					//							String[] split = textString.split(Pattern.quote("[CHOOSE]"));
					choices = textString.split(Pattern.quote("[CHOICE]"));
					String[] choiceLabels;
					//							drawUntil-=1;
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
				//						if (control.startsWith("WINDOWARG")) {
				//							int arg = state.getGameState().getWindowArgument();
				//						}
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
					((MotherSystemState) state).setItemToBuy(state.items.get(id));
				}
				if (control.startsWith("SAVE_PROGRESS")) {
					state.getGameState().setFlag("saveGame");
				}
			}
			controlCodes.put(i,newListOfControlCodes);
		}
	}

	@Override
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
					replacement = PartyCharacter.NINTEN.getNameOfCharacter();
				}
				if (controlCode.equals("ANA")) {
					replacementCode = true;
					replacement = PartyCharacter.ANA.getNameOfCharacter();
				}
				if (controlCode.equals("LOID")) {
					replacementCode = true;
					replacement = PartyCharacter.LOID.getNameOfCharacter();
				}
				if (controlCode.equals("TEDDY")) {
					replacementCode = true;
					replacement = PartyCharacter.TEDDY.getNameOfCharacter();
				}
				if (controlCode.equals("FAVFOOD")) {
					replacementCode = true;
					replacement = PartyCharacter.FAVFOOD.getNameOfCharacter();
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

}
