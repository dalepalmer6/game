package battlesystem.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import battlesystem.BattleEntity;
import battlesystem.Enemy;
import battlesystem.PCBattleEntity;
import battlesystem.options.BattleAction;
import battlesystem.options.BattleSelectionTextWindow;
import battlesystem.options.BattleTextWindow;
import battlesystem.options.EnemyOption;
import battlesystem.options.EnemyOptionPanel;
import gamestate.EntityStats;
import gamestate.entities.EnemyEntity;
import gamestate.partymembers.PartyMember;
import menu.DrawableObject;
import menu.Menu;
import menu.MenuItem;
import menu.PlayerStatusWindow;
import menu.TexturedMenuItem;
import menu.animation.AnimationMenu;
import menu.gameovermenu.GameOverMenu;
import menu.windows.TextWindow;
import system.MotherSystemState;
import system.SystemState;
import system.controller.InputController;
import system.data.LevelupData;
import system.data.StatusConditions;

public class BattleMenu extends Menu {
//	private BattleMenuSelectionTextWindow actionMenu;
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
	private int moneyGained;
	private boolean allActionsMade;
	private ArrayList<BattleEntity> deadEntities;
	private boolean forceWaitForPrompt;
	private TexturedMenuItem youWon;
	private BattleActionMenu actionMenu;
	private boolean mortalDamage;
	private String mortalDeathString;
	private BattleTextWindow promptBak;
	
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
	
//	public SelectionTextWindow getActionMenu() {
//		return actionMenu;
//	}
	
	public EnemyOptionPanel getEnemyOptionPanel() {
		return eop;
	}
	
	public BattleMenu(SystemState m) {
		super(m);
		windowStack = new ArrayList<MenuItem>();
	}
	
	public String predicateToUpperCase(String p) {
		if (p.equals("") || p.equals(" ")) {
			return "";
		}
		return p.substring(0,1).toUpperCase() + p.substring(1) + " ";
	}
	
	public void generateGreeting() {
		String pred = enemies.get(0).getPredicate();
		pred = predicateToUpperCase(pred);
		String name = enemies.get(0).getName();
		String and = null;
		if (enemies.size() > 1) {
			and = "and cohorts";
		} else {
			and = "";
		}
		String greet = "suddenly attacked";
		String greeting = String.format("%s %s %s %s!",pred,name,and,greet);
		setPromptGreet(greeting);
		waitToStart = true;
	}
	
	public void createActionMenu() {
		BattleActionMenu bam = new BattleActionMenu(state,partyMembers,indexMembers-1);
		bam.createMenu();
		state.getMenuStack().push(bam);
		if (indexMembers - 1 != 0 || actionMenu != null) {
			bam.setDoAnim(false);
		}
		actionMenu = bam;
	}
	
