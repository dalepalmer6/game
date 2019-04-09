package gamestate.elements.psi;

import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.Animation;

public class PSIAttack extends Item{
	private int ppCost;
	private String animation;
	private Animation anim;
	
	public PSIAttack(int id, String name, String desc, int type, int action, String animation) {
		super(id,name,desc,type,action);
		ppCost = 0;
		this.animation = animation;
		damageLowerBound = 100;
		damageUpperBound = 120;
	}
	
	public Animation getAnimation() {
		return anim;
	}
	
	public void setAnim(Animation anim) {
		this.anim = anim;
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
