package gamestate;

import java.util.HashMap;

public class EntityStats {
	private HashMap<String, Integer> stats;
	
	public EntityStats(int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck) {
		stats = new HashMap<String,Integer>();
		stats.put("HP",hp);
		stats.put("PP",pp);
		stats.put("ATK",atk);
		stats.put("DEF",def);
		stats.put("IQ",iq);
		stats.put("SPD",spd);
		stats.put("GUTS",guts);
		stats.put("LUCK",luck);
	}
	
	public int getStat(String stat) {
		return stats.get(stat); 
	}
	
	public void replaceStat(String stat, int val) {
		stats.put(stat,val);
	}
}
