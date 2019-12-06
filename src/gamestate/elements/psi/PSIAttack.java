package gamestate.elements.psi;

import battlesystem.BattleEntity;
import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.animation.Animation;

public class PSIAttack extends Item{
	private Animation anim;
	private String classification;
	private String family;
	private String stage;
	private int ppUsed;
	
	public PSIAttack(int id, String name, String desc, int type, int action, String animation,String classification,String family,String stage, int ppUsed) {
		super(id,name,desc,type,action,-1,"",0,0);
//		ppCost = 0;
//		this.animation = animation;
		this.classification = classification;
		this.family = family;
		this.stage = stage;
		this.ppUsed = ppUsed;
		this.consume = true;
	}
	
	public int getPPConsumption() {
		return ppUsed;
	}
	
	public void consume(PartyMember user) {
		user.consumePP(this.ppUsed);
	}
	
	public void consume(BattleEntity user) {
		user.getBattleStats().replaceStat("CURPP",user.getBattleStats().getStat("CURPP") - ppUsed);
	}
	
	
	public void setMinMaxDmg(int min, int max) {
		this.damageUpperBound = max;
		this.damageLowerBound = min;
		damageVariable = max;
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
	
	public void use() {
		
	}

}
