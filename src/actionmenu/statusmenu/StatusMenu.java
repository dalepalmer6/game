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
			twwl.createLabel("" + pmStats.getStat("LVL"),twwl.getX() + twwl.getWidth(),64);
			twwl.createLabel("Offense",32,128);
			twwl.createLabel("" + pmStats.getStat("ATK"),twwl.getX() + twwl.getWidth(),128);
			twwl.createLabel("Defense",32,192);
			twwl.createLabel("" + pmStats.getStat("DEF"),twwl.getX() + twwl.getWidth(),192);
			twwl.createLabel("IQ",32,256);
			twwl.createLabel("" + pmStats.getStat("IQ"),twwl.getX() + twwl.getWidth(),256);
			twwl.createLabel("Speed",32,320);
			twwl.createLabel("" + pmStats.getStat("SPD"),twwl.getX() + twwl.getWidth(),320);
			twwl.createLabel("Guts",32,384);
			twwl.createLabel("" + pmStats.getStat("GUTS"),twwl.getX() + twwl.getWidth(),384);
			twwl.createLabel("Vitality",32,448);
			twwl.createLabel("" + pmStats.getStat("VIT"),twwl.getX() + twwl.getWidth(),448);
			twwl.createLabel("Luck",32,512);
			twwl.createLabel("" + pmStats.getStat("LUCK"),twwl.getX() + twwl.getWidth(),512);
			twwl.createLabel("Current EXP",32,576);
			twwl.createLabel("" + pmStats.getStat("CURXP"),twwl.getX() + twwl.getWidth(),576);
			twwl.createLabel("EXP To Next Level",32,640); // needs calculation
			twwl.createLabel("inf",twwl.getX() + twwl.getWidth(),640); // needs calculation
			
			twwl.createLabel("HP",twwl.getX() + twwl.getWidth()*2 - 96,64);
			twwl.createLabel("" + pmStats.getStat("CURHP") + "/" + pmStats.getStat("HP"),twwl.getX() + twwl.getWidth()*3,64);
			twwl.createLabel("PP",twwl.getX() + twwl.getWidth()*2 - 96,128);
			twwl.createLabel("" + pmStats.getStat("CURPP") + "/" + pmStats.getStat("PP"),twwl.getX() + twwl.getWidth()*3,128);
			
			twwl.createLabel("Weapon",twwl.getX() + twwl.getWidth()*2 - 96,256);
			twwl.createLabel(pm.getEquips().get(0).getName(),twwl.getX() + twwl.getWidth()*3,256);
			twwl.createLabel("Body",twwl.getX() + twwl.getWidth()*2 - 96,320);
			twwl.createLabel(pm.getEquips().get(1).getName(),twwl.getX() + twwl.getWidth()*3,320);
			twwl.createLabel("Head",twwl.getX() + twwl.getWidth()*2 - 96,384);
			twwl.createLabel(pm.getEquips().get(2).getName(),twwl.getX() + twwl.getWidth()*3,384);
			twwl.createLabel("Other",twwl.getX() + twwl.getWidth()*2 - 96,448);
			twwl.createLabel(pm.getEquips().get(3).getName(),twwl.getX() + twwl.getWidth()*3,448);
			
			twwl.createLabel("Skills",twwl.getX() + twwl.getWidth()*2 - 96,576);
			twwl.createLabel("Skill Type",twwl.getX() + twwl.getWidth()*3,576);
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
