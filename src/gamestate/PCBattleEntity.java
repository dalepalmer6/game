package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;
import menu.StartupNew;

public class PCBattleEntity extends BattleEntity {
	private String id; //use to get the sprite to draw on top of the window
	
	public PCBattleEntity(String texture, String name, String id, EntityStats stats, StartupNew state) {
		super(texture,name,stats,state);
		this.id = id;
		this.state = "normal";
	}
	
	public String getId() {
		return id;
	}
	
	public TextWindowWithPrompt performBattleAction(BattleMenu bm, ArrayList<PCBattleEntity> party, ArrayList<Enemy> enemies) {
		return null;
		// TODO Auto-generated method stub
		
	}

	public void takeDamage(int damage) {
		systemState.setShakeVariables(damage,true);
		super.takeDamage(damage);
	}
	
}
