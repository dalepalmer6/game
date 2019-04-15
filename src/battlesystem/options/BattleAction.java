package battlesystem.options;

import java.io.IOException;
import java.util.ArrayList;

import battlesystem.BattleMenu;
import gamestate.BattleEntity;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import menu.Animation;
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
	
	public boolean isComplete() {
		return completed;
	}
	
	public void createAnim(StartupNew state) throws IOException {
		BattleMenu bm = (BattleMenu) state.getMenuStack().peek();
		switch(usedAction) {
			case "psi" : //draw anim only if the psi has an animation
				Animation anim = ((PSIAttack) itemToUse).getAnimation();
				if (anim.getTexture().equals("undef")) {
					bm.setGetResultText();
					if (!targetAll) {
						bm.getCurrentActiveBattleAction().setComplete();
					}
//					
					break;
				}
				state.getMenuStack().peek().addToMenuItems(anim);
				state.setCurrentAnimation(anim.getTexture() + ".png");
				state.createAtlas();
				anim.updateAnim();
				break;
			default : 	bm.setGetResultText();
				bm.getCurrentActiveBattleAction().setComplete();
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
		result += target.getName() + " suffered damage of " + damage + "!";
		target.takeDamage(damage);
		return result;
	}
	
	public boolean isActionDone() {
		return actionDone;
	}
	
	public String doAction() {
		String result = "";
		index++;
		switch(usedAction) {
			case "bash" : 	state.setSFX("bash.wav");
							state.playSFX();
							result += doBash(actor,recipient);
							state.battleMenu.setTurnIsDone();
							break;
			case "psi" : if (targetAll) {
//							state.setSFX("bash.wav");
//							state.playSFX();
							BattleEntity target = null;
								target = targets.get(index-1);
								result += itemToUse.useInBattle(actor,target);
//								
							if (index == targets.size()) {
								state.battleMenu.setTurnIsDone();
								state.battleMenu.getCurrentActiveBattleAction().setComplete();
							}
							
						} else {
							result += itemToUse.useInBattle(actor,recipient); 
							state.battleMenu.setTurnIsDone();
						}
						break;
						
			case "item" : 	state.setSFX("eat.wav");
							state.playSFX();
							result += itemToUse.useInBattle(actor,recipient);
							break;
		}
		return result;
	}
	
	public void setIndexOfUse(Item item) {
		this.itemToUse = item;
	}
	
	public String getBattleActionString() {
		String battleString = "";
		
		switch (usedAction) {
			case "bash" : 	state.setSFX("attack1.wav");
							state.playSFX();
							battleString += actor.getName() + " attacks!";//use flavor text
							break;
			case "run":		battleString = "Tried to run... but failed!";
							break;
			case "psi":		state.setSFX("psi.wav");
							state.playSFX();
							battleString += actor.getName() + " tried " + itemToUse.getName() + ".[NEWLINE]";
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
							battleString += itemToUse.getName() + " and " + "used " + "it on " + recipient.getName();
							battleString += ".[NEWLINE]";
							battleString += itemToUse.useInBattle(actor,recipient);
							break;
			case "stat":	battleString = "Even if there was a status menu, do you really think it would do you much good?";
							break;
		}
		
		return battleString;
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

	public void setTarget(ArrayList<BattleEntity> targets, boolean all) {
		// TODO Auto-generated method stub
		targetAll = true;
		this.targets = targets;
	}
}
