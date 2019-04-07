package gamestate;

import java.util.ArrayList;

public class PartyMember {
	private String name;
	private EntityStats stats;
	private Entity entity;
	private ArrayList<String> PSI = new ArrayList<String>();
	private ArrayList<Integer> items = new ArrayList<Integer>();
	
	public ArrayList<String> getPSI() {
		return PSI;
	}
	
	public ArrayList<Integer> getItems() {
		return items;
	}
	
	public void addItemToBag(int i) {
		items.add(i);
	}
	
	public PartyMember(String name) {
		this.name = name;
		this.stats = new EntityStats(32,10,1,2,3,4,5,6);
		this.PSI.add("PSI Lifeup");
		this.items.add(1);
	}
	
	public void loadStats() {
		//load from a saved file
	}
	
	public PCBattleEntity createBattleEntity() {
		return new PCBattleEntity("",name,stats);
	}
}
