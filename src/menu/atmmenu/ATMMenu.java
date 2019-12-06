package menu.atmmenu;

import menu.Menu;
import menu.MenuItem;
import menu.actionmenu.statusmenu.TextWindowWithLabels;
import menu.windows.DialogTextWindow;
import system.SystemState;
import system.controller.InputController;

public class ATMMenu extends Menu {
	
	public ATMMenu(SystemState state) {
		super(state);
	}
	
	public void update(InputController input) {
		MenuItem numScroller = null;
		for (MenuItem i : menuItems) {
			if (i instanceof DigitScroller) {
				numScroller = i;
			}
		}
		if (numScroller != null) {
			numScroller.setX(128);
			numScroller.setY(24+64);
			menuItems.remove(numScroller);
			addMenuItem(numScroller);
		}
	}
	
	public void createDeposit() {
		DialogTextWindow dtw = new DialogTextWindow("[NUMSCROLL_5][HAVEFUNDSONHAND_][DEPOSITFUNDS_]Depositing $[WINDOWARG].[WAIT20]Thank you![ELSE]You dont have the funds for that.[ENDIF]",state);
		addMenuItem(dtw);
		TextWindowWithLabels twwl = new TextWindowWithLabels(state,0,0,6,4);
		twwl.createLabel("Balance",24,24);
		twwl.createLabel("Deposit",24,24+64);
		twwl.createLabel("On Hand",24,24+128);
		addMenuItem(twwl);
		
	}
	
	public void createWithdraw() {
		DialogTextWindow dtw = new DialogTextWindow("[NUMSCROLL_5][HAVEFUNDSINBANK_][WITHDRAWFUNDS_]Alright, debiting your account for $[WINDOWARG].[WAIT20]Thank you![ELSE]You dont have the funds for that.[ENDIF]",state);
		addMenuItem(dtw);
		TextWindowWithLabels twwl = new TextWindowWithLabels(state,0,0,6,4);
		twwl.createLabel("Balance",24,24);
		twwl.createLabel("Deposit",24,24+64);
		twwl.createLabel("On Hand",24,24+128);
		addMenuItem(twwl);
	}
	
}
