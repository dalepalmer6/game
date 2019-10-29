package gamestate.elements.items;

import java.util.ArrayList;
import java.util.List;

import font.SelectionTextWindow;
import font.TextUtil;
import gamestate.BattleEntity;
import gamestate.DoorEntity;
import gamestate.EntityStats;
import gamestate.PCBattleEntity;
import gamestate.PartyMember;
import gamestate.StatusConditions;
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
	protected boolean consume = true;
	
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
		int hp = target.getBattleStats().getStat("CURHP");
		int maxHP = target.getBattleStats().getStat("HP");
		int dhp = calculateDamage();
		hp += (dhp);
		hp = Math.min(maxHP,hp);
		target.getBattleStats().replaceStat("CURHP",hp);
		return dhp;
	}
	
	public int healTarget(BattleEntity user, BattleEntity target, int amount) {
		int hp = target.getBattleStats().getStat("CURHP");
		int maxHP = target.getBattleStats().getStat("HP");
		int dhp = amount;
		hp += (dhp);
		hp = Math.min(maxHP,hp);
		target.getBattleStats().replaceStat("CURHP",hp);
//		return target.getName() + " recovered " + dhp + "HP!";
		return dhp;
	}
	
	public int suckPP(BattleEntity user, BattleEntity target) {
		int pp = user.getBattleStats().getStat("CURPP");
		int maxPP = user.getBattleStats().getStat("PP");
		int dpp = calculateDamage();
		int enemyPP = target.getBattleStats().getStat("CURPP");
		while (dpp > enemyPP) {
			dpp--;
		}
		enemyPP -= dpp;
		pp+=dpp;
		pp = Math.min(maxPP,pp);
		target.getBattleStats().replaceStat("CURPP",enemyPP);
		user.getBattleStats().replaceStat("CURPP",pp);
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
		int pp = target.getBattleStats().getStat("CURPP");
		int maxPP = target.getBattleStats().getStat("PP");
		int dpp = calculateDamage();
		pp += (dpp);
		pp = Math.min(maxPP,pp);
		target.getBattleStats().replaceStat("CURPP",pp);
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
		int hp = target.getBattleStats().getStat("CURHP");
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
		boolean healed = false;
		ArrayList<StatusConditions> statusesHealed = StatusConditions.getAfflictedStatus(status);
		ArrayList<StatusConditions> statusesAffected = StatusConditions.getAfflictedStatus(be.getState());
		statusesHealed.remove(StatusConditions.NORMAL);
		statusesAffected.remove(StatusConditions.NORMAL);
		usedString = "[PLAYSFX_healedbeta.wav]";
		for (StatusConditions s : statusesHealed) {
			if (statusesAffected.contains(s)) {
				usedString += be.getName() + " " + s.getCureText() + "[PROMPTINPUT]";
				healed = true;
			}
			if (be.removeStatus(s.getIndex()) && s.equals(StatusConditions.DEAD)) {
				healTarget(be,be,30);
			} 
		}
		
		if (!healed) {
			usedString = "Nothing happened.";
		}
		
		return 0;
	}
	
	public int causeStatus(BattleEntity be, int status) {
		//TODO do this
		if (Math.random() > 0.25) {
			usedString += StatusConditions.getFailed();
			return 0;
		}
		
		StatusConditions cond = StatusConditions.getStatusCondition(status);
		
		if (be.addStatus(cond)) {
			usedString += String.format(cond.getInitText(),be.getName());
		} else {
			usedString += String.format(StatusConditions.getAlreadyAffected(),be.getName());
		}
		return 0;
	}
	
	public int changeStat(BattleEntity be, int diff, String stat) {
		int oldStat = be.getBattleStats().getStat(stat);
		be.getBattleStats().replaceStat(stat,oldStat + diff);
		String sfxPlay = null;
		String changeType = null;
		if (diff < 0) {
			sfxPlay = "[PLAYSFX_statsdown.wav]";
			changeType = "decreased";
		} else if (diff > 0) {
			sfxPlay = "[PLAYSFX_statsup.wav]";
			changeType = "increased";
		} else {
			sfxPlay = "";
			changeType = "did not change";
		}
		String name = be.getName();
		String pluralContext = "'s";
		String statName = stat; //convert to real name
		int difference = Math.abs(diff);
		String end = "";
		if (diff != 0) {
			end = String.format("%s by %d",changeType,diff);
		} else {
			end = changeType;
		}
		
		usedString += String.format("%s%s%s %s %s.",sfxPlay,name,pluralContext,statName,changeType,difference);
		return 0;
	}
	
	public int changeStatInBattle(BattleEntity be, int diff, String stat) {
		//only allow a stat to be modified up to 1.25x in the final version of this method
		int oldStat = be.getBattleStats().getStat(stat);
		be.getBattleStats().replaceStat(stat,oldStat * (1 - (diff/100)));
		
		int difference = be.getBattleStats().getStat(stat) - oldStat;
		
		String sfxPlay = null;
		String changeType = null;
		if (difference < 0) {
			sfxPlay = "[PLAYSFX_statsdown.wav]";
			changeType = "decreased";
		} else if (difference > 0) {
			sfxPlay = "[PLAYSFX_statsup.wav]";
			changeType = "increased";
		} else {
			sfxPlay = "";
			changeType = "did not change";
		}
		String name = be.getName();
		String pluralContext = TextUtil.getPluralForm(name);
		String statName = EntityStats.statNames.get(stat); //convert to real name
//		int difference = Math.abs(diff);
		String end = "";
		if (difference != 0) {
			end = String.format("%s by %d",changeType,diff);
		} else {
			end = changeType;
		}
		
		usedString += String.format("%s%s%s %s %s.",sfxPlay,name,pluralContext,statName,end,difference);
		return 0;
	}
	
	public int useBread(BattleEntity user) {
		//transform the bread item and store the x,y and map name
		int index = ((PCBattleEntity)user).getItems().indexOf(this);
		((PCBattleEntity) user).getItems().set(index, user.getSystemState().items.get(86));
		user.getSystemState().saveCoordinates();
		return 0;
	}
	
	public int useBreadCrumbs(BattleEntity user) {
		//create a door with the saved coordinates at your position
		DoorEntity door = user.getSystemState().createWarpDoor();
		user.getSystemState().getGameState().getEntityList().add(door);
		return 0;
	}
	
	public int goToEveLoc(BattleEntity user) {
		//warp to Eve's resting place
		DoorEntity door = user.getSystemState().createWarpDoor();
		user.getSystemState().getGameState().getEntityList().add(door);
		return 0;
	}
	
	public int goToMagicant(BattleEntity user) {
		//action to warp to magicant
		DoorEntity door = user.getSystemState().createMagicantWarp();
		user.getSystemState().getGameState().getEntityList().add(door);
		return 0;
	}
	
	public int useBigBag(BattleEntity user, BattleEntity target) {
		//uses a Magic Herb, but the Big Bag is used
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
			case 4: changeStatInBattle(target,-damageVariable,"SPD");
			break;
			case 5: changeStatInBattle(target,-damageVariable,"ATK");
			break;
			case 6: changeStatInBattle(target,-damageVariable,"DEF");
			break;
			case 7: changeStatInBattle(target,-damageVariable,"GUTS");
			break;
			case 8: result += dealDamage(user,target,"ice"); 
			
			break;
			case 9: result += dealDamage(user,target,"fire");
			
			break;
			case 10: result += dealDamage(user,target,"thunder");
			
			break;
			case 11: useBread(user); 
				usedString += String.format("%s began to drop a trail of crumbs.",user.getName());
				break;
			case 12: changeStat(target,damageVariable,"GUTS");
//			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s guts increased by " + damageVariable + ".";
			break;
			case 13: changeStat(target,damageVariable,"VIT");
//			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s vitality increased by " + damageVariable + ".";
			break;
			case 14: changeStat(target,damageVariable,"IQ");
//			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s IQ increased by " + damageVariable + ".";
			break;
			case 15: changeStat(target,damageVariable,"SPD");
//			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s speed increased by " + damageVariable + ".";
			break;
			case 16: changeStat(target,damageVariable,"ATK");
//			usedString += "[PLAYSFX_statsdown.wav]" + target.getName() + "'s offense increased by " + damageVariable + ".";
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
				attemptToSteal(user,target);
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
				changeStatInBattle(target,-damageVariable,"DEF");
				break;
			case 38:
				changeStatInBattle(target,-damageVariable,"ATK");
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
	
	private void attemptToSteal(BattleEntity user, BattleEntity target) {
		PCBattleEntity pbe = null;
		if (target instanceof PCBattleEntity) {
			pbe = (PCBattleEntity) target;
			List<Item> items = pbe.getItems();
			int size = items.size();
			Item item = null;
			ArrayList<Item> stealables = new ArrayList<Item>();
			double rate = Math.random();
			for (Item i : items) {
				//look for all stealable items in the inventory i.e. items with value != 0
				if (i.getValue() == 0) {
					continue;
				} else {
					stealables.add(i);
				}
			}
			if (stealables.size() == 0) {
				usedString = String.format("%s has no items to steal.",pbe.getName());
				return;
			}
			if (rate <= 0.3) {
				int index = (int) (Math.random() * stealables.size());
				item = stealables.get(index);
				pbe.consumeItem(item);
				usedString = String.format("%s stole %s %s from %s!",user.getName(),item.getParticiple(),item.getName(),pbe.getName()); 
			} else {
				usedString = String.format("%s failed to steal anything!",user.getName());
			}
		} else {
			usedString = "ERROR: Item Steal Feature not Implemented for Party.";
		}
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
//			pm.setStats(targetBE.getBattleStats().getStat("CURHP"),targetBE.getStats().getStat("CURPP"),targetBE.getState());
			targetBE.applyBattleWear();
		}
		if (consume) {
			consume(user);
		}
		
		return result;
	}

	public void consume(PartyMember user) {
		user.getItemsList().remove(this);
	}
	
	public void consume(BattleEntity user) {
		
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
