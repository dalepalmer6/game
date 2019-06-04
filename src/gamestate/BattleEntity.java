package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import battlesystem.options.BattleAction;
import font.TextWindowWithPrompt;
import menu.EnemyAction;
import menu.StartupNew;

public class BattleEntity {
	protected String texture;
	protected String name;
	protected int status = 0;
	//0 - normal
	//1 - cold
	//2 - blind
	//4 - asthma
	//8 - stone
	//16 - dead 
	//32 - 
	protected int turnsAsleep = 0;
	protected int width;
	protected int height;
	protected EntityStats stats;
	protected SpritesheetCoordinates spriteData;
	protected BattleAction actionToPerform;
	//position to be drawn on screen
	protected StartupNew systemState;
	private boolean defending;
	protected int resistances;
	//0 - none
	//1 - freeze
	//2 - fire
	//4 - thunder
	//8 - null
	private int shieldStatus; //
	//0 - none
	//1 - shield
	//2 - psi
	//3 - power shield
	private int shieldCharge; //num hits the shield will endure
	private String predicate = "";
	
	public String getResistanceString() {
		String resists = name;
		if (resistances == 0) {
			resists += " doesn't appear to have any vulnerabilities!";
		} else {
			if ((resistances & 1) == 1) {
				resists += " appears weak to ice.";
			}
			if ((resistances & 2) == 8) {
				resists += " appears weak to fire.";
			}
			if ((resistances & 4) == 8) {
				resists += " appears weak to thunder.";
			}
			if ((resistances & 8) == 8) {
				resists += " appears weak to beam.";
			}
		}
		return resists;
	}
	
	public void setPredicate(String predicate) {
		// TODO Auto-generated method stub
		this.predicate = predicate;
	}
	
	public String getPredicate() {
		return predicate;
	}
	
//	public void setState(String string) {
//		// TODO Auto-generated method stub
//		state = string;
//	}
	public EnemyAction getRandomAction() {
		return null;
	}
	
	public StartupNew getSystemState() {
		return systemState;
	}
	
	public EntityStats getStats() {
		return stats;
	}
	
	public SpritesheetCoordinates getSpriteData() {
		return spriteData;
	}
	
	public String getName() {
		return name;
	}
	
	public void setWidth(int i) {
		this.width = i;
	}
	
	public void setHeight(int i) {
		this.height = i;
	}
	
	public int getWidth() {
		return width;
	}
	
//	public BattleEntity(String texture, String name, int hp, int pp) {
//		this.texture = texture;
//		this.name = name;
//		this.spriteData = new SpritesheetCoordinates();
//		spriteData.setPose("front_");
//		spriteData.addStateToPose("front_",0,0,32,64);
//		this.stats = new EntityStats(hp,pp,0,0,0,0,0,0,0,0,0,0,0);
//	}
//	
	public BattleEntity(String texture, String name, EntityStats es, StartupNew systemState) {
		this.texture = texture;
		this.systemState=systemState;
		this.name = name;
		this.stats = es;
	}
	
	public void ai() {
		
	}

	public String getTexture() {
		// TODO Auto-generated method stub
		return texture;
	}

	public void setResistances(int resist) {
		// TODO Auto-generated method stub
		resistances = resist;
	}
	
	public int takeDamage(int damage, int element) {
		// TODO Auto-generated method stub
		if (element != 0 && (resistances & element) == element) {
			System.out.println("It seems weak...");
			damage /= 2;
		}
		if (damage > 0 && defending) {
			//this halves all damage currently, add in checks for what type of damage, PSI or Phys
			//also check the status of shields such as PSI Shield or Power Shield when implemented
			damage /= 2;
		}
		int hp = this.stats.getStat("CURHP");
		hp -= damage;
		hp = Math.max(0,hp);
		stats.replaceStat("CURHP",hp);
		return damage;
	}

	public int getState() {
		// TODO Auto-generated method stub
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}	
	
	public void setDefend(boolean b) {
		// TODO Auto-generated method stub
		defending = b;
	}
	
	public boolean addStatus(int status) {
		int oldStatus = this.status;
		this.status |= status;
		return oldStatus != this.status;
	}
	
	public boolean removeStatus(int status) {
		this.status ^= status;
		return true;
	}
	
	public boolean getDefending()  {
		return defending;
	}
	
	public void setShield(int type) {
		this.shieldStatus = type;
		this.shieldCharge = 3;
	}
	
	public int getShieldType() {
		return shieldStatus;
	}
	
	public int getShieldCharge() {
		return shieldCharge;
	}

	public boolean decreaseShieldCharge() {
		// TODO Auto-generated method stub
		shieldCharge--;
		if (shieldCharge == 0) {
			shieldStatus = 0;
			return false;
		}
		return true;
	}
	
	public void incrementSleepTimer() {
		turnsAsleep++;
	}
	
	public int getSleepTimer() {
		return turnsAsleep;
	}
	
	public void resetSleepTimer() {
		turnsAsleep = 0;
	}

	public String getKillText() {
		// TODO Auto-generated method stub
		return "became tame.";
	}
}
