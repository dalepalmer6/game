package gamestate.elements.items;

import gamestate.BattleEntity;
import gamestate.PartyMember;
import gamestate.elements.Usable;

public class Item{
	protected String name;
	protected int id;
	protected String desc;
	protected int targetType; //0 for 1 party member, 1 for all party, 2 for 1 enemy, 3 for all enemies
	private int action;
	protected int damageLowerBound;
	protected int damageUpperBound;
	
	public int getTargetType() {
		return targetType;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	
	
	//predefine a bunch of use cases for items that will be assigned to items
	public String healTarget(BattleEntity user, BattleEntity target) {
		int hp = target.getStats().getStat("HP");
		int dhp = (int) ((int) hp * 0.30);
		
		hp += (dhp);
		target.getStats().replaceStat("HP",hp);
		return target.getName() + " recovered " + dhp + "HP!";
	}
	
	public String dealDamage(BattleEntity user, BattleEntity target, String element) {
		int hp = target.getStats().getStat("HP");
		
		return target.getName() + " suffered damage of " + damageLowerBound;
	}
	
	public String fleeBattle(BattleEntity user) {
		int randomVal = (int) (Math.random() * 100);
		String result = user.getName() + " tried to escape...";
		if (randomVal >= 50) {
			result += " and did!";
		} else {
			result += " but couldn't!";
		}
		return result;
	}
	
	public String superHeal(BattleEntity user, BattleEntity target) {
		String result = "Used super heal, remove all stuff";
		return result;
	}
	
	public String healStatus(BattleEntity user, BattleEntity target) {
		String result = "Healing some status";
		return result;
	}
	
	public String shield(BattleEntity user, BattleEntity target) {
		String result = "Set up a barrier ";
		
		return result;
	}
	
	public String psiShield(BattleEntity user, BattleEntity target) {
		String result = "PSI shield";
		
		return result;
	}
	
	public String useInBattle(BattleEntity user, BattleEntity target) {
		String result = "";
		switch (action) {
			case 0: result += healTarget(user,target); break;
			case 1: result += dealDamage(user,target,"ice"); break;
			case 2: result += dealDamage(user,target,"fire"); break;
			case 3: result += dealDamage(user,target,"thunder"); break;
			case 4: result += fleeBattle(user); break;
			case 5: result += superHeal(user,target); break;
			case 6: result += healStatus(user,target); break;
			case 7: result += shield(user,target); break;
			case 8: result += psiShield(user,target); break;
		}
		return result;
	}
	
	public String useOutBattle(PartyMember pm) {
		
		return null;
	}
	

	
	public Item(int id, String name, String desc, int ttype,int action) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.targetType = ttype;
		this.action = action;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
