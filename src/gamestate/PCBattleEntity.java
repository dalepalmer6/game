package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;

public class PCBattleEntity extends BattleEntity {
	public PCBattleEntity(String texture, String name,EntityStats stats) {
		super(texture,name,stats);
	}
	
	public TextWindowWithPrompt performBattleAction(BattleMenu bm, ArrayList<PCBattleEntity> party, ArrayList<Enemy> enemies) {
		return null;
		// TODO Auto-generated method stub
		
	}
}
