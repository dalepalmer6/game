package gamestate.elements.items;

import java.util.ArrayList;

import font.SelectionTextWindow;
import gamestate.BattleEntity;
import gamestate.DoorEntity;
import gamestate.PCBattleEntity;
import gamestate.PartyMember;
import gamestate.elements.Usable;
import gamestate.elements.psi.PSIAttack;
import menu.Menu;
import menu.StartupNew;
import menu.TeleportDestinationMenuItem;

public class Item{
	protected String name;
	protected int id;
	protected String desc;
	protected int targetType; //0 for 1 party member, 1 for all party, 2 for 1 enemy, 3 for all enemies
	protected int action;
	protected int damageLowerBound;
	protected int damageUpperBound;
	protected int equippable; //0b1, wpn; 0b10, head; 0b100, body; 0b1000, other;
	protected String participle;
	protected int value;
	protected String usedString;
	protected int damageVariable;
	private boolean inBattle;
	private boolean outBattle;
	private boolean consume = true;
	
	public int getActionType() {
		return action;
	}
	
	public int getTargetType() {
		return targetType;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public int getEquippable() {
		return equippable;
	}
	
	public boolean canPartyMemberEquip(String id) {
		switch (id) {
			case "NINTEN" : return (equippable & 16) == 16;
			case "ANA" : return (equippable & 32) == 32;
			case "LOID" : return (equippable & 64) == 64;
			case "TEDDY" : return (equippable & 128) == 128;
			default : return false;
		}
	}
	
	public String getEquipmentType() {
		String returnValue = "";
		switch (equippable & 15)  {
			case 0:	returnValue = "NAN";break;
			case 1: returnValue = "WPN";break;
			case 2: returnValue = "HED";break;
			case 4: returnValue = "BDY";break;
			case 8: returnValue = "OTR";break;
		}
		return returnValue;
	}
	
	public String getParticiple() {
		return participle;
	}
	
	//predefine a bunch of use cases for items that will be assigned to items
	public int healTarget(BattleEntity user, BattleEntity target) {
//		if ((target.getState() & 16) == 16) {
//			//is deadl, return 0;
//			return 0;
//		}
		int hp = target.getStats().getStat("CURHP");
		int maxHP = target.getStats().getStat("HP");
		int dhp = calculateDamage();
		hp += (dhp);
		hp = Math.min(maxHP,hp);
		target.getStats().replaceStat("CURHP",hp);
//		return target.getName() + " recovered " + dhp + "HP!";
		return dhp;
	}
	
	public int healTarget(BattleEntity user, BattleEntity target, int amount) {
		int hp = target.getStats().getStat("CURHP");
		int maxHP = target.getStats().getStat("HP");
		int dhp = amount;
		hp += (dhp);
		hp = Math.min(maxHP,hp);
		target.getStats().replaceStat("CURHP",hp);
//		return target.getName() + " recovered " + dhp + "HP!";
		return dhp;
	}
	
	public int suckPP(BattleEntity user, BattleEntity target) {
		int pp = user.getStats().getStat("CURPP");
		int maxPP = user.getStats().getStat("PP");
		int dpp = calculateDamage();
		int enemyPP = target.getStats().getStat("CURPP");
		while (dpp > enemyPP) {
			dpp--;
		}
		enemyPP -= dpp;
		pp+=dpp;
		pp = Math.min(maxPP,pp);
		target.getStats().replaceStat("CURPP",enemyPP);
		user.getStats().replaceStat("CURPP",pp);
		usedString = user.getName() + " sucked " + dpp + "PP from " + target.getName();
		return dpp;
	}
	
	public int setShield(BattleEntity target, int type) {
		target.setShield(type);
		usedString += target.getName() + " became enveloped by a ";
		switch (type) {
			case 1: usedString += "power shield."; break;
			case 2: usedString += "psychic shield."; break;
			case 3: usedString += "reflective power shield."; break;
		}
		return 0;
	}
	
	public int healPP(BattleEntity user, BattleEntity target) {
		int pp = target.getStats().getStat("CURPP");
		int maxPP = target.getStats().getStat("PP");
		int dpp = calculateDamage();
		pp += (dpp);
		pp = Math.min(maxPP,pp);
		target.getStats().replaceStat("CURPP",pp);
		return dpp;
	}
	
	public int dealDamage(BattleEntity user, BattleEntity target, String element) {
		int damageElement = 0;//1 for freeze, 2 for fire, 4 for lightning, 8 for null
		switch (element) {
			case "fire" : damageElement |= 2; break;
			case "ice" : damageElement |= 1;break;
			case "thunder" : damageElement |=4;break;
			case "null" : damageElement |= 8;break;
			default : damageElement = 0; break;
		}
		int hp = target.getStats().getStat("CURHP");
		int damage = calculateDamage();
		if (target.getShieldType() == 2) {
			damage/=2;
			if (target.decreaseShieldCharge()) {
				usedString = target.getName() + "'s PSI Shield absorbed some of the damage.";
			} else {
				usedString = target.getName() + "'s PSI Shield dissipated.";
			}
		}
		damage = target.takeDamage(damage,damageElement);
		return damage;
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
	
	public int calculateDamage() {
		return damageVariable;
	}
	
	public int healStatus(BattleEntity be, int status) {
		//TODO do this
		int beStatus = be.getState();
		if ((beStatus & 16) == 16 && (status & 16) == 0) {
			healTarget(be,be,30);
		}
		beStatus &= status;
		if (beStatus != be.getState()) {
			usedString = "[PLAYSFX_healedbeta.wav]" + be.getName() + " recovered their status.";
		} else {
			usedString = "Nothing happened.";
		}
		be.setStatus(beStatus);
		return 0;
	}
	
	public int causeStatus(BattleEntity be, int status) {
		//TODO do this
//		int beStatus = be.getState();
		if (be.addStatus(status)) {
			usedString = be.getName();
			if ((status & 1) == 1) {
				usedString += " ?? ";
			}
			if ((status & 2) == 2) {
				usedString += " ?? ";
			}
			if ((status & 4) == 4) {
				usedString += " started having an asthma attack! ";
			}
			if ((status & 8) == 8) {
				usedString += " ?? ";
			}
			if ((status & 16) == 16) {
				usedString = be.getName() + " was killed ";
			}
			if ((status & 32) == 32) {
				usedString += " ?? ";
			}
			if ((status & 64) == 64) {
				usedString += " ?? ";
			}
			if ((status & 128) == 128) {
				usedString += " amnesia ";
			}
			if ((status & 256) == 256) {
				usedString += " sleep ";
			}
			if ((status & 512) == 512) {
				usedString += " paralysis ";
			}
			usedString = usedString.substring(0,usedString.length()-1) + ".";
		} else {
			usedString = be.getName() + " was unaffected.";
		}
		return 0;
	}
	
	public int changeIQ(BattleEntity be, int diff) {
		int oldIQ = be.getStats().getStat("IQ");
		be.getStats().replaceStat("IQ",oldIQ + diff);
		usedString = "[PLAYSFX_statsup.wav]" + be.getName() + "'s IQ increased by " + diff + ".";
		return 0;
	}
	
	public int changeVit(BattleEntity be, int diff) {
		int oldVit = be.getStats().getStat("VIT");
		be.getStats().replaceStat("VIT",oldVit + diff);
		usedString = "[PLAYSFX_statsup.wav]" + be.getName() + "'s vitality increased by " + diff + ".";
		return 0;
	}
	
	public int changeSpeed(BattleEntity be, int diff) {
		int oldSpd = be.getStats().getStat("SPD");
		be.getStats().replaceStat("SPD",oldSpd + diff);
		usedString = "[PLAYSFX_statsup.wav]" + be.getName() + "'s speed increased by " + diff + ".";
		return 0;
	}
	
	public int changeOffense(BattleEntity be, int diff) {
		int oldAtk = be.getStats().getStat("ATK");
		be.getStats().replaceStat("ATK",oldAtk + diff);
		usedString = "[PLAYSFX_statsup.wav]" + be.getName() + "'s offense increased by " + diff + ".";
		return 0;
	}
	
	public int changeGuts(BattleEntity be, int diff) {
		int oldGuts = be.getStats().getStat("GUTS");
		be.getStats().replaceStat("GUTS",oldGuts + diff);
		usedString = "[PLAYSFX_statsup.wav]" + be.getName() + "'s guts increased by " + diff + ".";
		return 0;
	}
	
	public int useBread(BattleEntity user) {
		//transform the bread and store the x,y and map name
		int index = ((PCBattleEntity)user).getItems().indexOf(this);
		((PCBattleEntity) user).getItems().set(index, user.getSystemState().items.get(86));
		user.getSystemState().saveCoordinates();
		return 0;
	}
	
	public int useBreadCrumbs(BattleEntity user) {
		//create a door with the saved coordinates
		DoorEntity door = user.getSystemState().createWarpDoor();
		user.getSystemState().getGameState().getEntityList().add(door);
		return 0;
	}
	
	public int goToEveLoc(BattleEntity user) {
		DoorEntity door = user.getSystemState().createWarpDoor();
		user.getSystemState().getGameState().getEntityList().add(door);
		return 0;
	}
	
	public int goToMagicant(BattleEntity user) {
		DoorEntity door = user.getSystemState().createMagicantWarp();
		user.getSystemState().getGameState().getEntityList().add(door);
		return 0;
	}
	
	public int useBigBag(BattleEntity user, BattleEntity target) {
		
		return 0;
	}
	
	//every case in here needs specific programming
	public int determineAction(BattleEntity user, BattleEntity target) {
		usedString = "";
		int result = 0;
		if ((target.getState() & 16) == 16 && action != 2) {
			//is deadl, return 0;
			return 0;
		}
		switch (action) {
			case 0: 			result += healTarget(user,target); 
							  	usedString = "[PLAYSFX_lifeupbeta.wav]" + target.getName() + " recovered " + result + "HP.[PROMPTINPUT]";
							  	break;
			case 1: 			result+= healPP(user,target);
								usedString = "[PLAYSFX_pphealedfield.wav]" + target.getName() + " recovered " + result + "PP.[PROMPTINPUT]";
								break;
			case 2: 			healStatus(target,damageVariable);
								break;
			case 3: 			causeStatus(target,damageVariable);
								usedString = "[PLAYSFX_condition.wav]" + target.getName() + " is suffering from a status.";
								break;
			case 4: changeSpeedInBattle(target,damageVariable); 
			usedString = "";
			if (damageVariable < 0) {
				usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s speed decreased by " + -1*damageVariable + ".";
			} else {
				usedString += "[PLAYSFX_statsup.wav]" + target.getName() + "'s speed increased by " + damageVariable + ".";;
			}
			break;
			case 5: changeOffenseInBattle(target,damageVariable);
			usedString = "";
			if (damageVariable < 0) {
				usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s offense decreased by " + -1*damageVariable + ".";
			} else {
				usedString += "[PLAYSFX_statsup.wav]" + target.getName() + "'s offense increased by " + damageVariable + ".";;
			}
			break;
			case 6: changeDefenseInBattle(target,damageVariable);
			usedString = "";
			if (damageVariable < 0) {
				usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s defense decreased by " + -1*damageVariable + ".";
			} else {
				usedString += "[PLAYSFX_statsup.wav]" + target.getName() + "'s defense increased by " + damageVariable + ".";;
			}
			break;
			case 7: changeGutsInBattle(target,damageVariable);
			usedString = "";
			if (damageVariable < 0) {
				usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s guts decreased by " + -1*damageVariable + ".";
			} else {
				usedString += "[PLAYSFX_statsup.wav]" + target.getName() + "'s guts increased by " + damageVariable + ".";;
			}
			break;
			case 8: result += dealDamage(user,target,"ice"); 
			
			break;
			case 9: result += dealDamage(user,target,"fire");
			
			break;
			case 10: result += dealDamage(user,target,"thunder");
			
			break;
			case 11: useBread(user); 
			usedString += user.getName() + " began dropping crumbs.";
			break;
			case 12: changeGuts(target,damageVariable);
			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s guts increased by " + damageVariable + ".";
			break;
			case 13: changeVit(target,damageVariable);
			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s vitality increased by " + damageVariable + ".";
			break;
			case 14: changeIQ(target,damageVariable);
			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s IQ increased by " + damageVariable + ".";
			break;
			case 15: changeSpeed(target,damageVariable);
			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s speed increased by " + damageVariable + ".";
			break;
			case 16: changeOffense(target,damageVariable);
			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s offense increased by " + damageVariable + ".";
			break;
			case 17: result += dealDamage(user,target,"null");
			break;
			case 18: useBigBag(user,target);
			usedString += " pulled a Magic Herb out of the bag, and " + target.getName() + " ate it.";
			consume = false;
			break;
			case 19: useBreadCrumbs(user);
			usedString += user.getName() + " followed the trail of crumbs back to its source.";
			break;
			case 20: goToEveLoc(user);
			usedString += "Upon touching the Memory Chip, you were overwhelmed with memories of Eve.";
			consume = false;
			break;
			case 21: goToMagicant(user);
			consume = false;
			usedString += "Upon touching the Onyx Hook, " + user.getName() + " was transported to Magicant."; 
			break;
			case 22: 
				//teleport a
				StartupNew state = user.getSystemState();
				Menu m = new Menu(state);
				SelectionTextWindow stw = new SelectionTextWindow(0,0,10,10,state);
				/*make this its own method and get the destinations from a different place*/
				stw.add(new TeleportDestinationMenuItem("My Home",338,266,"podunk",state));
				stw.add(new TeleportDestinationMenuItem("Podunk",1544,3168,"podunk",state));
				stw.add(new TeleportDestinationMenuItem("Merrysville",1735,2161,"merrysville",state));
				stw.add(new TeleportDestinationMenuItem("Reindeer",1675,1613,"reindeer",state));
				stw.add(new TeleportDestinationMenuItem("Spookane",2015,1591,"spookane",state));
				stw.add(new TeleportDestinationMenuItem("Snowman",2313,972,"snowman",state));
				
				m.addMenuItem(stw);
				state.getMenuStack().push(m);
				break;
			case 23:
				//teleport b
				break;
			case 24: 
				//telepathy
				break;
			case 25:
				suckPP(user,target);
				break;
			case 26:
				//bash
				
				break;
			case 27:
				//cause a status
				causeStatus(target,damageVariable);
				break;
			case 28:
				usedString = "Steal result text";
				break;
			case 29:
				setShield(target,damageVariable);
				break;
			case 30:
				//unused
				break;
			case 31:
				//unused
				break;
			case 32:
				//unused
				break;
			case 33:
				//unused
				break;
			case 34:
				//unused
				break;
			case 35:
				//unused
				break;
			case 36:
				setShield(target,0);
				break;
			case 37:
				changeDefenseInBattle(target,-damageVariable);
				break;
			case 38:
				changeOffenseInBattle(target,-damageVariable);
				break;
			case 39:
				//unused
				break;
			case 40:
				//unused
				break;
			case 41:
				fleeBattle(user);
				break;
			case 42:
				//unused
				break;
		}
		return result;
	}
	
	private int changeGutsInBattle(BattleEntity target, int damageVariable2) {
		// TODO Auto-generated method stub
		
		return  0;
	}

	private int changeDefenseInBattle(BattleEntity target, int damageVariable2) {
		// TODO Auto-generated method stub
		
		return  0;
	}

	private int changeOffenseInBattle(BattleEntity target, int damageVariable2) {
		// TODO Auto-generated method stub
		
		return  0;
	}

	private int changeSpeedInBattle(BattleEntity target, int damageVariable2) {
		// TODO Auto-generated method stub
		
		return  0;
	}

	public int useInBattle(BattleEntity user, BattleEntity target) {
		int result = 0;
		result = determineAction(user,target);
		if (user instanceof PCBattleEntity) {
			((PCBattleEntity) user).getItems().remove(this);
		}
		return result;
	}
	
	public String getUsedString() {
		return usedString;
	}
	
	public String useOutBattle(PartyMember pm) {
		
		return null;
	}
	
	public Item(int id, String name, String desc, int ttype,int action, int equippable, String participle, int value, int useVar) {
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.targetType = ttype;
		this.action = action;
		this.equippable = equippable;
		this.participle = participle;
		this.value = value;
		this.damageVariable = useVar;
		if (!(this instanceof PSIAttack)) {
			damageUpperBound = damageVariable;
			damageLowerBound = damageVariable;
		} else {
//			damageVariable = damageUpperBound;
		}
	}
	
	public Item clone() {
		Item clone = new Item(id,name,desc,targetType,action,equippable,participle,value,damageVariable);
		clone.setUsage(inBattle,outBattle);
		return clone;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void healTarget(PartyMember user, PartyMember target) {
		
	}
	
	public String getUseText() {
//		switch() {
//			
//		}
		return null;
	}
	
	public String useOutOfBattle(PartyMember user, ArrayList<PartyMember> target) {
		// TODO Auto-generated method stub
		String result = "";
//		calculateDamage();
//		getUseText();
		if (!(this instanceof PSIAttack)) {
			result += user.getName() + " pulled out " + participle + " " + name + " and ";
			if (target.size() > 1) {
				result += "everyone shared it.";
			} else {
				if (user.equals(target.get(0))) {
					result += "used it";
				} else {
					result += target.get(0).getName() + " used it.";
				}
			}
			result += "[PROMPTINPUT][WAIT10]";
		}
		
		for (PartyMember pm : target) {
			BattleEntity targetBE = pm.createBattleEntity();
			determineAction(user.createBattleEntity(),targetBE);
			result += usedString;
			pm.setStats(targetBE.getStats().getStat("CURHP"),targetBE.getStats().getStat("CURPP"),targetBE.getState());
		}
		if (consume) {
			user.getItemsList().remove(this);
		}
		
		return result;
	}

	public int getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	public void setUsage(boolean inBattleUsable, boolean outBattleUsable) {
		// TODO Auto-generated method stub
		this.inBattle = inBattleUsable;
		this.outBattle = outBattleUsable;
	}
	
	public boolean canUseInBattle() {
		return inBattle;
	}
	
	public boolean canUseOutBattle() {
		return outBattle;
	}
}
