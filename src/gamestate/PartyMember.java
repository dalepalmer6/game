package gamestate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import menu.StartupNew;

public class PartyMember {
	private String name;
	private String id;
	private int status = 0;
	private EntityStats stats;
	private EntityStats baseStats;//non modified by equipment, serves as a reference
	private Entity entity;
	private long knownPSI;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<EquipmentItem> equips = new ArrayList<EquipmentItem>(4);//in the future, make the index here reference the inventory instead of an item
	private StartupNew state;
	private int index;
	
	public int hasItem(int id) {
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if (item.getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	public int getOpenInventorySpace() {
		for (int j = 0; j < items.size(); j++) {
			Item i = items.get(j);
			if (i.getId() == 0) {
				return j;
			}
		}
		return -1;//no empty space
	}
	
	public void setItem(Item i, int space) {
		items.set(space,i);
	}
	
	public void consumeItem(Item i) {
		items.remove(i);
	}
	
	public String getId() {
		return id;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void updateStats() {
		stats = baseStats.createCopy();
		for (Item i : equips) {
			stats.addStats(((EquipmentItem) i).getStats());
		}
	}
	
	public EntityStats seeStatDiffs(EquipmentItem i) {
		stats = baseStats.createCopy();
		for (Item item : equips) {
			if (item.getEquipmentType().equals(i.getEquipmentType())) {
				//use i instead
				stats.addStats(i.getStats());
				continue;
			}
			stats.addStats(((EquipmentItem)item).getStats());
		}
		return stats;
	}
	
	public ArrayList<EquipmentItem> getEquips() {
		//0 weapon; 1 head; 2 body; 3 other
		return equips;
	}
	
	/*
	 * Current stats with equipment
	 * */
	public EntityStats getStats() {
		return stats;
	}
	
	/*
	 * Current stats without equipment
	 */
	public EntityStats getBaseStats() {
		return baseStats;
	}
	
	public long getKnownPSI() {
		return knownPSI;
	}
	
	public ArrayList<Item> getItemsList() {
		return items;
	}
	
	public boolean addItemToBag(Item i) {
		if (items.size() < 20) {
			items.add(i);
			return true;
		}
		return false;	
	}
	
	public String getName() {
		return name;
	}
	
	public PartyMember(String id,int number, StartupNew state) {
		this.index = number;
		this.state = state;
		this.id = id;
		loadStats(id);
	}
	
	public void loadStats(String i) {
		//load from a saved file
		String pathToEntity = "savedata/players/" + i;
		File file = new File(pathToEntity);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String[] row;
			//first line contains stat data
			//NAME,LEVEL,CURHP,CURPP,HP,PP,OFFENSE,DEFENSE,VITALITY,IQ,GUTS,LUCK,SPEED,CURRENTXP
			row = br.readLine().split(",");
			String name = row[0];
			int level = Integer.parseInt(row[1]);
			int curHP = Integer.parseInt(row[2]);
			int curPP = Integer.parseInt(row[3]);
			int HP = Integer.parseInt(row[4]);
			int PP = Integer.parseInt(row[5]);
			int offense = Integer.parseInt(row[6]);
			int defense = Integer.parseInt(row[7]);
			int vit = Integer.parseInt(row[8]);
			int iq = Integer.parseInt(row[9]);
			int guts = Integer.parseInt(row[10]);
			int luck = Integer.parseInt(row[11]);
			int speed = Integer.parseInt(row[12]);
			int curxp = Integer.parseInt(row[13]);
			this.name = name;
			baseStats = new EntityStats(level,curHP,curPP,HP,PP,offense,defense,iq,speed,guts,luck,vit,curxp);
			stats = baseStats.createCopy();
			//second line contains item bag data
			row = br.readLine().split(",");
			for (String elem : row) {
				Item item = state.items.get(Integer.parseInt(elem)).clone();
				this.items.add(item);
			}
			//third line contains psi data
			String bin = br.readLine();
			knownPSI = Long.parseLong(bin,2);
			//fourth line contains equipment indices
			row = br.readLine().split(",");
			int j = 0;
			for (String elem : row) {
				EquipmentItem item = (EquipmentItem) state.items.get(Integer.parseInt(elem));
				this.equips.add(j++,item);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PCBattleEntity createBattleEntity() {
		PCBattleEntity be = new PCBattleEntity("",name,id,stats,state,status);
		be.setItems(items);
		return be;
	}

	public void addExp(int awardEXP) {
		// TODO Auto-generated method stub
		stats.replaceStat("CURXP",stats.getStat("CURXP") + awardEXP);
		baseStats.replaceStat("CURXP",baseStats.getStat("CURXP") + awardEXP);
	}

	public void setStats(EntityStats stats) {
		// TODO Auto-generated method stub
		this.stats = stats;
	}

	public void setStats(int stat, int stat2, int status) {
		// TODO Auto-generated method stub
		this.baseStats.replaceStat("CURHP",stat);
		this.baseStats.replaceStat("CURPP",stat2);
		this.status = status;
	}
	
	public void addStats(EntityStats diffs) {
		this.baseStats.addStats(diffs);
		this.stats.addStats(diffs);
	}

	public void setKnownPSI(long newPSI) {
		// TODO Auto-generated method stub
		knownPSI = newPSI;
	}
}
