package gamestate.elements.items;

import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.UsableOutOfBattle;

public class ItemUsableOutOfBattle extends Item implements UsableOutOfBattle {

	public ItemUsableOutOfBattle(int id, String name, String desc, int ttype,int action) {
		super(id, name, desc, ttype,action);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String useOutBattle(PartyMember pm) {
		// TODO Auto-generated method stub
		return null;
	}
}
