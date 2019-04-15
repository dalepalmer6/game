package gamestate.elements.psi;

import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.Animation;

public class PSIAttack extends Item{
	private int ppCost;
	private String animation;
	private Animation anim;
	private String classification;
	private String family;
	private String stage;
	public PSIAttack(int id, String name, String desc, int type, int action, String animation,String classification,String family,String stage) {
		super(id,name,desc,type,action,-1,"");
		ppCost = 0;
		this.animation = animation;
		this.classification = classification;
		this.family = family;
		this.stage = stage;
	}
	
	public void setMinMaxDmg(int min, int max) {
		this.damageUpperBound = max;
		this.damageLowerBound = min;
	}
	
	public int calculateDamage() {
		double  damage = damageUpperBound - damageLowerBound;
		double  range = damage * Math.random();
		return (int) (damageLowerBound + range);
	}
	
	public String getStage() {
		return stage;
	}
	public String getFamily() {
		return family;
	}
	
	public String getClassification() {
		return classification;
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
