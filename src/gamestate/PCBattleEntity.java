package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;
import gamestate.elements.items.Item;
import menu.StartupNew;

public class PCBattleEntity extends BattleEntity {
	private String id; //use to get the sprite to draw on top of the window
	private ArrayList<Item> items;
	
	public PCBattleEntity(String texture, String name, String id, EntityStats stats, StartupNew state,int status) {
		super(texture,name,stats,state);
		this.id = id;
		this.status = status;
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
	
}
