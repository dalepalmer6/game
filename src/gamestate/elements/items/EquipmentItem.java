package gamestate.elements.items;

import gamestate.EntityStats;
import gamestate.elements.items.Item;

//(int lvl,int chp, int cpp, int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck, int vit,int curxp)
public class EquipmentItem extends Item {
	private EntityStats statsChange;
	private long resistances;
	
	public EquipmentItem(int id, String name, String desc, int ttype,int action, int equippable,
			int off,int def, int spd,int luck, int hp,int pp, long resists, String participle, int value) {
		super(id,name,desc,ttype,action,equippable,participle,value);
		statsChange=new EntityStats(0,0,0,0,0,off,def,0,spd,0,luck,0,0);
		resistances = resists;
	}
	
	public EntityStats getStats() {
		return statsChange;
	}
}
