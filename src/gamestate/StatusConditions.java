package gamestate;

import java.util.ArrayList;

import menu.TexturedMenuItem;

public enum StatusConditions {
	NORMAL(0,
			true,
			false,
			"Normal",
			"",
			1,
			null,
			null,
			null) {
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "";
		}
	},
	COLD(1,
			false,
			false,
			"Cold",
			 "%s sneezed and took damage.",
			1,
			StatusIcons.COLD,
			"%s caught a cold!",
			"%s got over the cold!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "Is suffering from a cold";
		}
	},
	
	POISON(2,
			false,
			false,
			"Poison",
			 "%s took damage from the poison.",
			1,
			StatusIcons.POISON,
			"%s became poisoned!",
			"%s was cured of the poison!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			String battleString = "[PROMPTINPUT]";
			double dmg = 20 + 4 - Math.random()*8;
			battleString += String.format(StatusConditions.POISON.getActionString(),actor.getName());
			actor.takeDamage((int) dmg,16);
//			createDOTMenuItem((int)dmg,actor);
			return battleString;
		}
	},
	
	ASTHMA(4,
			false,
			false,
			"Asthma",
			 "%s could not stop coughing.",
			5/8,
			StatusIcons.ASTHMA,
			"%s couldn't stop coughing!",
			"%s got over the asthma attack!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "Asthma";
		}
	},
	
	STONE(8,
			false,
			false,
			"Stone",
			 "%s is petrified and can't move.",
			1,
			StatusIcons.STONE,
			"%s turned to stone!",
			"%s became softened!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "Is petrify";
		}
	},
	
	DEAD(16,
			false,
			false,
			"Unconscious",
			 "If you see this, something went wrong.",
			1,
			StatusIcons.DEAD,
			"%s got hurt and collapsed!",
			"%s was revived!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "is daed";
		}
	},
	
	AMNESIA(32,
			false,
			false,
			"Amnesia",
			"%s was unable to concentrate.",
			1,
			StatusIcons.AMNESIA,
			"%s became unable to concentrate!",
			"%s was able to concentrate again!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "amnesiac";
		}
	},
	
	CRYING(64,
			false,
			false,
			"Crying",
			"%s couldn't stop crying.",
			1,
			StatusIcons.CRYING,
			"%s started to cry!",
			"%s stopped crying!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "crying";
		}
	},
	
	UNKNOWN(128,
			false,
			false,
			"Amnesia",
			"%s was unable to concentrate.",
			1,
			StatusIcons.UNKNOWN,
			"??",
			"??"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "??";
		}
	},
	
	SLEEP(256,
			false,
			false,
			"Sleep",
			 "%s is asleep.",
			0.33,
			StatusIcons.SLEEP,
			"%s fell asleep!",
			"%s woke up!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "sleep";
		}
	},
	
	PARALYZE(512,
			false,
			false,
			"Paralysis",
			 "%s is paralyzed and can't move.",
			1,
			StatusIcons.PARALYZE,
			"%s became paralyzed!",
			"%s was able to move freely again!"){
		
		@Override
		public String doActionOnTurn(BattleEntity actor) {
			return "prz";
		}
	};
	
	public static String getFailed() {
		return "But it failed.";
	}
	
	public static String getAlreadyAffected() {
		return "%s was unaffected.";
	}
	
	public static ArrayList<StatusConditions> getAfflictedStatus(int status) {
		ArrayList<StatusConditions> conditions = new ArrayList<StatusConditions>();
		if ((status & 1) == 1) {
			conditions.add(getStatusCondition(1));
		} 
		if ((status & 2) == 2) {
			conditions.add(getStatusCondition(2));
		} 
		if ((status & 4) == 4) {
			conditions.add(getStatusCondition(4));
		} 
		if ((status & 8) == 8) {
			conditions.add(getStatusCondition(8));
		} 
		if ((status & 16) == 16) {
			conditions.add(getStatusCondition(16));
		} 
		if ((status & 32) == 32) {
			conditions.add(getStatusCondition(32));
		} 
		if ((status & 64) == 64) {
			conditions.add(getStatusCondition(64));
		} 
		if ((status & 128) == 128) {
			conditions.add(getStatusCondition(128));
		} 
		if ((status & 256) == 256) {
			conditions.add(getStatusCondition(256));
		} 
		if ((status & 512) == 512) {
			conditions.add(getStatusCondition(512));
		} 
		
		if (conditions.size() == 0) {
			conditions.add(NORMAL);
		}
		
		return conditions;
	}
	
	public static StatusConditions getStatusCondition(int index) {
		StatusConditions value = null;
		switch (index) {
			case 1:		value = COLD;		break;
			case 2:		value = POISON;		break;
			case 4:		value = ASTHMA;		break;
			case 8:		value = STONE;		break;
			case 16:	value = DEAD;		break;
			case 32:	value = AMNESIA;	break;
			case 64:	value = CRYING;		break;
			case 128:	break;
			case 256:	value = SLEEP;		break;
			case 512:	value = PARALYZE;	break;
			default: 	value = NORMAL;		break;
		}
		return value;
	}
	
	private int index;				//internal index of the effect
	private boolean canMove;
	private boolean canBeCombined;
	private String action;
	private String name;
	private double probability;
	private StatusIcons iconData;
	private String initText;
	private String cureText;
	
	private StatusConditions(int value, boolean canMove, boolean canBeCombined, String name, String action, double probability, StatusIcons data, String startText, String cureText) {
		this.index = value;
		this.canMove = canMove;
		this.canBeCombined = canBeCombined;
		this.name = name;
		this.action = action;
		this.probability = probability;
		this.iconData = data;
		this.initText = startText;
		this.cureText = cureText;
	}
	
	public String doActionOnTurn(BattleEntity actor) {
		return "";
	}
	
	public String getCureText() {
		if (cureText == null) {
			return "";
		}
		return cureText;
	}
	
	public String getInitText() {
		return initText;
	}
	
	public TexturedMenuItem getIcon(double x, double y) {
		return getStatusCondition(this.index).iconData.getMenuItem(x,y);
	}
	
	public boolean effect() {
		if (Math.random() <= probability) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	public boolean canBeCombined() {
		return canBeCombined;
	}
	
//	public boolean persisitsOutOfBattle() {
//		return persists;
//	}
	
	public String getActionString() {
		return action;
	}
}
