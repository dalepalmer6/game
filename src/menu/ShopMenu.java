package menu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import actionmenu.goodsmenu.GoodsSelectMenuItem;
import font.SelectionTextWindow;
import font.SimpleDialogMenu;

public class ShopMenu extends Menu {
	private ArrayList<ShopMenuItem> saleItems;
	private SelectionTextWindow stw;
	private ArrayList<TextLabel> prices;
	private String shopName; 
	
	public ShopMenu(StartupNew state, String shopName) {
		super(state);
		this.shopName = shopName;
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
			SimpleDialogMenu.createDialogBox(state, "ERROR: FILE NOT FOUND, " + shopName + ".csv");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		addMenuItem(stw);
		for (TextLabel tl : prices) {
			addMenuItem(tl);
		}
	}
}
