package battlesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import battlesystem.options.Bash;
import battlesystem.options.BattleAction;
import battlesystem.options.BattleMenuSelectionTextWindow;
import battlesystem.options.BattleSelectionTextWindow;
import battlesystem.options.BattleTextWindow;
import battlesystem.options.EnemyOption;
import battlesystem.options.EnemyOptionPanel;
import battlesystem.options.Goods;
import battlesystem.options.PSI;
import battlesystem.options.RunAway;
import battlesystem.options.Status;
import canvas.Controllable;
import canvas.MainWindow;
import font.SelectionTextWindow;
import font.TextWindowWithPrompt;
import gamestate.BattleEntity;
import gamestate.Enemy;
import gamestate.EnemyEntity;
import gamestate.Entity;
import gamestate.EntityStats;
import gamestate.LevelupData;
import gamestate.PCBattleEntity;
import gamestate.PartyMember;
import global.InputController;
import menu.DrawableObject;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class BattleMenu extends Menu {
	private BattleMenuSelectionTextWindow actionMenu;
	private ArrayList<PartyMember> partyMembers;
	private ArrayList<BattleEntity> party;
	private BattleAction currentAction;
	private int indexMembers;
	private BattleEntity partyMember;
	private ArrayList<BattleEntity> enemies;
	private BattleTextWindow prompt;
	private EnemyOptionPanel eop;
	private ArrayList<BattleEntity> turnStack; //TODO added in the order of speed lowest to high
	private ArrayList<BattleEntity> allEntities;
	private BattleEntity currentTurn;
	private PlayerStatusWindow psw;
	private ArrayList<PlayerStatusWindow> pswList;
	private boolean readyToDisplay;
	private boolean doneActionSelect;
	private boolean needMenu;
	private HashMap<BattleEntity, BattleAction> battleActions;
	private Object displayPrompt ;
	private boolean getNextPrompt;
	private boolean getAnimation;
	private boolean getResultText;
	private boolean waitToStart;
	private ArrayList<MenuItem> windowStack;
	private BattleAction currentBattleAction;
	private boolean pollForActions = false;
	private int expPool;
	private boolean battleSceneEnd;
	private boolean getNext;
	private boolean displayRecExp;
	private boolean doOnce;
	private boolean ended;
	private ArrayList<EnemyEntity> enemyEntities;
	private boolean kill;
	private boolean turnIsDone;
	private boolean alertDeadEntity;
	private BattleEntity deadEntity;
	private boolean locked;
	private boolean levelupDisplay;
	private String levelupString;
	private boolean needResults;
	private boolean needNextPrompt;
	private boolean partyIsDead;
	private boolean endBattleSceneGameOver;
	
	public ArrayList<BattleEntity> getPartyMembers() {
		return party;
	}
	
	public ArrayList<BattleEntity> getEnemies() {
		return enemies;
	}
	
	public PlayerStatusWindow getCurrentPlayerStatusWindow() {
		return this.pswList.get(indexMembers-1);
	}
	
	public int getMemberIndex() {
		return indexMembers;
	}
	
	public void setCurrentAction(BattleAction ba) {
		this.currentAction = ba;
	}
	
	public BattleAction getCurrentAction() {
		return this.currentAction;
	}
	
	public BattleAction getCurrentActiveBattleAction() {
		return this.currentBattleAction;
	}
	
	
	public void addBattleAction(BattleEntity b, BattleAction e) {
		battleActions.put(b,e);
	}
	
	public SelectionTextWindow getActionMenu() {
		return actionMenu;
	}
	
	public EnemyOptionPanel getEnemyOptionPanel() {
		return eop;
	}
	
	public BattleMenu(StartupNew m) {
		super(m);
		windowStack = new ArrayList<MenuItem>();
	}
	
	public void generateGreeting() {
		String greeting = "You encountered a ";
		greeting += enemies.get(0).getName();
		if (enemies.size() > 1) {
			greeting += " and cohorts";
		}
		greeting += ".";
		setPromptGreet(greeting);
		waitToStart = true;
	}
	
	public void startBattle(ArrayList<EnemyEntity> enemyEntities) {
		//set the state to draw all menus at once, to facilitate the menu system
		state.getMenuStack().pop();
		state.setBGM("dangerous foe.ogg");
		this.enemyEntities = enemyEntities;
		expPool = 0;
		state.setDrawAllMenus();
		state.inBattle = true;
		state.battleMenu = this;
		//create a battle menu
		state.getMenuStack().push(this);
		MainWindow mainWindow = state.getMainWindow();
		actionMenu = new BattleMenuSelectionTextWindow(mainWindow.getScreenWidth()/2 - (20/2)*32,100,20,2,state);
		enemies = new ArrayList<BattleEntity>();
		for (EnemyEntity ee : enemyEntities) {
			enemies.addAll(ee.getEnemiesList());
		}
		generateGreeting();
		actionMenu.setKillWhenComplete();
		battleActions = new HashMap<BattleEntity, BattleAction>();
		
		Bash bashButton = new Bash("Bash",0,0,state);
		actionMenu.add(bashButton);
		Goods goodsButton = new Goods("Goods",0,0,state);
		actionMenu.add(goodsButton);
		PSI psiButton = new PSI("PSI",0,0,state);
		actionMenu.add(psiButton);
		Status statusButton = new Status("Status",0,0,state);
		actionMenu.add(statusButton);
		RunAway runButton = new RunAway("Run Away",0,0,state);
		actionMenu.add(runButton);
		
		
		//for each enemy in the battle, draw them on the canvas
		EnemyOptionPanel eop = new EnemyOptionPanel(state);
		this.eop = eop;
		int j = 0;
		for (BattleEntity e : enemies) {
			eop.addEnemyOption(new EnemyOption((Enemy) e,state.getMainWindow().getScreenWidth()/2  - (e.getWidth())*enemies.size()/2 + (e.getWidth() + 32)*j,
					state.getMainWindow().getScreenHeight()/2 -(((Enemy) e).getHeight()),state));
			j++;
		} 
		
		partyMembers = state.getGameState().getPartyMembers();
		party = new ArrayList<BattleEntity>();
		for (PartyMember pm : partyMembers) {
			party.add(pm.createBattleEntity());
		}
		
		
		allEntities = new ArrayList<BattleEntity>();
		allEntities.addAll(party);
		allEntities.addAll(enemies);
		
		pswList = new  ArrayList<PlayerStatusWindow> ();
		int i = 0;
		for (BattleEntity be : party) {
			psw = new PlayerStatusWindow(be,state.getMainWindow().getScreenWidth()/2  - (64*4)*party.size()/2 + (64*4)*i,state.getMainWindow().getScreenHeight()-(64*5),state);
			pswList.add(psw);
			addMenuItem(psw);
			i++;
		}

		turnStack = new ArrayList<BattleEntity>();
	}
	
	public BattleEntity getCurrentPartyMember() {
		return partyMember;
	}
	
	public void setPromptGreet(String s) {
		prompt = new BattleTextWindow(s,state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
		readyToDisplay = true;
		((BattleTextWindow)prompt).setPollForActionsOnExit();
	}
	
	public void setPromptFirst(String s) {
		prompt = new BattleTextWindow(s,state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
		readyToDisplay = true;
		((BattleTextWindow)prompt).loadAnimOnExit();
	}
	
	public void setPromptSecond(String s) {
		if (!s.equals("")) {
			readyToDisplay = true;
			prompt = new BattleTextWindow(s,state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
		} else {
			readyToDisplay = false;
			turnIsDone = true;
		}
		((BattleTextWindow)prompt).setGetResultsOnExit();
		if (turnIsDone) {
			turnIsDone = false;
			((BattleTextWindow)prompt).setPollForActionsOnExit();
			getCurrentActiveBattleAction().setComplete();
			getResultText = false;
		}
	}
	
	public void setPromptDead() {
		prompt = new BattleTextWindow(deadEntity.getName() + " became tame.",state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
		if (needNextPrompt) {
			needNextPrompt = false;
			((BattleTextWindow)prompt).setPollForActionsOnExit();
		}
		prompt.onCompleteKillEntity();
		getResultText = false;
		getNextPrompt = false;
		readyToDisplay = true;
		locked = true;
	}
	
	public void setPromptPartyDead() {
		prompt = new BattleTextWindow("You lost the battle.",state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
		getResultText = false;
		getNextPrompt = false;
		readyToDisplay = true;
		locked = true;
	}
	
	public void setTurnIsDone() {
		turnIsDone = true;
	}
	
	public boolean turnStackIsEmpty() {
		return turnStack.isEmpty();
	}
	
	public void update(InputController input) {
		if (battleSceneEnd && getNext && !ended) {
			//create the You Won prompt, then the exp divying screen, then go through anyone's level ups
			state.setBGM("you win.ogg");
			menuItems.remove(actionMenu);
			ended = true;
			getNext = false;
			prompt = new BattleTextWindow("YOU WON!",state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
//			prompt.setPollForActionsOnExit();
			for (int i = 0; i < partyMembers.size(); i++) {
				partyMembers.get(i).setStats(party.get(i).getStats().getStat("CURHP"),party.get(i).getStats().getStat("CURPP"));
			}
			readyToDisplay = true;
			battleSceneEnd = false;
			doOnce = false;
			displayRecExp = true;
		}
		
		if (displayRecExp && getNext) {
			getNext = false;
			int awardEXP = expPool/party.size();
			prompt = new BattleTextWindow(party.get(0).getName() + " and co. " +  " received " + awardEXP + " experience points.",state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
			levelupString = "";
			for (PartyMember pm : partyMembers) {
				pm.addExp(awardEXP);
				int lv = pm.getStats().getStat("LVL");
				if (pm.getStats().getStat("CURXP") >= LevelupData.getExpToLevel(lv)) {
					EntityStats oldStats = pm.getBaseStats();
					//generate diffs for all of the stats
					//int lvl,int chp, int cpp, int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck, int vit,int curxp
					int[] growth = {18, 5, 4, 7, 5, 5, 6};
					int newOff=Math.max(((growth[0] * (lv+1)) - ((oldStats.getStat("ATK") - 2)*10)) * 5/50,0);
					int newDef=Math.max(((growth[1] * (lv+1)) - ((oldStats.getStat("DEF") - 2)*10)) * 5/50,0);
					int newSpd=Math.max(((growth[2] * (lv+1)) - ((oldStats.getStat("SPD") - 2)*10)) * 5/50,0);
					int newGuts=Math.max(((growth[3] * (lv+1)) - ((oldStats.getStat("GUTS") - 2)*10)) * 5/50,0);
					int newVit=Math.max(((growth[4] * (lv+1)) - ((oldStats.getStat("VIT") - 2)*10)) * 5/50,0);
					int newIQ =Math.max(((growth[5] * (lv+1)) - ((oldStats.getStat("IQ") - 2)*10)) * 5/50,0);
					int newLuck=Math.max(((growth[6] * (lv+1)) - ((oldStats.getStat("LUCK") - 2)*10)) * 5/50,0);
//					int newHP = (growth[0] * lv) - ((oldStats.getStat("ATK") - 2)*10) * 1/5;
//					int newPP = (growth[0] * lv) - ((oldStats.getStat("ATK") - 2)*10) * 1/5;
					int newHP = 10;
					int newPP = 10;
					EntityStats statIncreases = new EntityStats(1,newHP,newPP,newHP,newPP,newOff,newDef,newIQ,newSpd,newGuts,newLuck,newVit,0);
					
					levelupString += pm.getName() + " leveled up![PROMPTINPUT]";
					if (newOff != 0) 
					levelupString += "Offense went up by " + newOff + "[PROMPTINPUT]";
					if (newDef != 0)
					levelupString += "Defense went up by " + newDef + "[PROMPTINPUT]";
					if (newDef != 0)
					levelupString += "Speed went up by " + newSpd + "[PROMPTINPUT]";
					if (newSpd != 0)
					levelupString += "Guts went up by " + newGuts + "[PROMPTINPUT]";
					if (newGuts != 0)
					levelupString += "Vitality went up by " + newVit + "[PROMPTINPUT]";
					if (newVit != 0)
					levelupString += "IQ went up by " + newIQ + "[PROMPTINPUT]";
					if (newIQ != 0)
					levelupString += "Luck went up by " + newLuck + "[PROMPTINPUT]";
					if (newLuck != 0)
					levelupString += "Max HP went up by " + newHP + "[PROMPTINPUT]";
					if (newHP != 0)
					levelupString += "Max PP went up by " + newPP + "[PROMPTINPUT]";
					if (newPP != 0)
					pm.addStats(statIncreases);
					readyToDisplay = true;
					displayRecExp = false;
					levelupDisplay = true;
				}
				//add a check to see if someone is leveling up, if so, then we need to display the level up prompts
				
			}
			if (levelupString.equals("")) {
				kill = true;
				readyToDisplay = true;
				displayRecExp = false;
			}
		}
		
		if (levelupDisplay && getNext) {
			state.setBGM("levelup.ogg");
			getNext = false;
			prompt = new BattleTextWindow(levelupString,state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
			levelupDisplay = false;
			readyToDisplay = true;
			kill = true;
		}
		int numAliveParty = 0;
		if (kill && getNext) {
			state.getMenuStack().pop();
			state.setOldBGM();
			for (EnemyEntity ee : enemyEntities) {
				ee.setToRemove(true);
			}
//			state.getGameState().setCanEncounter(true);
			state.getGameState().setInvincibleCounter();
		}
		
		else if (!battleSceneEnd && !ended){
			if(getNextPrompt || getResultText) {
				for (int i = 0; i < enemies.size(); i++) {
					BattleEntity e = enemies.get(i);
					if (!e.getState().equals("dead")) {
						if (e.getStats().getStat("CURHP") <= 0) {
							alertDeadEntity = true;
							locked = true;
							deadEntity = e;
							break;
						}
					}
				}

				for (int i = 0; i < party.size(); i++) {
					BattleEntity e = party.get(i);
					PlayerStatusWindow psw = pswList.get(i);
					if (!e.getState().equals("dead")) {
						if (psw.getHP() <= 0) {
							alertDeadEntity = true;
							locked = true;
							deadEntity = e;
							break;
						}
					}
				}
				for (BattleEntity be : party) {
					if (!be.getState().equals("dead")) {
						numAliveParty++;
					}
				}
				if (numAliveParty == 0) {
					partyIsDead = true;
					getNext = true;
					getNextPrompt = false;
					getResultText = false;
				}
			}
			
			if (partyIsDead && getNext) {
				setPromptPartyDead();
				endBattleSceneGameOver = true;
				partyIsDead = false;
				getNext = false;
			}
			
			if (endBattleSceneGameOver && getNext) {
				state.getMenuStack().pop();
//				state.getGameState().setCurrentMap("house - myhome");//get the savedmap
//				state.getGameState().createStartPosition();
//				state.getGameState().loadMapData();
//				state.getGameState().getEntityList().clear();
				state.getGameState().reloadInitialMap();
				state.getGameState().setCanEncounter(true);
			}
			
//			if (partyIsDead && getNext) {
//				//load the gameover screen
//				//for now just reload the gamestate map
//				state.getMenuStack().pop();
//			}
			
			if (enemies.size() == 0) {
				endBattleScene();
			}
			
			if (turnStack.isEmpty() && pollForActions && !locked && !battleSceneEnd) {
				battleActions.clear();
				pollForActions = false;
				needMenu = true;
				indexMembers = 0;
				battleActions = new HashMap<BattleEntity,BattleAction>();
				for (BattleEntity be : allEntities) {
					if (!be.getState().equals("dead")) {
						turnStack.add(be);
					}
				}
				sortTurnStackBySpeed();
			}
			
			if (needMenu) {
				windowStack.clear();
				partyMember = party.get(indexMembers);
				while (partyMember.getState().equals("dead")) {
					if (indexMembers < party.size()) {
						indexMembers++;
						partyMember = party.get(indexMembers);
					}else {
						
					}
				}
				partyMember = party.get(indexMembers);
				indexMembers++;
				addToMenuItems(actionMenu);
				needMenu = false;
				
			}
			
			if (doneActionSelect) {
				setToRemove(actionMenu);
				doneActionSelect = false;
				addBattleAction(partyMember,currentAction);
				if (indexMembers < party.size()) {
					needMenu = true;
				}
				else {
					indexMembers = 0;
					//generate battle actions for each enemy
					for (BattleEntity e : enemies) {
						//create stubs
						BattleAction ba = new BattleAction(state);
						ba.setUser(e);
						ba.setTargets(party,party.get(0),false);
						ba.setAction("bash");
						addBattleAction(e,ba);
					}
				}
			}
			
//			if (getResultText) {
				
//			}
			
			if (battleActions.size() == enemies.size() + numAliveParty && getNextPrompt && !alertDeadEntity && enemies.size() > 0) {
				if (currentBattleAction == null || currentBattleAction.isComplete()) {
					BattleEntity be = turnStack.remove(turnStack.size()-1);
					currentBattleAction = battleActions.get(be);
					if (be instanceof PCBattleEntity) {
						for (BattleEntity pbe : party) {
							if (pbe == be) {
								indexMembers =  party.indexOf(pbe)+1;
							}
						}
					} else {
						indexMembers = 0;
					}
				}
				setPromptFirst(currentBattleAction.getBattleActionString());
				getNextPrompt = false;
			}
			
			if (getAnimation) {
				try {
					currentBattleAction.createAnim(state);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getAnimation = false;
			}
			
//			for (MenuItem i : menuItems) {
//				i.updateAnim();
//			}
			
			if (getResultText && alertDeadEntity) {
				getResultText = false;
				needResults = true;
				setPromptDead();
			}
			
			if (getResultText && !alertDeadEntity) {
				getResultText = false;
				setPromptSecond(currentBattleAction.doAction());
			}
			
			if (getNextPrompt && alertDeadEntity) {
				alertDeadEntity = false;
				getNextPrompt = false;
				needNextPrompt = true;
				setPromptDead();
			}
			
			for (int i = 0; i < party.size(); i++) {
				pswList.get(i).setY(state.getMainWindow().getScreenHeight()-(64*5));
				pswList.get(i).updateStatus(party.get(i).getStats().getStat("CURHP"),party.get(i).getStats().getStat("CURPP"));
				if (i == indexMembers-1 && i >= 0) {
					pswList.get(i).setY(state.getMainWindow().getScreenHeight()-(64*5)-32);
				}
			}
			
			
			
		}
		
		if (readyToDisplay) {
			windowStack.clear();
			addToMenuItems(prompt);
			readyToDisplay = false;
			prompt.setGetNext();
		}
		
	}
	
	public void sortTurnStackBySpeed()
    {
        int n = turnStack.size();
 
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            double rand1 = Math.random();
            double rand2 = Math.random();
            double adj1 = 1.5;
            double adj2 = 1.5;
            if (rand1 < 0.5) {
            	adj1 *= -1;
            }
            if (rand2 < 0.5) {
            	adj2 *= -1;
            }
            for (int j = i+1; j < n; j++) {
            	 if (turnStack.get(j).getStats().getStat("SPD") * adj1
                 		< turnStack.get(min_idx).getStats().getStat("SPD") * adj2) {
            		 min_idx = j;
            	 }
            }
               
 
            // Swap the found minimum element with the first
            // element
            BattleEntity temp = turnStack.get(min_idx);
            turnStack.remove(min_idx);
            turnStack.add(min_idx, turnStack.get(i));
            turnStack.remove(i);
            turnStack.add(i,temp);
        }
    }

	public void killDeadEntity() {
		if (needResults) {
			getResultText = true;
		}
		locked = false;
		turnStack.remove(deadEntity);
		battleActions.remove(deadEntity);
		if (deadEntity instanceof Enemy) {
			currentBattleAction.indexDown();
			enemies.remove(deadEntity);
			allEntities.remove(deadEntity);
			this.eop = new EnemyOptionPanel(state);
			int j = 0;
			for (BattleEntity en : enemies) {
				eop.addEnemyOption(new EnemyOption((Enemy) en,state.getMainWindow().getScreenWidth()/2  - (en.getWidth())*enemies.size()/2 + (en.getWidth() + 32)*j,
						state.getMainWindow().getScreenHeight()/2 -(((Enemy) en).getHeight()),state));
				j++;
			} 
			expPool += ((Enemy)deadEntity).getExpYield();
		}
		else if (deadEntity instanceof PCBattleEntity) {
			//dont remove, just set the state of the pc to dead
			((PCBattleEntity) deadEntity).setState("dead");
			for (BattleEntity be : battleActions.keySet()) {
				BattleAction ba = battleActions.get(be);
				if (ba.getTarget().equals(deadEntity)) {
//					if (ba.getActor() instanceof PCBattleEntity) {
						for (BattleEntity pbe : party) {
							if (!pbe.getState().equals("dead")) {
								battleActions.get(be).setTargets(party,pbe,false);
							}
						}
//					} else if (ba.getActor() instanceof PCBattleEntity) {
						
//					}					
				}
			}
		}
		deadEntity = null;
		alertDeadEntity = false;
	}
	
	private void endBattleScene() {
		// TODO Auto-generated method stub
		battleSceneEnd = true;
		getNext = true;
	}

	public void setDoneAction() {
		// TODO Auto-generated method stub
		doneActionSelect = true;
	}

	public void setGetNextPrompt() {
		// TODO Auto-generated method stub
		getNextPrompt  = true;
	}
	
	public void popWindowStackAndRemoveMI(MenuItem i) {
		if (windowStack.size() != 0) {
			needToRemove.add(i);
			addToMenuItems(windowStack.remove(windowStack.size()-1));
		} else {
			if (indexMembers > 1) {
				needToRemove.add(actionMenu);
				needMenu = true;
				indexMembers-=2;
			}
			
		}
	}
	

	public List<DrawableObject> getDrawableObjects() {
		drawables = new ArrayList<DrawableObject>();
		drawables.add(eop);
		drawables.addAll(windowStack);
		drawables.addAll(menuItems);
		
		return drawables;
	}
	
	public void setToRemove(MenuItem i) {
		if (i instanceof BattleSelectionTextWindow) {
			windowStack.add(i);
		}
		
		needToRemove.add(i);
	}

	public void setGetAnimation(boolean b) {
		// TODO Auto-generated method stub
		getAnimation = b;
	}

	public void setGetResultText() {
		// TODO Auto-generated method stub
		getResultText =  true;
	}

	public void setPollForActions() {
		// TODO Auto-generated method stub
		pollForActions = true;
	}

	public void setGetNext() {
		// TODO Auto-generated method stub
		getNext=true;
	}

}
