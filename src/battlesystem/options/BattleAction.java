package battlesystem.options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import battlesystem.BattleEntity;
import battlesystem.Enemy;
import battlesystem.EnemyAction;
import battlesystem.PCBattleEntity;
import battlesystem.menu.BattleMenu;
import battlesystem.menu.DamageMenuItem;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import menu.animation.Animation;
import system.MotherSystemState;
import system.SystemState;
import system.data.StatusConditions;

public class BattleAction {
	private BattleEntity actor;
	private BattleEntity recipient;
	private String usedAction;
	private int damage;
	private String element;
	private boolean healing;
	private Item itemToUse;
	private boolean completed = false;
	private SystemState state;
//	private BattleMenu bm = state.battleMenu;
	private boolean targetAll;
	private ArrayList<BattleEntity> targets;
	private int index = 0;
	private boolean actionDone;
	private int damageDealt;
//	private boolean defending;
	private boolean isHealing = false;
	private String resultText;
	private boolean needDamageNums;
	private boolean doNothing;
	private EnemyAction enemyAction;
	private boolean continuous;
	private int continuousCounter = 0;
	private String flavorTextAttack;
//	private boolean thunderWillHit = true;
	private boolean isSmash = false;
	private boolean damageOverTime;
	
	public boolean doingNothing() {
		return doNothing;
	}
	
	public boolean isSmash() {
		return isSmash;
	}
	
	public boolean isComplete() {
		return completed;
	}
	
	public void createAnim(SystemState state) throws IOException {
		BattleMenu bm = state.battleMenu;
		switch(usedAction) {
			case "psi" : //draw anim only if the psi has an animation
				Animation anim = ((PSIAttack)itemToUse).getAnimation();
				if (actor instanceof Enemy) {
					anim = new Animation(state,"enemypsi",0,0,state.getMainWindow().getScreenWidth(),state.getMainWindow().getScreenHeight());
//					anim.createAnimation();
				} else {
					if (anim == null) {
						//no animation for the psi
						bm.setGetResultText();
						break;
					}
					anim = ((PSIAttack) itemToUse).getAnimation();
					anim.createAnimation();
				}
				
				state.getMenuStack().peek().addToMenuItems(anim);
//				state.setCurrentAnimation(anim.getTexture() + ".png");
				anim.bindAnimToTwo();
				anim.setSFXPath("psi/freezeg.wav");
				anim.updateAnim();
				break;
			default : 	bm.setGetResultText();
				break;
		}
		
		bm.setGetAnimation(false);
	}
	
	public String doBash(BattleEntity user, BattleEntity target) {
		String result = "";
		int targetDef = target.getBattleStats().getStat("DEF");
		int userOff = user.getBattleStats().getStat("ATK");
		int targetSpd = target.getBattleStats().getStat("SPD");
		int userSpd = user.getBattleStats().getStat("SPD");
		double posneg = Math.random();
		double randAdjust = Math.random()*25/100;
		double critRand = Math.random();
		double dodgeRand = Math.random();
		int atkLevel = 2; //should vary with weapons
		int damage;
		
		//critrate
		double guts = user.getBattleStats().getStat("GUTS");
		double critRate = Math.max(1f/20f,guts/500);
		if (critRand < critRate) {
			//set the SMAAAAAAASH text to display, use graphic/TMI, make a big deal.
//			result += "SMAAAAAAAASH!!![NEWLINE]";
			isSmash = true;
			damage = Math.max(1,((4*userOff)-targetDef));
		} else {
			damage = Math.max(1,((atkLevel * userOff) - targetDef));
			if (posneg > 0.5) {
				randAdjust *= -1;
			}
			damage = (int) ((1+randAdjust) * damage);
		}
		double dodgeRate = ((double) (2*targetSpd - userSpd))/500d;
		if (dodgeRand < dodgeRate) {
			state.setSFX("miss.wav");
			return String.format("%s dodged swiftly!",target.getName());
		}
		damage = Math.max(1,damage);
		
		if (target.getShieldType() == 1) {
			damage/=2;
			if (target.decreaseShieldCharge()) {
				resultText = target.getName() + "'s PSI Shield absorbed some of the damage.";
			} else {
				resultText = target.getName() + "'s PSI Shield dissipated.";
			}
		} else if (target.getShieldType() == 3) {
			if (target.decreaseShieldCharge()) {
				resultText = target.getName() + "'s Power Shield reflected the damage.";
			} else {
				resultText = target.getName() + "'s Power Shield dissipated.";
			}
			BattleEntity temp = user;
			this.actor = target;
			this.recipient = temp;
			user = target;
			target = temp;
		}
		damage = target.takeDamage(damage,0);
		result += target.getName() + " suffered damage of " + damage + "!";
		
		
		setDamageDealt( damage);
		return result;
	}
	
