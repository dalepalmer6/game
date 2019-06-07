package menu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.GoodsMenuOutOfBattle;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import font.DialogTextWindow;
import font.SelectionTextWindow;
import font.SimpleDialogMenu;
import font.TextWindowWithPrompt;
import gamestate.elements.items.Item;
import global.InputController;

public class ShopMenu extends Menu {
	private ArrayList<ShopMenuItem> saleItems;
	private SelectionTextWindow stw;
	private ArrayList<TextLabel> prices;
	private String shopName; 
	private String greeting = "Hello there![CHOOSE][CHOICE]Buy[TEXT][BUYWINDOW][CHOICE]Sell[TEXT][SELLWINDOW]";
	private String regreeting = "Is there anything else I can do for you?[CHOOSE][CHOICE]Buy[TEXT][BUYWINDOW][CHOICE]Sell[TEXT][SELLWINDOW]";
	private String sellText = "I see. [WAIT20][NEWLINE]I can probably give you %d for %s. [NEWLINE]Is that alright?[CHOOSE][CHOICE]Yes[TEXT]Ok, let's do this.[CONSUMEITEM_%d_%d][ADDMONEY_%d][CHOICE]No[TEXT]Oh, you changed your mind?";
	private boolean selling;
	private boolean skip;
	
	public void addBuyWindow() {
		menuItems.clear();
		addMenuItem(stw);
		for (TextLabel tl : prices) {
			addMenuItem(tl);
		}
	}
	
	public void addSellWindow() {
		menuItems.clear();
		//push the item menu, so that on a return, the item is staged for sale
		selling = true;
		GoodsMenuOutOfBattle m = new GoodsMenuOutOfBattle(state,state.getGameState().getPartyMembers(),true);
//		m.createMenu();
//		m.setSelling(true);
		state.getMenuStack().push(m);
		skip = true;
	}
	
	public ShopMenu(StartupNew state, String shopName) {
		super(state);
		this.shopName = shopName;
		addMenuItem(new DialogTextWindow(greeting,state));
	}
	
	public void reloadActionOnPop() {
		menuItems.clear();
		selling = false;
		addMenuItem(new DialogTextWindow(regreeting,state));
	}
	
	public void readFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/shops/" + shopName + ".csv"));
			String row;
			while ((row = br.readLine()) != null) {
				String[] ids = row.split(",");
				saleItems = new ArrayList<ShopMenuItem>();
				for (int i = 0; i < ids.length; i++) {
					saleItems.add(new ShopMenuItem(state.items.get(Integer.parseInt(ids[i])),state));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			SimpleDialogMenu.createDialogBox(state, "ERROR: FILE NOT FOUND, " + shopName + ".csv","System Error");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(InputController input) {
		super.update(input);
		if (selling) {
			Item item = ((GoodsSelectMenuItem) state.getSelectionStack().peek()).getItem();
			String out = String.format(sellText,item.getValue()/2,item.getName(),item.getId(),state.getPartyIndex(),item.getValue()/2);
			SimpleDialogMenu.createDialogBox(state,out);
		}
	}
	
	public void loadShopItems() {
		readFile();
		
		stw = new SelectionTextWindow(0,0,6,8,state);
		prices = new ArrayList<TextLabel>();
		for (ShopMenuItem i : saleItems) {
			prices.add(new TextLabel("$" + i.getItem().getValue() + "",stw.getX() + stw.getWidth()*4,stw.getCurrentOpenY(),state));
			stw.add(i);
		}
//		addMenuItem(stw);
//		for (TextLabel tl : prices) {
//			addMenuItem(tl);
//		}
	}
}
