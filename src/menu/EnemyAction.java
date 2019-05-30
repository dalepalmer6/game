package menu;

import gamestate.elements.items.Item;

public class EnemyAction {
	private String text;
	private int action;
	private int useVariable;
	private int target;
	
	public EnemyAction(String t, int a, int u, int targ) {
		text = t;
		action = a;
		useVariable = u;
		target=targ;
	}
	
	public void setTarget(int t) {
		target = t;
	}
	
	//public Item(int id, String name, String desc, int ttype,int action, int equippable, String participle, int value, int useVar) {
	public Item getItemRepresentation() {
		return new Item(0,"","",target,action,0,"",0,useVariable);
	}
	
	public String getText() {
		return text;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getUseVariable() {
		return useVariable;
	}
	
	public int getTarget() {
		return target;
	}
	
	
}
