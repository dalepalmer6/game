package gamestate;

import gamestate.elements.items.Item;
import menu.StartupNew;

public class PresentEntity extends Entity{
	private int itemId;
	private Item item;
	private String flagName;
	
	public PresentEntity(int x, int y, int itemId,  String name, StartupNew m) {
		super("present.png", x, y, 16*4, 16*4, m, "present"+itemId);
		// TODO Auto-generated constructor stub
		this.itemId = itemId;
		this.item = state.items.get(itemId);
		this.flagName = name;
		actionTaken = "idle";
		text = "[FLAGISSET_present" + flagName + "]The box has been emptied.[ELSE][NINTEN] opened the present. Inside there was " + item.getParticiple() + " " + item.getName() + "[ADDITEM_" + item.getId() + "]. [PROMPTINPUT]You took it.[SETFLAG_present" + flagName + "][ENDIF] ";
	}
	
	public void update(GameState gs) {
		super.update(gs);
		if (gs.getFlag("present" + flagName)) {
			actionTaken = "opened";
		}
	}

	public String toString() {
		return x + "," + y + "," + itemId + "," + flagName;
	}
	
	public void move() {
		
	}
	
}
