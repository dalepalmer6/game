package battlesystem;

import java.util.ArrayList;

import battlesystem.menu.BattleMenu;
import gamestate.EntityStats;
import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.windows.TextWindowWithPrompt;
import system.SystemState;

public class PCBattleEntity extends BattleEntity {
	private String id; //use to get the sprite to draw on top of the window
	private ArrayList<Item> items;
	private PartyMember partyMember;
	
	public void consumePP(int usedPP) {
		partyMember.consumePP(usedPP);
	}
	
	public PCBattleEntity(String texture, String name, String id, EntityStats stats, SystemState state,int status) {
		super(texture,name,stats,state);
		this.id = id;
	}
	
	public void setPartyMember(PartyMember pm) {
		this.partyMember = pm;
	}
	
	public String getId() {
		return id;
	}
	
	public TextWindowWithPrompt performBattleAction(BattleMenu bm, ArrayList<PCBattleEntity> party, ArrayList<Enemy> enemies) {
		return null;
		// TODO Auto-generated method stub
		
	}

	public int takeDamage(int damage, int element) {
		systemState.setShakeVariables(damage,true);
		return super.takeDamage(damage,element);
	}

	public void setItems(ArrayList<Item> items) {
		// TODO Auto-generated method stub
		this.items = items;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public void consumeItem(Item i) {
		partyMember.consumeItem(i);
	}
	
}