	public int getDamageDealt() {
		return damageDealt;
	}
	
	public boolean isActionDone() {
		return actionDone;
	}
	
	public void indexDown() {
		index--;
	}
	
	public String doAction() {
		damageDealt = 0;
		String result = "";
		if (doNothing) {
			state.battleMenu.setTurnIsDone();
			completed = true;
			return "donothing";
		}
		if (targets.size() == 0) {
			return result;
		}
		if (!targets.contains(recipient)) {
			recipient = targets.get(0);
		} 
		switch(usedAction) {
			case "bash" : 	
							if (targetAll) {
								if (index >= targets.size()) {
									state.battleMenu.setGetNextPrompt();
									state.battleMenu.setTurnIsDone();
									state.battleMenu.getCurrentActiveBattleAction().setComplete();
									break;
								} 
								while ((targets.get(index).getState() & 16) == 16) {
									index++;
									if (index >= targets.size()) {
										state.battleMenu.setGetNextPrompt();
										state.battleMenu.setTurnIsDone();
										state.battleMenu.getCurrentActiveBattleAction().setComplete();
										return "";
									}
								}
								doBash(actor,targets.get(index));
							} 
							else {
								if (actor instanceof PCBattleEntity) {
									state.setSFX("bash.wav");
									state.playSFX();
								} else {
									state.setSFX("dmg.wav");
									state.playSFX();
								}
								
								doBash(actor,recipient);
							}
							continuousCounter--;
							if (continuous && continuousCounter > 0) {
								
							} else if (!targetAll){
								state.battleMenu.setTurnIsDone();
							}
							if (resultText != null) {
								result = resultText;
							}
								
							needDamageNums = true;
//							state.battleMenu.getCurrentActiveBattleAction().setComplete();
							break;
			case "psi" : if (itemToUse.getActionType() == 0 || itemToUse.getActionType() == 1) {
							isHealing = true;
						}
						if (itemToUse.getActionType() == 10) {
							double prob = Math.random();
							double chance = (double) targets.size() / 4;
							continuousCounter--;
							boolean needToBreak = false;
							if (continuous && continuousCounter >= 0) {
								
							} else {
								state.battleMenu.setGetNextPrompt();
								state.battleMenu.setTurnIsDone();
								state.battleMenu.getCurrentActiveBattleAction().setComplete();
								result = "";
								needToBreak = true;
							}
							
							if (prob < chance) {
								int index = (int) (Math.random() * (double) targets.size());
								setDamageDealt(itemToUse.useInBattle(actor,targets.get(index)));
							} else {
								damageDealt = -1; // a miss
								result = "";
							}
							if (needToBreak) {
								break;
							}
//							
						}
						else if (targetAll) {
							if (index >= targets.size()) {
								state.battleMenu.setGetNextPrompt();
								state.battleMenu.setTurnIsDone();
								state.battleMenu.getCurrentActiveBattleAction().setComplete();
								break;
							} 
							while ((targets.get(index).getState() & 16) == 16) {
								index++;
								if (index >= targets.size()) {
									state.battleMenu.setGetNextPrompt();
									state.battleMenu.setTurnIsDone();
									state.battleMenu.getCurrentActiveBattleAction().setComplete();
									return "";
								}
							}
							
							BattleEntity target = null;
							target = targets.get(index);
							setDamageDealt(itemToUse.useInBattle(actor,target));
						} else {
							setDamageDealt(itemToUse.useInBattle(actor,recipient));
							continuousCounter--;
							if (continuous && continuousCounter > 0) {
								
							} else {
								state.battleMenu.setTurnIsDone();
							}
						}
						result = itemToUse.getUsedString();
						if (result == null) {
							result = "";
						}
						if (damageDealt != 0) {
							needDamageNums = true;
						}
						break;
						
			case "item" : 	if (itemToUse.getActionType() == 0 || itemToUse.getActionType() == 1) {
								isHealing = true;
							} 
							itemToUse.consume(actor);
							if (targetAll) {
								if (index >= targets.size()) {
									state.battleMenu.setGetNextPrompt();
									state.battleMenu.setTurnIsDone();
									state.battleMenu.getCurrentActiveBattleAction().setComplete();
									break;
								} 
								while ((targets.get(index).getState() & 16) == 16) {
									index++;
									if (index >= targets.size()) {
										state.battleMenu.setGetNextPrompt();
										state.battleMenu.setTurnIsDone();
										state.battleMenu.getCurrentActiveBattleAction().setComplete();
										return "";
									}
								}
								BattleEntity target = null;
								target = targets.get(index);
								setDamageDealt(itemToUse.useInBattle(actor,target));
							} else {
								setDamageDealt(itemToUse.useInBattle(actor,recipient));
								state.battleMenu.setTurnIsDone();
							}
							if (damageDealt != 0) {
								needDamageNums = true;
							}
							result = itemToUse.getUsedString();
							break;
			case "defend" : break;
		}
		
		BattleEntity targ = recipient;
		if (targ instanceof PCBattleEntity) {
			
			int index = state.battleMenu.getPartyMembers().indexOf(targ);
			if (state.battleMenu.getPSWs().get(index).getTargetHP() - getDamageDealt() <= 0) {
				state.setSFX("mortaldmg.wav");
				result += String.format("%s %s!",targ.getName(), "took mortal damage");
			};
		}
		
		index++;
		return result;
	}
	
