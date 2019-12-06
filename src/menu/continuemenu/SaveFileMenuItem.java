package menu.continuemenu;

import java.io.IOException;
import java.util.ArrayList;

import gamestate.GameState;
import gamestate.entities.Entity;
import menu.MenuItem;
import menu.TexturedMenuItem;
import menu.actionmenu.equipmenu.TextLabel;
import menu.animation.AnimationMenu;
import menu.mapeditmenu.MapNames;
import menu.windows.TextWindow;
import system.MainWindow;
import system.SystemState;

public class SaveFileMenuItem extends MenuItem {
	GameState gs;
	String name;
	ArrayList<MenuItem> items;
	ArrayList<MenuItem> partyMems;
	private boolean doSave;
	
	public SaveFileMenuItem(String s, SystemState state, GameState gs, String name, int i) {
		super(s,256,i*384,state);
		this.gs = gs;
		this.name = name;
		items = new ArrayList<MenuItem>();
		partyMems = new ArrayList<MenuItem>();
		createItem();
		createPartyMemberMenuItems();
	}
	
	public void reset() {
		items = new ArrayList<MenuItem>();
		partyMems = new ArrayList<MenuItem>();
		createItem();
		createPartyMemberMenuItems();
	}
	
	public void createPartyMemberMenuItems() {
		boolean[] needed = {gs.getFlag("nintenInParty"),gs.getFlag("anaInParty"),gs.getFlag("loidInParty"),gs.getFlag("pippiInParty"),gs.getFlag("teddyInParty"),};
		int x = 0;
		int y = 48;
		int i = 0;
		String entityId = "";
		for (boolean need : needed) {
			if (need) {
				switch(i++) {
					case 0: entityId = SystemState.Characters.NINTEN.getId(); break;
					case 1: entityId = SystemState.Characters.ANA.getId(); break;
					case 2: entityId = SystemState.Characters.LOID.getId(); break;
					case 3: entityId = "pippi"; break;
					case 4: entityId = SystemState.Characters.TEDDY.getId(); break;
				}
				Entity e = state.getEntityFromEnum(entityId).createCopy(0,0,16*4,24*4,"entity");
				//int x, int y, int width, int height,  SystemState state, String texture, int dx, int dy, int dw, int dh
				TexturedMenuItem tmi = new TexturedMenuItem("",this.x + (x+=96),this.y + y,24*4,32*4,state,entityId + ".png",0,0,24,32);
				addMenuItem(tmi);
			}
		}
	}
	
	public String execute() {
		if (!doSave) {
			state.setSaveFileName(name);
			state.getMenuStack().clear();
			SystemState.out.println("Loading a previous game.");
			AnimationMenu an = new AnimationMenu(state);
			an.createAnimMenu();
			state.getMenuStack().push(an);
		} else {
			//save game using this file
			try {
				state.getGameState().saveData(name);
				gs = new GameState(state,null);
				gs.loadFromSaveFile(name);
				reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	//create a text window with text saying the name
	public void createItem() {
		TextWindow tw = new TextWindow(true," ",(int)x,(int)y,20,3,state);
		addMenuItem(tw);
		TextLabel mapLabel = new TextLabel(gs.getNintenName(),(int)x + 512,(int)y+24,state);
		TextLabel nintenNameLabel = new TextLabel("Level " + gs.getNintenLevel(),(int)x+512,(int)y+88,state);
		TextLabel nintenLevelLabel = new TextLabel(MapNames.getName(gs.getMapName()),(int)x+512,(int)y+88+64,state);
		TextLabel timePlayedLabel = new TextLabel(gs.getFormattedTimePlayed(),(int)x+512,(int)y+88+128,state);
		addMenuItem(mapLabel);
		addMenuItem(nintenNameLabel);
		addMenuItem(nintenLevelLabel);
		addMenuItem(timePlayedLabel);
		
		for (int i = 0; i < 9; i++) {
			if (gs.getFlag("melody" + (i+1))) {
				//add a menu item for the melody
				int dx = 0;
				int dy = 0;
				int wid = 12;
				int hgt = 18;
				TexturedMenuItem tmi = new TexturedMenuItem("",(int) x + 768 + ((wid+4)*4*i) + 64, (int) y + 88, wid * 4, hgt * 4, state, "melodies.png",i*wid,0,wid,hgt);
				addMenuItem(tmi);
			}
		}
	}

	public void addMenuItem(MenuItem mi) {
		items.add(mi);
	}
	
	@Override
	public void draw(MainWindow m) {
		for (MenuItem mi : items) {
			mi.draw(m);
		}
	}

	public void setSaving() {
		// TODO Auto-generated method stub
		doSave = true;
	}
	
}
