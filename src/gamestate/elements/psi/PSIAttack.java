package gamestate.elements.psi;

import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.items.Item;

public class PSIAttack extends Item{
	private int ppCost;
	private String animation;
	
	public PSIAttack(int id, String name, String desc, int type, int action, String animation) {
		super(id,name,desc,type,action);
		ppCost = 0;
		this.animation = animation;
		damageLowerBound = 100;
		damageUpperBound = 120;
	}
	
	public String getAnimation() {
		return animation;
	}
	
//	public String useOutBattle(PartyMember pm) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	public String useInBattle(BattleEntity user, BattleEntity target) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
}
