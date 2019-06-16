package actionmenu;
import java.util.ArrayList;

import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.InvisibleMenuItem;
import canvas.Controllable;
import font.TextWindow;
import gamestate.PartyMember;
import global.InputController;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class PlayerInfoWindow extends Menu implements Controllable{
	protected int index;
	protected InvisibleMenuItem invisSelectItem;
	protected ArrayList<PartyMember> party;
	protected ArrayList<MenuItem> partyIcons;
	protected TextWindow menuTitle;
	
	public enum PlayerOffsets {
		NINTEN(22,32,15,17),
		ANA(38,32,15,17),
		LOID(54,32,15,17),
		PIPPI(70,32,21,17),
		TEDDY(92,32,16,17);
		
		public int dx;
		public int dy;
		public int dw;
		public int dh;
		
		PlayerOffsets(int dx, int dy, int dw, int dh) {
			this.dx = dx;
			this.dy = dy;
			this.dw = dw;
			this.dh = dh;
		}
		
	}
	
	public PlayerInfoWindow(StartupNew state, ArrayList<PartyMember> party) {
		super(state);
		addMenuItem(new BackingBar(state));
		if (!state.inBattle) {
			this.party = party;
			reload();
			for(MenuItem m : partyIcons) {
				addMenuItem(m);
			}
			addMenuItem(menuTitle);
			addMenuItem(invisSelectItem);
		}
		
	}
	
	public void reload() {
		backShouldExit = true;
		if (partyIcons == null) {
			partyIcons = new ArrayList<MenuItem>();
			int j = 0;
			for (PartyMember pm : party) {
				PlayerOffsets po = null;
				switch(pm.getId()) {
					case "NINTEN": po = PlayerOffsets.NINTEN; break;
					case "ANA":po = PlayerOffsets.ANA; break;
					case "LOID":po = PlayerOffsets.LOID; break;
					case "PIPPI":po = PlayerOffsets.PIPPI; break;
					case "TEDDY":po = PlayerOffsets.TEDDY; break;
					default : po = PlayerOffsets.NINTEN; break;
				}
				PartyMemberIconMenuItem i = new PartyMemberIconMenuItem(pm.getName(),256 + j*96,0,64,64,state,"menu.png",po.dx,po.dy,po.dw,po.dh);
				partyIcons.add(i);
				j++;
			}
		}
		for (MenuItem i : partyIcons) {
			addToMenuItems(i);
		}
		
		if (menuTitle == null) {
			menuTitle = new TextWindow(true,"Test",128 + 10*128,0,2,0,state);
		}
		addToMenuItems(menuTitle);
		if (invisSelectItem == null) {
			invisSelectItem = new InvisibleMenuItem(state,party.size());
		}
//		addToMenuItems(backingBar);
		addToMenuItems(invisSelectItem);
	}
	
	public void update(InputController input) {
		index = invisSelectItem.getIndex();
		int i = 0;
		for (MenuItem pi : partyIcons) {
			if (i == index) {
				pi.setHovered(true);
			} else {
				pi.setHovered(false);
			}
			i++;
		}
	}

	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