	public boolean needDamageNums() {
		return needDamageNums;
	}
	
	public void setIndexOfUse(Item item) {
		this.itemToUse = item;
	}
	
	public boolean isDamageOverTime() {
		return damageOverTime;
	}
	
	public String getBattleActionString() {
		String battleString = "";
		
		boolean statusLock = false;
		
		List<StatusConditions> conditions = StatusConditions.getAfflictedStatus(actor.getState());
		
		if (conditions.contains(StatusConditions.SLEEP)) {
			//SLEEP
			if (!battleString.equals("")) {
				battleString += "[PROMPTINPUT]";
			}
			
			if (Math.random() <= 0.33 * actor.getSleepTimer()) {
				actor.removeStatus(StatusConditions.SLEEP.getIndex());
				battleString += actor.getName() + " woke up.";
				actor.resetSleepTimer();
			}
			else {
				battleString += String.format(StatusConditions.SLEEP.getActionString(),actor.getName());
				actor.incrementSleepTimer();
				doNothing = !StatusConditions.SLEEP.canMove();
				usedAction = "fail";
				statusLock = true;
			}
			
		}
		
		if (conditions.contains(StatusConditions.ASTHMA)) {
			//asthma
			if (usedAction.equals("item")) {
				
			} else if (Math.random() < 0.60){
				if (!battleString.equals("")) {
					battleString += "[PROMPTINPUT]";
				}
				battleString += String.format(StatusConditions.ASTHMA.getActionString(),actor.getName());
				doNothing = !StatusConditions.ASTHMA.canMove();
				usedAction = "fail";
				statusLock = true;
				if (Math.random() < 0.30) {
					battleString += "[PROMPTINPUT][PLAYSFX_healedbeta.wav]" + actor.getName() + " recovered from the asthma attack.";
					actor.removeStatus(4);
				}
			}
		}
		
		if (conditions.contains(StatusConditions.STONE)) {
			if (!battleString.equals("")) {
				battleString += "[PROMPTINPUT]";
			}
			battleString += String.format(StatusConditions.STONE.getActionString(),actor.getName());
			doNothing = !StatusConditions.STONE.canMove();
			usedAction = "fail";
			statusLock = true;
		}
		
		if (doNothing && statusLock) {
			return battleString;
		}
		
		if (conditions.contains(StatusConditions.COLD)) {
			if (!battleString.equals("")) {
				battleString += "[PROMPTINPUT]";
			}
			double dmg = 5 + 5 - Math.random()*10;
			battleString += String.format(StatusConditions.COLD.getActionString(),actor.getName());
			actor.takeDamage((int) dmg,16);
			createDOTMenuItem((int)dmg,actor);
		}
		
		if (conditions.contains(StatusConditions.POISON)) {
			if (!battleString.equals("")) {
				battleString += "[PROMPTINPUT]";
			}
			double dmg = 20 + 4 - Math.random()*8;
			battleString += String.format(StatusConditions.POISON.getActionString(),actor.getName());
			actor.takeDamage((int) dmg,16);
			createDOTMenuItem((int)dmg,actor);
		}
		
		if (!battleString.equals("")) {
			battleString += "[PROMPTINPUT]";
		}
		
		switch (usedAction) {
			case "bash" : 	
							if (actor instanceof PCBattleEntity) {
								state.setSFX("playeratk.wav");
								state.playSFX();
							} else {
								state.setSFX("enemyatk.wav");
								state.playSFX();
							}
							battleString += actor.getName();
							if (flavorTextAttack != null) {
								battleString += " " + flavorTextAttack + "!";
							} else {
								battleString += " attacks!";//use flavor text
							}
							break;
			case "run":		battleString = "Tried to run... but failed!"; // needs logic, is broken.
							break;
			case "psi":		
							itemToUse.consume(actor);
							if (actor instanceof PCBattleEntity) {
								state.setSFX("psicast.wav");
								state.playSFX();
							} else {
								state.setSFX("enemypsicast.wav");
								state.playSFX();
							}
							battleString += actor.getName() + " tried " + itemToUse.getName() + ".";
							if (itemToUse.getActionType() == 10) {
								//psi thunder
								continuous = true;
								switch(itemToUse.getId()) {
									case 15: continuousCounter = 1; break;
									case 16: continuousCounter = 2; break;
									case 17: continuousCounter = 3; break;
									case 18: continuousCounter = 4; break;
									case 19: continuousCounter = 5; break;
								}
//								continuousCounter = 3;
							}
							break;
			case "item":	battleString += actor.getName() + " pulled out ";
							if (itemToUse.getName().toLowerCase().startsWith("a") ||
									itemToUse.getName().toLowerCase().startsWith("e") ||
									itemToUse.getName().toLowerCase().startsWith("i") ||
									itemToUse.getName().toLowerCase().startsWith("o") ||
									itemToUse.getName().toLowerCase().startsWith("u")) {
								battleString += "an ";
							} else {
								battleString += "a ";
							}
							battleString += itemToUse.getName() + " and " + "used " + "it.";
							battleString += ".[NEWLINE]";
//							battleString += itemToUse.useInBattle(actor,recipient);
							break;
			case "defend":	battleString += actor.getName() + " took a defensive stance.";
							doNothing = true;
							break;
			case "enemyaction" : battleString += useEnemyAction(); 
							usedAction = "item";
							break;
		}
		
		return battleString;
	}
	
