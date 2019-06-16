package gamestate;

import java.util.ArrayList;

import gamestate.elements.items.Item;
import menu.StartupNew;

public class PresentEntity extends Entity{
	private int itemId;
	private Item item;
	private String flagName;
	
	public PresentEntity(double x, double y, int itemId,  String name, StartupNew m) {
		super("present.png", x, y, 16*4, 16*4, m, "present"+itemId);
		// TODO Auto-generated constructor stub
		this.itemId = itemId;
		this.item = state.items.get(itemId);
		this.flagName = name;
		actionTaken = "idle";
		text = "[FLAGISSET_present" + flagName + "]The box has been emptied.[ELSE][PLAYSFX_openpresent.wav][NINTEN] opened the present.[PROMPTINPUT]Inside there was " + item.getParticiple() + " " + item.getName() + ".[PROMPTINPUT][PLAYSFX_getitem.wav][ADDITEM_" + item.getId() + "][NINTEN] took it.[SETFLAG_present" + flagName + "][ENDIF] ";
	}
	
	public void setNewParams(int x, int y, String name, int id) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
		this.flagName = name;
		this.itemId = id;
		this.item = state.items.get(id);
		
	}
	
	public void update(GameState gs) {
		super.update(gs);
		if (gs.getFlag("present" + flagName)) {
			actionTaken = "opened";
		}
	}
	
	public int getItemId() {
		return itemId;
	}

	public String toString() {
		return x + "," + y + "," + itemId + "," + flagName;
	}
	
	public void move() {
		
	}
	
}