	public void startBattle(ArrayList<EnemyEntity> enemyEntities) {
		//set the state to draw all menus at once, to facilitate the menu system
		deadEntities = new ArrayList<BattleEntity>();
		state.getMenuStack().pop();
//		state.setBGM(enemy); //depend on the enemy
		this.enemyEntities = enemyEntities;
		expPool = 0;
		state.setDrawAllMenus(true);
		state.inBattle = true;
		state.battleMenu = this;
		//create a battle menu
		state.getMenuStack().push(this);
		
		enemies = new ArrayList<BattleEntity>();
		for (EnemyEntity ee : enemyEntities) {
			enemies.addAll(ee.getEnemiesList());
		}
		//for each enemy in the battle, draw them on the canvas
		EnemyOptionPanel eop = new EnemyOptionPanel(state);
		this.eop = eop;
		int j = 0;
		for (BattleEntity e : enemies) {
			eop.addEnemyOption(new EnemyOption((Enemy) e,state.getMainWindow().getScreenWidth()/2  - (e.getWidth())*enemies.size()/2 + (e.getWidth() + 32)*j,
			state.getMainWindow().getScreenHeight()/2 -(((Enemy) e).getHeight()/2),state));
			j++;
		} 
		((MotherSystemState) state).setEOP(eop);
		state.setAudioOverride(false);
		state.setBGM(((Enemy)enemies.get(0)).getBGM());
		state.setAudioOverride(true);
		generateGreeting();
		
		battleActions = new HashMap<BattleEntity, BattleAction>();
		
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
	
	public ArrayList<PlayerStatusWindow> getPSWs() {
		return pswList;
	}
	
	public BattleEntity getCurrentPartyMember() {
		return partyMember;
	}
	
	public void setPromptGreet(String s) {
		prompt = createTextWindow(s);
		readyToDisplay = true;
		((BattleTextWindow)prompt).setPollForActionsOnExit();
	}
	
	public void setPromptFirst(String s) {
		prompt = createTextWindow(s);
		readyToDisplay = true;
		((BattleTextWindow)prompt).loadAnimOnExit();
	}
	
	public void setPromptMortal() {
		prompt = createTextWindow(mortalDeathString);
		if (needNextPrompt) {
			needNextPrompt = false;
			prompt.setPollForActionsOnExit();
		}
		readyToDisplay = true;
		((BattleTextWindow)prompt).setPollForActionsOnExit();
	}
	
	public void setPromptSecond(String s) {
		if (s.equals("donothing")) {
			readyToDisplay = false;
			if (turnStackIsEmpty() && currentBattleAction.isComplete()) {
				setPollForActions();
			}
			if (currentBattleAction.isComplete()) {
				setGetNextPrompt();
			}
		}
		else if (!s.equals("")) {
			readyToDisplay = true;
			prompt = createTextWindow(s);
		} else {
			if (currentBattleAction.isComplete() || enemies.size() == 0) {
				if (turnStack.isEmpty()) {
					setPollForActions();
				}
				
				return;
			}
		}
		if (currentBattleAction.needDamageNums()) {
			//the prompt is empty, so show the damage dealt on the entity
			boolean disallowAutoProgress = readyToDisplay;
			readyToDisplay = false || readyToDisplay;
			if (currentBattleAction.isComplete() || enemies.size() == 0) {
				if (turnStack.isEmpty()) {
					setPollForActions();
				}
				return;
			} else {
				//get the x y coordinates to put the damage
				
				DamageMenuItem mi = getDamageMenuItem(currentBattleAction.getDamageDealt(), currentBattleAction.getTarget());
				
				
				mi.setDisallowAutoProgress(disallowAutoProgress);
				mi.setIsHealing(currentBattleAction.isHealing());
				
				if (currentBattleAction.isSmash()) {
					SmashMenuItem smi = new SmashMenuItem(mi.getX(),mi.getY(),71*4,15*4,state,"battlehud.png",284,0,71,15);
					addMenuItem(smi);
				}
				
				addMenuItem(mi);
			}
		}
		
		if (currentBattleAction.isContinuous() && currentBattleAction.needAnim() && !currentBattleAction.isComplete()) {
			prompt.loadAnimOnExit();
		} else if (!currentBattleAction.isComplete()){
			prompt.setGetResultsOnExit();
		}
		
		if (turnIsDone) {
			turnIsDone = false;
			prompt.setPollForActionsOnExit();
			currentBattleAction.setComplete();
			getResultText = false;
			return;
		}
	}
	
	public DamageMenuItem getDamageMenuItem(int dmg, BattleEntity target) {
		int x = 0;
		int y = 0;
		if (target instanceof PCBattleEntity) {
			int index = party.indexOf(target);
			PlayerStatusWindow psw = pswList.get(index);
			x = (int) (psw.getX() + psw.getWidth()/2);
			y = (int) (psw.getY() + psw.getHeight()/2);
		} else {
			int index = enemies.indexOf(target);
			EnemyOption eo = eop.getEnemyOptions().get(index);
			x = (int) (eo.getX() + eo.getWidth()/2);
			y = (int) (eo.getY() + eo.getHeight()/2);
			eop.setToShake(index);
			if (currentBattleAction.getTarget().getBattleStats().getStat("CURHP") <= 0) {
				eop.setKilled(index);
			}
		}
		DamageMenuItem mi = new DamageMenuItem(dmg,x,y,state);
		return mi;
	}
	
	public void setPromptDead() {
		String text = "";
//		while (state.getMenuStack().peek() != this) {
//			state.getMenuStack().pop();
//		}
		if (deadEntity instanceof PCBattleEntity) {
			text = "[PLAYSFX_die.wav]" + deadEntity.getName() + " got hurt and collapsed!";
		} else {
			text = deadEntity.getName() + " " + deadEntity.getKillText(); 
		}
		prompt = createTextWindow(text);
		for (MenuItem i : menuItems) {
			if (i instanceof TextWindow) {
				setToRemove(i);
//				if (i == actionMenu) { // original
			}
		}
		if (actionMenu != null) { // try fix
			if (state.getMenuStack().peek() != actionMenu) {
				do {
					state.getMenuStack().pop();
				} while (state.getMenuStack().peek() != actionMenu);
			}
			if (state.getMenuStack().peek() == actionMenu) {
				state.getMenuStack().pop();
			}
			
			indexMembers--;
			while (!checkIfPlayerCanAct(party.get(indexMembers))) {
				indexMembers++;
				if (indexMembers >= party.size()) {
					break;
				}
			}
			prompt.setNeedMenuOnExit();
		}
		//flavor text for enemy or party members
		
		if (needNextPrompt) {
			needNextPrompt = false;
			prompt.setPollForActionsOnExit();
		}
		prompt.onCompleteKillEntity();
//		getResultText = false;
//		getNextPrompt = false;
		readyToDisplay = true;
		locked = true;
	}
	
	public void setPromptPartyDead() {
		prompt = createTextWindow(String.format("%s lost the battle.", partyMembers.get(0).getName()));
		while (state.getMenuStack().peek() != this) {
			state.getMenuStack().pop();
		}
		actionMenu = null;
		getResultText = false;
		getNextPrompt = false;
		readyToDisplay = true;
		locked = true;
	}
	
	public BattleTextWindow createTextWindow(String s) {
		return new BattleTextWindow(s,state.getMainWindow().getScreenWidth()/2 - (16/2)*72,32,16,2,state);
	}
	
	public void setTurnIsDone() {
		turnIsDone = true;
	}
	
	public boolean turnStackIsEmpty() {
		return turnStack.isEmpty();
	}
	
	public void createYouWin() {
		state.setAudioOverride(false);
		state.setBGM("you win.ogg");
//		state.setAudioOverride(true);
//		menuItems.remove(actionMenu);
		ended = true;
		getNext = false;
		prompt = createTextWindow(" ");
		youWon = new TexturedMenuItem("You win",prompt.getX() + prompt.getWidth()*2 - (74*4/2),prompt.getY() + 64,74*4,8*4,state,"battlehud.png",128,21,74,8);
//		for (int i = 0; i < partyMembers.size(); i++) {
//			partyMembers.get(i).setStats(party.get(i).getStats().getStat("CURHP"),party.get(i).getStats().getStat("CURPP"),party.get(i).getState());
//		}
		readyToDisplay = true;
//		battleSceneEnd = false;
		doOnce = false;
		displayRecExp = true;
	}
	
	public void showWinnings() {
		menuItems.remove(youWon);
		getNext = false;
		int awardEXP = Math.max(1,expPool/calculateAlivePartyMembers());
		state.getGameState().addFundsToBank(moneyGained);
		
		String and = null;
		if (getAliveMembers().size() == 1) {
			and = "";
		} else {
			and = "and co.";
		}
		
		String results = String.format("%s %s received %d experience points.",getAliveMembers().get(0).getName(),and,awardEXP);
		
		prompt = createTextWindow(results);
		levelupString = "";
		int index = 0;
		for (PartyMember pm : partyMembers) {
			if (!checkIfPlayerCanAct(party.get(index++))) {
				continue;
			}
			pm.addExp(awardEXP);
			int lv = pm.getStats().getStat("LVL");
			int newOff = 0;
			int newDef = 0;
			int newSpd = 0;
			int newGuts = 0;
			int newVit = 0;
			int newIQ = 0;
			int newLuck = 0;
			int newHP = 0;
			int newPP = 0;
			boolean leveledUp = false;
			long newPSI = 0;
			long oldPSI = pm.getKnownPSI();
			EntityStats statIncreases = new EntityStats(1,newHP,newPP,newHP,newPP,newOff,newDef,newIQ,newSpd,newGuts,newLuck,newVit,0);
			while (pm.getStats().getStat("CURXP") >= LevelupData.getExpToLevel(lv+1)) {
				newPSI = oldPSI | LevelupData.getPSIShouldBeKnown(pm.getId().toLowerCase(),lv,oldPSI);
				pm.addStats(statIncreases);
				lv++;
				EntityStats oldStats = pm.getBaseStats();
				//generate diffs for all of the stats
				//int lvl,int chp, int cpp, int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck, int vit,int curxp
				int[] growth = {18, 5, 4, 7, 5, 5, 6};
				newOff+=Math.max(((growth[0] * (lv+1)) - ((oldStats.getStat("ATK") - 2)*10)) * 5/50,0);
				newDef+=Math.max(((growth[1] * (lv+1)) - ((oldStats.getStat("DEF") - 2)*10)) * 5/50,0);
				newSpd+=Math.max(((growth[2] * (lv+1)) - ((oldStats.getStat("SPD") - 2)*10)) * 5/50,0);
				newGuts+=Math.max(((growth[3] * (lv+1)) - ((oldStats.getStat("GUTS") - 2)*10)) * 5/50,0);
				newVit+=Math.max(((growth[4] * (lv+1)) - ((oldStats.getStat("VIT") - 2)*10)) * 5/50,0);
				newIQ +=Math.max(((growth[5] * (lv+1)) - ((oldStats.getStat("IQ") - 2)*10)) * 5/50,0);
				newLuck+=Math.max(((growth[6] * (lv+1)) - ((oldStats.getStat("LUCK") - 2)*10)) * 5/50,0);
//				int newHP = (growth[0] * lv) - ((oldStats.getStat("ATK") - 2)*10) * 1/5;
//				int newPP = (growth[0] * lv) - ((oldStats.getStat("ATK") - 2)*10) * 1/5;
				newHP += 10;
				newPP += 10;
				leveledUp = true;
			}
			if (leveledUp) {	
				statIncreases = new EntityStats(0,newHP,newPP,newHP,newPP,newOff,newDef,newIQ,newSpd,newGuts,newLuck,newVit,0);
				
				levelupString += pm.getName() + " leveled up![PROMPTINPUT]";
				if (newOff != 0) 
				levelupString += "Offense went up by " + newOff + ".[PROMPTINPUT]";
				if (newDef != 0)
				levelupString += "Defense went up by " + newDef + ".[PROMPTINPUT]";
				if (newSpd != 0)
				levelupString += "Speed went up by " + newSpd + ".[PROMPTINPUT]";
				if (newGuts != 0)
				levelupString += "Guts went up by " + newGuts + ".[PROMPTINPUT]";
				if (newVit != 0)
				levelupString += "Vitality went up by " + newVit + ".[PROMPTINPUT]";
				if (newIQ != 0)
				levelupString += "IQ went up by " + newIQ + ".[PROMPTINPUT]";
				if (newLuck != 0)
				levelupString += "Luck went up by " + newLuck + ".[PROMPTINPUT]";
				if (newHP != 0)
				levelupString += "Max HP went up by " + newHP + ".[PROMPTINPUT]";
				if (newPP != 0)
				levelupString += "Max PP went up by " + newPP + ".[PROMPTINPUT]";
				if (newPSI != oldPSI) {
					levelupString += "[PLAYSFX_psilearn.wav]" + pm.getName() + " acquired a new PSI ability through battle.[PROMPTINPUT]";
					pm.setKnownPSI(newPSI);
				}
				
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
	
	public void doLevelupDisplay() {
		state.setBGM("levelup.ogg");
		getNext = false;
		prompt = createTextWindow(levelupString);
		levelupDisplay = false;
		readyToDisplay = true;
		kill = true;
	}
	
	public void endBattleRoutine() {
		AnimationMenu an = new AnimationMenu(state);
		an.createAnimMenu();
		state.getMenuStack().push(an);
	}
	
	@Override
	public void doDoneFadeOutAction() {
		// TODO Auto-generated method stub
		if (calculateAlivePartyMembers() == 0) {
//			state.getMenuStack().pop();
			state.getMenuStack().push(new GameOverMenu(state));
		} else {
			killBattleMenu();
			state.setAudioOverride(false);
			state.setBGM(state.getGameState().getMap().getBGM());
		}
	}
	
	public void killBattleMenu() {
		state.getMenuStack().pop();
		state.setShouldFadeIn();
		
		for (BattleEntity be : party) {
			be.applyBattleWear();
		}
		
		state.getGameState().restoreEnemyEntities();
		for (EnemyEntity ee : enemyEntities) {
			ee.setToRemove(true);
		}
		state.inBattle = false;
		state.setDrawAllMenus(false);
		state.getGameState().setInvincibleCounter();
		state.setNeedAddSavedMenu(state.getSavedMenu());
	}
	
	public int getIndex() {
		return indexMembers;
	}
	
	public void update(InputController input) {
		if (battleSceneEnd && getNext && !ended) {
			//create the You Won prompt, then the exp divying screen, then go through anyone's level ups
			createYouWin();
		}
//		if (mortalDamage && getNext) {
//			promptBak = prompt;
//			mortalDamage = false;
//			setPromptSecond(mortalDeathString);
//			prompt.setGetNext();
//		}
		
		if (displayRecExp && getNext) {
			showWinnings();
		}
		
		if (levelupDisplay && getNext) {
			doLevelupDisplay();
		}
		int numAliveParty = 0;
		if (kill && getNext) {
			//TODO create the game over routine and go here
			endBattleRoutine();
		}
		
		else if (!battleSceneEnd && !ended) {
			if (getNextPrompt || getResultText) {
				for (int i = 0; i < enemies.size(); i++) {
					BattleEntity e = enemies.get(i);
					if (checkIfPlayerCanAct(e)) {
						if (e.getBattleStats().getStat("CURHP") <= 0) {
							alertDeadEntity = true;
							deadEntities.add(e);
							break;
						}
					}
				}
				for (int i = 0; i < party.size(); i++) {
					BattleEntity e = party.get(i);
					PlayerStatusWindow psw = pswList.get(i);
					if (checkIfPlayerCanAct(e)) {
						if (psw.getHP() <= 0) {
							alertDeadEntity = true;
//							locked = true;
							deadEntities.add(e);
							e.setStatus(StatusConditions.DEAD.getIndex());
							break;
						}
					}
				}
				
				numAliveParty = calculateAlivePartyMembers();
				if (numAliveParty == 0) {
					partyIsDead = true;
					getNext = true;
					getNextPrompt = false;
					getResultText = false;
					state.addSavedMenu(false);
					state.clearSavedMenu();
				}
			}
			
			if (deadEntities.size() > 0 && !locked) {
				alertDeadEntity = false;
				deadEntity = deadEntities.remove(deadEntities.size()-1);
				if (getResultText) {
					getResultText = false;
					needResults = true;
				}
				if (getNextPrompt) {
					getNextPrompt = false;
					needNextPrompt = true;
				}
				setPromptDead();
			}
			
			if (alertDeadEntity && !locked) {
				alertDeadEntity = false;
				if (getResultText) {
					getResultText = false;
					needResults = true;
				}
				setPromptDead();
			}
			
			if (partyIsDead && getNext) {
				setPromptPartyDead();
				endBattleSceneGameOver = true;
				partyIsDead = false;
				getNext = false;
			}
			
			if (endBattleSceneGameOver && getNext) {
//				state.getMenuStack().pop();
//				state.getMenuStack().push(new GameOverMenu(state));
				endBattleRoutine();
				state.inBattle = false;
//				state.setDrawAllMenus(false);
				state.getGameState().setCanEncounter(true);
				state.getGameState().resetNumEntities();
			}
			
			if (enemies.size() == 0) {
				endBattleScene();
			}
			
			if (turnStack.isEmpty() && pollForActions && !locked && !battleSceneEnd) {
				battleActions.clear();
				pollForActions = false;
				needMenu = true;
				allActionsMade = false;
				indexMembers = 0;
				battleActions = new HashMap<BattleEntity,BattleAction>();
				for (BattleEntity be : allEntities) {
					if (((be.getState() & 16) != 16)) {
						turnStack.add(be);
					}
					be.setDefend(false);
				}
				sortTurnStackBySpeed();
			}
			
			if (needMenu && !locked) {
				windowStack.clear();
				partyMember = party.get(indexMembers++);
				while (!checkIfPlayerCanAct(partyMember)) {
					if (indexMembers < party.size()) {
//						indexMembers++;
						partyMember = party.get(indexMembers++);
					} else {
						needMenu = false;
						doneActionSelect = true;
//						actionMenu = null;
						return;
					}
				}
				createActionMenu();
//				addToMenuItems(actionMenu);
				needMenu = false;
			}
			
			if (!doneActionSelect && state.getMenuStack().peek() == this && actionMenu != null) {
				state.getMenuStack().push(actionMenu);
			}
			
			if (doneActionSelect && !locked) {
				state.getMenuStack().remove(actionMenu);
				doneActionSelect = false;
//				actionMenu = null;
				addBattleAction(partyMember,currentAction);
				if (indexMembers < party.size()) {
					needMenu = true;
				}
				else {
					indexMembers = 0;
					//generate battle actions for each enemy
					for (BattleEntity e : enemies) {
						BattleAction ba = new BattleAction(state);
						ba.setUser(e);
						//this should be randomly dictated by the enemy's possible actions as well, rather than always a bash
						ba.setAction("enemyaction");
						ba.setEnemyActionIndex(e.getRandomAction(),party,enemies);
						addBattleAction(e,ba);
					}
					allActionsMade = true;
				}
			}
			
			if (allActionsMade) {
				actionMenu = null;
			}
			
			if (!locked && allActionsMade && getNextPrompt && !alertDeadEntity && enemies.size() > 0) {
				if (currentBattleAction == null || currentBattleAction.isComplete()) {
					turnIsDone = false;
					getResultText = false;
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
				if (currentBattleAction != null) {
					setPromptFirst(currentBattleAction.getBattleActionString());
				}
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
			
			if (getResultText && !locked && !currentBattleAction.isComplete()) {
				getResultText = false;
				getNextPrompt = false;
				setPromptSecond(currentBattleAction.doAction());
			}
			
			for (int i = 0; i < party.size(); i++) {
				pswList.get(i).setTargetPosY(state.getMainWindow().getScreenHeight()-(64*5));
				pswList.get(i).updateStatus(party.get(i).getBattleStats().getStat("CURHP"),party.get(i).getBattleStats().getStat("CURPP"));
				if (i == indexMembers-1 && i >= 0) {
					pswList.get(i).setTargetPosY(state.getMainWindow().getScreenHeight()-(64*5)-32);
				}
				menuItems.addAll(pswList.get(i).getStatusIcons());
			}
		}
		
		if (readyToDisplay) {
			windowStack.clear();
			
			if (battleSceneEnd) {
				battleSceneEnd = false;
				addToMenuItems(youWon);
			}
			readyToDisplay = false;
			addMenuItem(prompt);
			prompt.setGetNext();
//			if (currentBattleAction != null) {
//				if (currentBattleAction.isComplete()) {
//					prompt.setPollForActionsOnExit();
//				}
//			}
		}
		
	}
	
	private void determineRandomAction(BattleEntity e) {
		// TODO Auto-generated method stub
		
	}

	private int calculateAlivePartyMembers() {
		int i =0;
		for (BattleEntity be : party) {
			if (checkIfPlayerCanAct(be)) {
				i++;
			}
		}
		return i;
	}
	
	private List<BattleEntity> getAliveMembers() {
		ArrayList<BattleEntity> bes = new ArrayList<BattleEntity>();
		for (BattleEntity be : party) {
			if (checkIfPlayerCanAct(be)) {
				bes.add(be);
			}
		}
		return bes;
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
            	 if (turnStack.get(j).getBattleStats().getStat("SPD") * adj1
                 		< turnStack.get(min_idx).getBattleStats().getStat("SPD") * adj2) {
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
			for (EnemyOption eo : eop.getEnemyOptions()) {
				if (eo.getEnemy() == deadEntity) {
					eop.removeEnemyOption(eo);
					break;
				}
			}
//			this.eop = new EnemyOptionPanel(state);
//			int j = 0;
//			for (BattleEntity en : enemies) {
//				eop.addEnemyOption(new EnemyOption((Enemy) en,state.getMainWindow().getScreenWidth()/2  - (en.getWidth())*enemies.size()/2 + (en.getWidth() + 32)*j,
//						state.getMainWindow().getScreenHeight()/2 -(((Enemy) en).getHeight()),state));
//				j++;
//			} 
			expPool += ((Enemy)deadEntity).getExpYield();
			moneyGained += ((Enemy) deadEntity).getMoneyYield();
		}
		else if (deadEntity instanceof PCBattleEntity) {
			//dont remove, just set the state of the pc to dead
//			deadEntity.setStatus(16);
			for (BattleEntity be : battleActions.keySet()) {
				BattleAction ba = battleActions.get(be);
				if (ba == null) {
					continue;
				}
				if (ba.getTarget() == null) {
//					SystemState.out.println("target is null");
					continue;
				}
				if (ba.getTarget().equals(deadEntity)) {
//					if (ba.getActor() instanceof PCBattleEntity) {
						for (BattleEntity pbe : party) {
							if (checkIfPlayerCanAct(pbe)) {
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
	
	public boolean checkIfPlayerCanAct(BattleEntity player) {
		boolean value = false;
		value = (player.getState() & 16) != 16 && (player.getState() & 8) != 8;
		return value;
	}
	
	public void popWindowStackAndRemoveMI() {
//		if (windowStack.size() != 0) {
////			needToRemove.add(i);
////			addToMenuItems(windowStack.remove(windowStack.size()-1));
//			state.getMenuStack().push(actionMenu);
//		} else {
			if (indexMembers > 1) {
				boolean atLeastOne = false;
				for (int x = indexMembers-2; x >= 0; x--) {
					if (checkIfPlayerCanAct(party.get(x))) {
						atLeastOne = true;
					}
				}
				if (!atLeastOne) {
					return;
				}
//				needToRemove.add(actionMenu);
				state.getMenuStack().pop();
				needMenu = true;
//				actionMenu = null;
				indexMembers-=2;
				while (!checkIfPlayerCanAct(party.get(indexMembers))) {
					indexMembers--;
				}
				if (indexMembers >= party.size()) {
					needMenu = false;
					getNextPrompt = true;
					doneActionSelect = true;
				}
			}
			
//		}
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

	public void setNeedMenu() {
		// TODO Auto-generated method stub
		needMenu = true;
	}

	public void setForceWaitForGetPrompt() {
		forceWaitForPrompt = true;
	}

	public boolean canGetNext() {
		// TODO Auto-generated method stub
		return getNext;
	}

}
