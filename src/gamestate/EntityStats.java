package gamestate;

import java.util.HashMap;
import java.util.Set;

public class EntityStats {
	private HashMap<String, Integer> stats;
	public static HashMap<String,String> statNames = new HashMap<String,String>() {{
		put("LVL","Level");
		put("CURHP","Current HP");
		put("CURPP","Current PP");
		put("HP","HP");
		put("PP","PP");
		put("ATK","Offense");
		put("DEF","Defense");
		put("IQ","IQ");
		put("VIT","Vitality");
		put("LUCK","Luck");
		put("GUTS","Guts");
		put("SPD","Speed");
		put("CURXP","Current Experience");
	}};
	private int status;
	
	public Set<String> getStatKeys() {
		return stats.keySet();
	}
	
	public EntityStats(int lvl,int chp, int cpp, int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck, int vit,int curxp) {
		stats = new HashMap<String,Integer>();
		stats.put("LVL",lvl);
		stats.put("CURHP",chp);
		stats.put("CURPP",cpp);
		stats.put("HP",hp);
		stats.put("PP",pp);
		stats.put("ATK",atk);
		stats.put("DEF",def);
		stats.put("IQ",iq);
		stats.put("VIT",vit);
		stats.put("SPD",spd);
		stats.put("GUTS",guts);
		stats.put("LUCK",luck);
		stats.put("CURXP",curxp);
		status = 0;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int s) {
		status = s;
	}
	
	public String toString() {
		return stats.get("LVL") + "," +
				stats.get("CURHP") + "," +
				stats.get("CURPP") + "," +
				stats.get("HP") + "," +
				stats.get("PP") + "," +
				stats.get("ATK") + "," +
				stats.get("DEF") + "," +
				stats.get("IQ") + "," +
				stats.get("VIT") + "," +
				stats.get("SPD") + "," +
				stats.get("GUTS") + "," +
				stats.get("LUCK") + "," +
				stats.get("CURXP") + "," +
				status;
	}
	
	public void addStats(EntityStats es) {
		for (String key : stats.keySet()) {
			this.replaceStat(key,this.getStat(key)+es.getStat(key));
		}
	}
	
	public int getStat(String stat) {
		return stats.get(stat); 
	}
	
	public void replaceStat(String stat, int val) {
		stats.put(stat,val);
	}
	
	//sets the stats 
	public void applyBattleWear(EntityStats worn) {
		replaceStat("CURHP",worn.getStat("CURHP"));
		replaceStat("CURPP",worn.getStat("CURPP"));
		setStatus(worn.getStatus());
	}

	public EntityStats createCopy() {
		// TODO Auto-generated method stub
		EntityStats clone = new EntityStats(stats.get("LVL"),
				stats.get("CURHP"),
				stats.get("CURPP"),
				stats.get("HP"),
				stats.get("PP"),
				stats.get("ATK"),
				stats.get("DEF"),
				stats.get("IQ"),
				stats.get("VIT"),
				stats.get("SPD"),
				stats.get("GUTS"),
				stats.get("LUCK"),
				stats.get("CURXP"));
		clone.setStatus(status);
		return clone;
	}
}
