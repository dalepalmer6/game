package battlesystem.menu;

import java.util.ArrayList;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.actionmenu.goodsmenu.GoodsSelectMenuItem;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.SystemState;
import system.controller.InputController;

public class GoodsMenuInBattle extends InBattleWindow {
	
	public GoodsMenuInBattle(SystemState state, PartyMember p) {
		super(state);
		int width = 2;
		int height = 10;
		stw.createGrid(width,height,192,stw.getTextStartY()-8);
		stw.setYSelectionSize(4);
		stw.setSteps(512,48);
		this.pm = p;
		this.party = new ArrayList<PartyMember>();
		this.party.add(p);
		double x = stw.getX();
		double y = stw.getY();
		int i = 0;
			for (Item item : pm.getItemsList()) {
				if (i != 0 && i % 2 == 0) {
					y += 64;
					x = stw.getX();
				}
				stw.setCurrentOpen(x,y);
				x += 256;
				if (item.getId() == 0) {
					continue;
				}
				stw.add(new GoodsSelectMenuItem(item,0,this.state,this.party,false),i%2,i/2);
				i++;
//			descWindow = new TextWindow(true," ",256,128+11*64,20,1,state);
			//add a TexturedMenuItem that draws the item's picture
//			addMenuItem(descWindow);
			}
		addMenuItem(stw);
//		if (!state.inBattle) {
//			menuTitle.setText("Goods");
//		}
		
	}
	
	public void reloadActionOnPop() {
//		inventories = new ArrayList<SelectionTextWindow>();
		int j = 0;
		stw.clearSelections();
		stw.setCurrentOpen(stw.getTextStartX(),stw.getTextStartY());
//			stw.setTextStart(base.getTextStartX()+96,base.getTextStartY());
			stw.setSteps(680,0);
			stw.setDrawOnly(false);
			for (Item item : pm.getItemsList()) {
				if (item.getId() == 0) {
					continue;
				}
				stw.add(new GoodsSelectMenuItem(item,0,state,party,false));
			}
			
			addMenuItem(stw);
//		}
//		if (!state.inBattle) {
//			menuTitle.setText("Goods");
//		}
	}

}
