package gamestate.elements.items;

import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.UsableInBattle;
import gamestate.elements.UsableOutOfBattle;

public class ItemUsableInAndOutOfBattle extends Item implements UsableInBattle, UsableOutOfBattle {

	public ItemUsableInAndOutOfBattle(int id, String name, String desc, int ttype, int action) {
		super(id, name, desc, ttype,action);
		// TODO Auto-generated constructor stub
	}


}
