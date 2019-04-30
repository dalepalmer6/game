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
import gamestate.EntityStats;
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
		 allEntities.addAll(party);allEntities.addAll(enemies);
		
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
//		((BattleTextWindow)prompt).setCompleteOnExit();
//		if (!s.equals("done")) {
			prompt = new BattleTextWindow(s,state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
			((BattleTextWindow)prompt).setGetResultsOnExit();
//		}
		if (turnIsDone) {
			turnIsDone = false;
			((BattleTextWindow)prompt).setPollForActionsOnExit();
			getCurrentActiveBattleAction().setComplete();
			getResultText = false;
		}
		
		readyToDisplay = true;
	}
	
	public void setPromptDead() {
		prompt = new BattleTextWindow(deadEntity.getName() + " became tame.",state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
		((BattleTextWindow)prompt).setPollForActionsOnExit();
		prompt.onCompleteKillEntity();
		getResultText = false;
		readyToDisplay = true;
		locked = true;
	}
	
	public void setTurnIsDone() {
		turnIsDone = true;
	}
	
	public boolean turnStackIsEmpty() {
		return turnStack.isEmpty();
	}
	
	public void update() {
		if (battleSceneEnd && getNext && !ended) {
			//create the You Won prompt, then the exp divying screen, then go through anyone's level ups
			state.setSFX("eb_fanfare.wav");
			state.playSFX();
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
			prompt = new BattleTextWindow(party.get(0).getName() + " and co. received " + awardEXP + " experience points.",state.getMainWindow().getScreenWidth()/2 - (20/2)*32,100,20,2,state);
//			prompt.setPollForActionsOnExit();
			for (PartyMember pm : partyMembers) {
				pm.addExp(awardEXP);
				//add a check to see if someone is leveling up, if so, then we need to display the level up prompts
			}
			readyToDisplay = true;
			displayRecExp = false;
			kill = true;
		}
		
		if (kill && getNext) {
			state.getMenuStack().pop();
			for (EnemyEntity ee : enemyEntities) {
				ee.setToRemove(true);
			}
			
		}
		
		else if (!battleSceneEnd && !ended){
			for (int i = 0; i < allEntities.size(); i++) {
				BattleEntity e = allEntities.get(i);
				if (e.getStats().getStat("CURHP") <= 0) {
					alertDeadEntity = true;
					locked = true;
					deadEntity = e;
					break;
				}
			}
			
			if (enemies.size() == 0) {
				endBattleScene();
			}
			
			if (turnStack.isEmpty() && pollForActions && !locked) {
				battleActions.clear();
				pollForActions = false;
				needMenu = true;
				indexMembers = 0;
				battleActions = new HashMap<BattleEntity,BattleAction>();
				for (BattleEntity be : allEntities) {
					turnStack.add(be);
				}
			}
			
			if (needMenu) {
				windowStack.clear();
				partyMember = party.get(indexMembers);
				addToMenuItems(actionMenu);
				needMenu = false;
				indexMembers++;
			}
			
			if (doneActionSelect) {
				setToRemove(actionMenu);
				doneActionSelect = false;
				addBattleAction(partyMember,currentAction);
				if (indexMembers < party.size()) {
					needMenu = true;
				}
				else {
					//generate battle actions for each enemy
					for (BattleEntity e : enemies) {
						//create stubs
						BattleAction ba = new BattleAction(state);
						ba.setUser(e);
						ba.setTarget(party.get(0));
						ba.setAction("bash");
						addBattleAction(e,ba);
					}
				}
			}
			
//			if (getResultText) {
				
//			}
			
			if (battleActions.size() == enemies.size() + party.size() && getNextPrompt && !alertDeadEntity && enemies.size() > 0) {
				if (currentBattleAction == null || currentBattleAction.isComplete()) {
					BattleEntity be = turnStack.remove(turnStack.size()-1);
					currentBattleAction = battleActions.get(be);
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
			
			
			if (getResultText && !alertDeadEntity) {
				getResultText = false;
				setPromptSecond(currentBattleAction.doAction());
			}
			
			if (getNextPrompt && alertDeadEntity) {
				alertDeadEntity = false;
				getNextPrompt = false;
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
//			if (!prompt.getText().equals("done")) {
				addToMenuItems(prompt);
//			}
			readyToDisplay = false;
			prompt.setGetNext();
		}
		
	}
	
	public void killDeadEntity() {
		locked = false;
		battleActions.remove(deadEntity);
		party.remove(deadEntity);
		enemies.remove(deadEntity);
		allEntities.remove(deadEntity);
		
		this.eop = new EnemyOptionPanel(state);
		int j = 0;
		for (BattleEntity en : enemies) {
			eop.addEnemyOption(new EnemyOption((Enemy) en,state.getMainWindow().getScreenWidth()/2  - (en.getWidth())*enemies.size()/2 + (en.getWidth() + 32)*j,
					state.getMainWindow().getScreenHeight()/2 -(((Enemy) en).getHeight()),state));
			j++;
		} 
		if (deadEntity instanceof Enemy) {
			expPool += ((Enemy)deadEntity).getExpYield();
		}
		deadEntity = null;
		alertDeadEntity = false;
	}
	
	private void endBattleScene() {
		// TODO Auto-generated method stub
		battleSceneEnd = true;
		getNext = true;
		doOnce = true;
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