	public DamageMenuItem createDOTMenuItem(int dmg, BattleEntity target) {
		DamageMenuItem mi = state.battleMenu.getDamageMenuItem(dmg,target);
		mi.setDamageOverTime(true);
		state.battleMenu.addMenuItem(mi);
		return mi;
	}
	
	public String useEnemyAction() {
		itemToUse = enemyAction.getItemRepresentation();
		return actor.getName() + " " + enemyAction.getText() + ".";
	}
	
	public void setUser(BattleEntity e) {
		this.actor = e;
	}
	
	public void setTarget(BattleEntity e) {
		this.recipient = e;
	}
	
	/*THIS WILL CAUSE ISSUES WITH BACK. STORE THE ACTION AS A STACK POTENTIALLY*/
	public void setAction(String act) {
		this.usedAction = act;
		if (act.equals("defend")) {
			actor.setDefend(true);
		} else {
			actor.setDefend(false);
		}
	}
	
	public void appendAction(String actExtend) {
		usedAction += actExtend;
	}
	
	public BattleAction(SystemState state) {
		this.state = state;
	}
	
	public void setDamage(int d) {
		damage = d;
	}
	
	public void setDamageDealt(int d) {
		if (damageDealt < 0) {
			isHealing = true;
		}
		damageDealt = d;
	}
	
