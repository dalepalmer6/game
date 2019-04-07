package battlesystem.options;

import gamestate.BattleEntity;
import gamestate.elements.items.Item;
import gamestate.elements.items.ItemUsableInBattle;
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
	
	public boolean isComplete() {
		return completed;
	}
	
	public String doAction(StartupNew state) {
		String result = "";
		switch(usedAction) {
			case "bash" : 	result += "Bash result text";
							break;
			case "psi" : //draw anim
				Animation anim = new Animation(state,((PSIAttack)itemToUse).getAnimation() +".png");
				anim.createAnimation();
				result += itemToUse.useInBattle(actor,recipient); 
		}
		completed = true;
		return result;
	}
	
	public void setIndexOfUse(Item item) {
		this.itemToUse = item;
	}
	
	public String getBattleActionString() {
		String battleString = "";
		
		switch (usedAction) {
			case "bash" : 	battleString += actor.getName();
							battleString += " used " + usedAction + " on " + recipient.getName();
							battleString += " and ";
							if (healing) {
								battleString += " healed ";
							} else {
								battleString += " dealt ";
							}
							battleString += damage + " HP. ";
							completed = true;
							break;
			case "run":		battleString = "Tried to run... but failed!";
							break;
			case "psi":		battleString += actor.getName() + " tried " + itemToUse.getName() + ".[NEWLINE]";
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
	
	public BattleAction() {}
	
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
}
