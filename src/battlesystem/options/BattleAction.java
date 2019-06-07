package battlesystem.options;

import java.io.IOException;
import java.util.ArrayList;

import battlesystem.BattleMenu;
import gamestate.BattleEntity;
import gamestate.Enemy;
import gamestate.PCBattleEntity;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import menu.Animation;
import menu.EnemyAction;
import menu.StartupNew;

public class BattleAction {
	private BattleEntity actor;
	private BattleEntity recipient;
	private String usedAction;
	private int damage;
	private String element;
	private boolean healing;
	private Item itemToUse;
	private boolean completed = false;
	private StartupNew state;
//	private BattleMenu bm = state.battleMenu;
	private boolean targetAll;
	private ArrayList<BattleEntity> targets;
	private int index = 0;
	private boolean actionDone;
	private int damageDealt;
	private boolean defending;
	private boolean isHealing = false;
	private String resultText;
	private boolean needDamageNums;
	private boolean doNothing;
	private EnemyAction enemyAction;
	private boolean continuous;
	private int continuousCounter = 0;
	private String flavorTextAttack;
	private boolean thunderWillHit = true;
	
	public boolean isComplete() {
		return completed;
	}
	
	public void createAnim(StartupNew state) throws IOException {
		BattleMenu bm = (BattleMenu) state.getMenuStack().peek();
		switch(usedAction) {
			case "psi" : //draw anim only if the psi has an animation
				Animation anim = null;
				if (actor instanceof Enemy) {
					anim = new Animation(state,"enemypsi",0,0,state.getMainWindow().getScreenWidth(),state.getMainWindow().getScreenHeight());
					anim.createAnimation();
				} else {
					if (anim == null) {
						//no animation for the psi
						bm.setGetResultText();
						break;
					}
					anim = ((PSIAttack) itemToUse).getAnimation();
					anim.createAnimation();
				}
				
//				if (anim.getTexture().equals("undef")) {
//					
//					if (!targetAll) {
////						bm.getCurrentActiveBattleAction().setComplete();
//					}
////					
//					break;
//				}
//				
				state.getMenuStack().peek().addToMenuItems(anim);
				state.setCurrentAnimation(anim.getTexture() + ".png");
//				state.createAtlas();
				anim.bindAnimToTwo();
				anim.updateAnim();
				break;
			default : 	bm.setGetResultText();
//				bm.getCurrentActiveBattleAction().setComplete();
				break;
		}
		
		bm.setGetAnimation(false);
	}
	
	public String doBash(BattleEntity user, BattleEntity target) {
		String result = "";
		int targetDef = target.getStats().getStat("DEF");
		int userOff = user.getStats().getStat("ATK");
		int targetSpd = target.getStats().getStat("SPD");
		int userSpd = user.getStats().getStat("SPD");
		double posneg = Math.random();
		double randAdjust = Math.random()*25/100;
		double critRand = Math.random();
		double dodgeRand = Math.random();
		int atkLevel = 2; //should vary with weapons
		int damage;
		
		//critrate
		int guts = user.getStats().getStat("GUTS");
		double critRate = Math.max(1/20,guts/500);
		if (critRand < critRate) {
			//set the SMAAAAAAASH text to display, make a big deal.
			result += "SMAAAAAAAASH!!![NEWLINE]";
			damage = Math.max(1,((4*userOff)-targetDef));
		} else {
			damage = Math.max(1,((atkLevel * userOff) - targetDef));
			if (posneg > 0.5) {
				randAdjust *= -1;
			}
			damage = (int) ((1+randAdjust) * damage);
		}
		double dodgeRate = (2*targetSpd - userSpd)/500;
		if (dodgeRand < dodgeRate) {
			return target.getName() + " dodged swiftly!";
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
								damageDealt = 777;
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
		index++;
		return result;
	}
	
	public boolean needDamageNums() {
		return needDamageNums;
	}
	
	public void setIndexOfUse(Item item) {
		this.itemToUse = item;
	}
	
	public String getBattleActionString() {
		String battleString = "";
		
		boolean statusLock = false;
		if ((actor.getState() & 256) == 256) {
			//sleep
			if (!battleString.equals("")) {
				battleString += "[PROMPTINPUT]";
			}
			actor.incrementSleepTimer();
			if (Math.random() * Math.min(0,actor.getSleepTimer()) < 0.33) {
				actor.removeStatus(256);
				battleString += actor.getName() + " woke up.";
			} 
			else {
				battleString += actor.getName() + " is fast asleep.";
				doNothing = true;
				statusLock = true;
			}
		}
		
		if ((actor.getState() & 4) == 4) {
			//asthma
			if (usedAction.equals("item")) {
				
			} else if (Math.random() < 0.60){
				if (!battleString.equals("")) {
					battleString += "[PROMPTINPUT]";
				}
				battleString += actor.getName() + " couldn't stop coughing.";
				doNothing = true;
				statusLock = true;
				if (Math.random() < 0.30) {
					battleString += "[PROMPTINPUT][PLAYSFX_healedbeta.wav]" + actor.getName() + " was cured of asthma.";
					actor.removeStatus(4);
				}
			}
		}
		
		if ((actor.getState() & 8) == 8) {
			//stone - dont even show their turn
			if (!battleString.equals("")) {
				battleString += "[PROMPTINPUT]";
			}
			battleString += actor.getName() + " is unable to move.";
			actor.takeDamage(16,0);
			doNothing = true;
			statusLock = true;
		}
		
		if (doNothing && statusLock) {
			return battleString;
		}
		
		if ((actor.getState() & 1) == 1) {
			//cold
			if (!battleString.equals("")) {
				battleString += "[PROMPTINPUT]";
			}
			battleString += actor.getName() + " sneezed and took damage.";
			actor.takeDamage(4,0);
		}
		
//		if ((actor.getState() & 2) == 2) {
//			//psn
//			if (!battleString.equals("")) {
//				battleString += "[PROMPTINPUT]";
//			}
//			battleString += actor.getName() + " took damage from poison.";
//			actor.takeDamage(16,0);
//		}
		
		
		//2 - blind
		//4 - asthma
		//8 - stone
		
		
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
			case "run":		battleString = "Tried to run... but failed!";
							break;
			case "psi":		
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
			case "defend":	battleString = actor.getName() + " took a defensive stance.";
							doNothing = true;
							break;
			case "enemyaction" : battleString = useEnemyAction(); 
							usedAction = "item";
							break;
		}
		
		return battleString;
	}
	
	public String useEnemyAction() {
		itemToUse = enemyAction.getItemRepresentation();
//		if (itemToUse.getActionType() == 30) {
//			usedAction = "bash";
//			doNothing = true;
//			flavorTextAttack = enemyAction.getText();
//			return getBattleActionString();
//		}
//		else if (itemToUse.getActionType() == 26) {
//			//is a bash
//			usedAction = "bash";
//			flavorTextAttack = enemyAction.getText();
//			return getBattleActionString();
//		} else if (itemToUse.getActionType() == 35) {
//			//is a psi
//			usedAction = "psi";
//			itemToUse = state.psi.get(enemyAction.getUseVariable());
//			return getBattleActionString();
//		}
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
	
	public BattleAction(StartupNew state) {
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
//		if (targetAll) {
//			this.targets = party;
//		} else {
//			this.recipient = 
//		}
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
			itemToUse = state.psi.get(enemyAction.getUseVariable());
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