	public void setElement(String elem) {
		this.element = elem;
	}
	
	public void setHealing(boolean b) {
		this.healing = b;
	}
	
	public BattleEntity getTarget() {
		return recipient;
	}
	
	public BattleEntity getActor() {
		return actor;
	}
	
	public int getDamage() {
		if (healing) {
			damage *= -1;
		}
		return damage;
	}
	
	public String getElement() {
		return element;
	}

	public void setComplete() {
		// TODO Auto-generated method stub
		completed = true;
	}

	public void setTargetsForEnemyAction(ArrayList<BattleEntity> party, ArrayList<BattleEntity> enemies) {
		int val = (int) Math.floor(Math.random() * party.size());
		while ((party.get(val).getState() & 16) == 16) {
			val = (int) Math.floor(Math.random() * party.size());
		}
		int valEnemy = (int) Math.floor(Math.random() * enemies.size());
		if (enemyAction.getTarget() == 0) {
			//on one enemy
			targetAll = false;
			targets=new ArrayList<BattleEntity>();
			targets.add(enemies.get(valEnemy));
			recipient = this.actor;
		}
		if (enemyAction.getTarget() == 1) {
			//on all enemies
			targets = enemies;
			targetAll = true;
		}
		if (enemyAction.getTarget() == 2) {
			//on one party mem
			recipient = party.get(0);
			targets=new ArrayList<BattleEntity>();
			targets.add(party.get(val));
			targetAll = false;
		}
		if (enemyAction.getTarget() == 3) {
			//on all party
			targetAll = true;
			targets = party;
		}
	}
	
	public void setTargets(ArrayList<BattleEntity> targets, BattleEntity target, boolean all) {
		// TODO Auto-generated method stub
		targetAll = all;
		this.recipient = target;
		this.targets = targets;
	}

	public boolean isHealing() {
		// TODO Auto-generated method stub
		return isHealing;
	}

	public void setEnemyActionIndex(EnemyAction e, ArrayList<BattleEntity> players, ArrayList<BattleEntity> enemies) {
		// TODO Auto-generated method stub
		enemyAction = e;
		/*
		 * Converts an enemy action to the corresponding battle action.
		 * */
		if (enemyAction.getAction() == 30) {
			usedAction = "bash";
			continuous = false;
			continuousCounter = 0;
			doNothing = true;
			flavorTextAttack = enemyAction.getText();
		}
		else if (enemyAction.getAction() == 26) {
			//is a bash on a single
			usedAction = "bash";
			continuous = false;
			continuousCounter = 0;
			flavorTextAttack = enemyAction.getText();
		} else if (enemyAction.getAction() == 35) {
			//is a psi
			usedAction = "psi";
			itemToUse = ((MotherSystemState) state).psi.get(enemyAction.getUseVariable());
			enemyAction.setTarget(itemToUse.getTargetType());
		} 
		else if (enemyAction.getAction() == 36) {
			//is a bash, continuous
			usedAction = "bash";
			continuous = true;
			continuousCounter = enemyAction.getUseVariable();
			flavorTextAttack = enemyAction.getText();
		}
		setTargetsForEnemyAction(players,enemies);
	}
	
	public boolean isContinuous() {
		return continuous;
	}

	public boolean needAnim() {
		// TODO Auto-generated method stub
		return continuousCounter > 0;
	}
	
}
