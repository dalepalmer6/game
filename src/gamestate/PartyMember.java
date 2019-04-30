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
	private EntityStats stats;
	private EntityStats baseStats;//non modified by equipment, serves as a reference
	private Entity entity;
	private long knownPSI;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Item> equips = new ArrayList<Item>(4);//in the future, make the index here reference the inventory instead of an item
	private StartupNew state;
	private int index;
	
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
	
	public ArrayList<Item> getEquips() {
		//0 weapon; 1 head; 2 body; 3 other
		return equips;
	}
	
	public EntityStats getStats() {
		return stats;
	}
	
	public long getKnownPSI() {
		return knownPSI;
	}
	
	public ArrayList<Item> getItemsList() {
		return items;
	}
	
	public void addItemToBag(Item i) {
		items.add(i);
	}
	
	public String getName() {
		return name;
	}
	
	public PartyMember(String id,int number, StartupNew state) {
		this.index = number;
		this.state = state;
		this.id = id;
		loadStats(number);
	}
	
	public void loadStats(int i) {
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
				Item item =  state.items.get(Integer.parseInt(elem));
				this.items.add(item);
			}
			//third line contains psi data
			String bin = br.readLine();
			knownPSI = Long.parseLong(bin,2);
			//fourth line contains equipment indices
			row = br.readLine().split(",");
			int j = 0;
			for (String elem : row) {
				Item item = state.items.get(Integer.parseInt(elem));
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
		return new PCBattleEntity("",name,stats);
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

	public void setStats(int stat, int stat2) {
		// TODO Auto-generated method stub
		this.baseStats.replaceStat("CURHP",stat);
		this.baseStats.replaceStat("CURPP",stat2);
	}
}
