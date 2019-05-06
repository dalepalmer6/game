package actionmenu.statusmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import actionmenu.goodsmenu.InvisibleMenuItem;
import canvas.Controllable;
import font.SelectionTextWindow;
import gamestate.EntityStats;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class StatusMenu extends Menu {
	private ArrayList<PartyMember> party;
	private int index;
	private InvisibleMenuItem invisSelectItem;
	private ArrayList<TextWindowWithLabels> statuses;
	
	public StatusMenu(StartupNew m, ArrayList<PartyMember> party) {
		super(m);
		this.party = party;
	}
	
	public void update(InputController input) {
		this.setToRemove(statuses.get(index));
		index = invisSelectItem.getIndex();
		this.addToMenuItems(statuses.get(index));
//		if (invisSelectItem.getCanLoadInventory()) {
//			GoodsMenu gm = new GoodsMenu(state,party,index);
//			gm.createMenu();
//			state.getMenuStack().push(gm);
//		}
	}
	
	public void createMenu() {
		//hardcoded menu here
		TextWindowWithLabels twwl=null;
		statuses = new ArrayList<TextWindowWithLabels>();
		for (int i = 0; i < party.size(); i++) {
			PartyMember pm = party.get(i);
			twwl = new TextWindowWithLabels(state);
			EntityStats pmStats = pm.getStats();
//			twwl.setDrawOnly(true);
			twwl.createLabel(pm.getName(),32,0);
			twwl.createLabel("Level",32,32);
			twwl.createLabel("" + pmStats.getStat("LVL"),twwl.getX() + twwl.getWidth(),32);
			twwl.createLabel("Status Affliction", 32,64);
			twwl.createLabel("HP",32,128);
			twwl.createLabel("" + pmStats.getStat("CURHP") + "/" + pmStats.getStat("HP"),twwl.getX() + twwl.getWidth(),128);
			twwl.createLabel("PP",32,160);
			twwl.createLabel("" + pmStats.getStat("CURPP") + "/" + pmStats.getStat("PP"),twwl.getX() + twwl.getWidth(),160);
			twwl.createLabel("Current EXP",32,192);
			twwl.createLabel("" + pmStats.getStat("CURXP"),twwl.getX() + twwl.getWidth(),192);
			twwl.createLabel("EXP To Next Level",32,224); // needs calculation
			twwl.createLabel("inf",twwl.getX() + twwl.getWidth(),224); // needs calculation
			twwl.createLabel("Offense",twwl.getX() + twwl.getWidth()*2 - 96,32);
			twwl.createLabel("" + pmStats.getStat("ATK"),twwl.getX() + twwl.getWidth()*2,32);
			twwl.createLabel("Defense",twwl.getX()+ twwl.getWidth()*2 - 96,64);
			twwl.createLabel("" + pmStats.getStat("DEF"),twwl.getX() + twwl.getWidth()*2,64);
			twwl.createLabel("IQ",twwl.getX()+ twwl.getWidth()*2 - 96,96);
			twwl.createLabel("" + pmStats.getStat("IQ"),twwl.getX() + twwl.getWidth()*2,96);
			twwl.createLabel("Guts",twwl.getX()+ twwl.getWidth()*2 - 96,128);
			twwl.createLabel("" + pmStats.getStat("GUTS"),twwl.getX() + twwl.getWidth()*2,128);
			twwl.createLabel("Vitality",twwl.getX()+ twwl.getWidth()*2 - 96,160);
			twwl.createLabel("" + pmStats.getStat("VIT"),twwl.getX() + twwl.getWidth()*2,160);
			twwl.createLabel("Speed",twwl.getX()+ twwl.getWidth()*2 - 96,192);
			twwl.createLabel("" + pmStats.getStat("SPD"),twwl.getX() + twwl.getWidth()*2,192);
			twwl.createLabel("Luck",twwl.getX()+ twwl.getWidth()*2 - 96,224);
			twwl.createLabel("" + pmStats.getStat("LUCK"),twwl.getX() + twwl.getWidth()*2,224);
			twwl.createLabel("Push 'CONFIRM' to see learned PSI.",128,twwl.getY() + twwl.getHeight()*2);
			statuses.add(twwl);
		}
		invisSelectItem = new InvisibleMenuItem(state,party.size());
		addMenuItem(invisSelectItem);
	}
	
}
