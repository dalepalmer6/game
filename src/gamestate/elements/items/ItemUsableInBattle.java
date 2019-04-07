package gamestate.elements.items;

import gamestate.BattleEntity;
import gamestate.elements.UsableInBattle;

public class ItemUsableInBattle extends Item implements UsableInBattle {
	
	public ItemUsableInBattle(int id, String name, String desc, int targetType, int action) {
		super(id, name, desc, targetType,action);
	}

}
