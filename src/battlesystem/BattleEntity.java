package battlesystem;

import java.util.ArrayList;

import battlesystem.options.BattleAction;
import gamestate.EntityStats;
import menu.windows.TextWindowWithPrompt;
import system.SystemState;
import system.data.StatusConditions;
import system.sprites.SpritesheetCoordinates;

public class BattleEntity {
	protected String texture;
	protected String name;
	protected int status = 0;
	protected int turnsAsleep = 0;
	protected int width;
	protected int height;
	protected EntityStats stats;
	protected EntityStats battleStats;
	protected SpritesheetCoordinates spriteData;
	protected BattleAction actionToPerform;
	//position to be drawn on screen
	protected SystemState systemState;
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
	protected String predicate = "";
	
	public void applyBattleWear() {
		stats.applyBattleWear(battleStats);
	}
	
	public void consumePP(int usedPP) {
		
	}
	
	public boolean hasEnoughPP(int usedPP) {
		if (battleStats.getStat("CURPP") < usedPP) {
			return false;
		} else {
			return true;
		}
	}
	
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
	
	public EnemyAction getRandomAction() {
		return null;
	}
	
	public SystemState getSystemState() {
		return systemState;
	}
	
	public EntityStats getStats() {
		return stats;
	}
	
	public EntityStats getBattleStats() {
		return battleStats;
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
	
	public BattleEntity(String texture, String name, EntityStats es, SystemState systemState) {
		this.texture = texture;
		this.systemState=systemState;
		this.name = name;
		this.stats = es;
		this.battleStats = es.createCopy();
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
//			SystemState.out.println("It seems weak...");
			damage /= 2;
		}
		if (damage > 0 && defending) {
			//this halves all damage currently, add in checks for what type of damage, PSI or Phys
			//also check the status of shields such as PSI Shield or Power Shield when implemented
			damage /= 2;
		}
		int hp = this.battleStats.getStat("CURHP");
		hp -= damage;
		hp = Math.max(0,hp);
		battleStats.replaceStat("CURHP",hp);
		return damage;
	}

	public int getState() {
		// TODO Auto-generated method stub
		return battleStats.getStatus();
	}

	public void setStatus(int status) {
		this.battleStats.setStatus(status);
	}	
	
	public void setDefend(boolean b) {
		// TODO Auto-generated method stub
		defending = b;
	}
	
	public boolean addStatus(int status) {
		int oldStatus = battleStats.getStatus();
		
		battleStats.setStatus(battleStats.getStatus() | status);
		return oldStatus != battleStats.getStatus();
	}
	
	public boolean addStatus(StatusConditions status) {
		int oldStatus = battleStats.getStatus();
		
		battleStats.setStatus(battleStats.getStatus() | status.getIndex());
		return oldStatus != battleStats.getStatus();
	}
	
	//remove the status bit
	public boolean removeStatus(int status) {
		battleStats.setStatus(battleStats.getStatus() & ~status);
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
