package actionmenu.statusmenu;

import java.util.ArrayList;

import actionmenu.PlayerInfoWindow;
import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import actionmenu.goodsmenu.InvisibleMenuItem;
import canvas.Controllable;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.EntityStats;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class StatusMenu extends PlayerInfoWindow {
	private ArrayList<PartyMember> party;
	private int index;
	private InvisibleMenuItem invisSelectItem;
	private ArrayList<TextWindowWithLabels> statuses;
	private TextWindow confirmAction;
	
	public StatusMenu(StartupNew m, ArrayList<PartyMember> party) {
		super(m,party);
		this.party = party;
	}
	
//	public void update(InputController input) {
//		this.setToRemove(statuses.get(index));
//		index = invisSelectItem.getIndex();
//		this.addToMenuItems(statuses.get(index));
//		if (invisSelectItem.getCanLoadInventory()) {
//			GoodsMenu gm = new GoodsMenu(state,party,index);
//			gm.createMenu();
//			state.getMenuStack().push(gm);
//		}
//	}
	
	public void createMenu() {
		//hardcoded menu here
		TextWindowWithLabels twwl=null;
		menuTitle.setText("Status");
		statuses = new ArrayList<TextWindowWithLabels>();
		for (int i = 0; i < party.size(); i++) {
			PartyMember pm = party.get(i);
			twwl = new TextWindowWithLabels(state);
			EntityStats pmStats = pm.getStats();
//			twwl.setDrawOnly(true);
			twwl.createLabel(pm.getName(),32,16);
			twwl.createLabel("Level",32,64);
			twwl.createLabel("" + pmStats.getStat("LVL"),(int)twwl.getX() + twwl.getWidth(),64);
			twwl.createLabel("Offense",32,128);
			twwl.createLabel("" + pmStats.getStat("ATK"),(int)twwl.getX() + twwl.getWidth(),128);
			twwl.createLabel("Defense",32,192);
			twwl.createLabel("" + pmStats.getStat("DEF"),(int)twwl.getX() + twwl.getWidth(),192);
			twwl.createLabel("IQ",32,256);
			twwl.createLabel("" + pmStats.getStat("IQ"),(int)twwl.getX() + twwl.getWidth(),256);
			twwl.createLabel("Speed",32,320);
			twwl.createLabel("" + pmStats.getStat("SPD"),(int)twwl.getX() + twwl.getWidth(),320);
			twwl.createLabel("Guts",32,384);
			twwl.createLabel("" + pmStats.getStat("GUTS"),(int)twwl.getX() + twwl.getWidth(),384);
			twwl.createLabel("Vitality",32,448);
			twwl.createLabel("" + pmStats.getStat("VIT"),(int)twwl.getX() + twwl.getWidth(),448);
			twwl.createLabel("Luck",32,512);
			twwl.createLabel("" + pmStats.getStat("LUCK"),(int)twwl.getX() + twwl.getWidth(),512);
			twwl.createLabel("Current EXP",32,576);
			twwl.createLabel("" + pmStats.getStat("CURXP"),(int)twwl.getX() + twwl.getWidth(),576);
			twwl.createLabel("EXP To Next Level",32,640); // needs calculation
			twwl.createLabel("inf",(int)twwl.getX() + twwl.getWidth(),640); // needs calculation
			
			twwl.createLabel("HP",(int)twwl.getX() + twwl.getWidth()*2 - 96,64);
			twwl.createLabel("" + pmStats.getStat("CURHP") + "/" + pmStats.getStat("HP"),(int)twwl.getX() + twwl.getWidth()*3,64);
			twwl.createLabel("PP",(int)twwl.getX() + twwl.getWidth()*2 - 96,128);
			twwl.createLabel("" + pmStats.getStat("CURPP") + "/" + pmStats.getStat("PP"),(int)twwl.getX() + twwl.getWidth()*3,128);
			
			twwl.createLabel("Weapon",(int)twwl.getX() + twwl.getWidth()*2 - 96,256);
			twwl.createLabel(pm.getEquips().get(0).getName(),(int)twwl.getX() + twwl.getWidth()*3 - 256,256);
			twwl.createLabel("Body",(int)twwl.getX() + twwl.getWidth()*2 - 96,320);
			twwl.createLabel(pm.getEquips().get(1).getName(),(int)twwl.getX() + twwl.getWidth()*3 - 256,320);
			twwl.createLabel("Head",(int)twwl.getX() + twwl.getWidth()*2 - 96,384);
			twwl.createLabel(pm.getEquips().get(2).getName(),(int)twwl.getX() + twwl.getWidth()*3 - 256,384);
			twwl.createLabel("Other",(int)twwl.getX() + twwl.getWidth()*2 - 96,448);
			twwl.createLabel(pm.getEquips().get(3).getName(),(int)twwl.getX() + twwl.getWidth()*3 - 256,448);
			
			twwl.createLabel("Skills",(int)twwl.getX() + twwl.getWidth()*2 - 96,576);
			twwl.createLabel("Skill Type",(int)twwl.getX() + twwl.getWidth()*3,576);
			statuses.add(twwl);
		}
		confirmAction = new TextWindow(true,"Push 'CONFIRM' to see learned PSI.",256,128+11*64,20,1,state);
		addMenuItem(confirmAction);
		invisSelectItem = new InvisibleMenuItem(state,party.size());
		addMenuItem(invisSelectItem);
	}
	
	public void update(InputController input) {
		super.update(input);
//		if (!backShouldExit && input.getSignals().get("BACK")) {
//			statuses.get(index).setDrawOnly(true);
//			backShouldExit = true;
//			menuItems.add(invisSelectItem);
//		}
//		
		index = invisSelectItem.getIndex();
//		confirmAction.setText(((GoodsSelectMenuItem) statuses.get(index).getSelectedItem()).getItem().getDescription());
		
		menuItems.removeAll(statuses);
		this.addToMenuItems(statuses.get(index));
		
		if (invisSelectItem.getCanLoadInventory()) {
			invisSelectItem.setCanLoadInventory(false);
//			inventories.get(index).setDrawOnly(false);
			backShouldExit = false;
			menuItems.remove(invisSelectItem);
		}
	}
	
}
