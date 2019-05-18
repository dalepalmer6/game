package actionmenu;
import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import canvas.MainWindow;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;

public class PauseMenuSelectionWindow extends SelectionTextWindow {
	ArrayList<PartyMember> party;
	int targetY;
	TextLabel moneyLabel;
	
	@Override
	public void add(MenuItem m, int x, int y) {
		//adds a MenuItem at a particular index, to create dynamic STW with different row/col lengths
		m.setX(currentOpenX);
		m.setY(currentOpenY);
		selections.get(y).add(x,m);
	}
	
	public PauseMenuSelectionWindow(StartupNew m, ArrayList<PartyMember> party) {
		super("horizontal",0,0,50,2,m);
		y = -128;
		targetY = 0;
		this.party = party;
		currentOpenX = 32;
		currentOpenY = 16;
		add(new GoodsMenuItem(state,party,-128),0,0);
		currentOpenX = 144;
		currentOpenY = 16;
		add(new EquipMenuItem(state,party,-128),1,0);
		currentOpenX = 256;
		currentOpenY = 16;
		add(new PSIMenuItem(state,party,-128),2,0);
		currentOpenX = 368;
		currentOpenY = 16;
		add(new StatusMenuItem(state,party,-128),3,0);
	}
	
	public void updateAnim() {
		super.updateAnim();
		for (ArrayList<MenuItem> list : selections) {
			for(MenuItem i : list) {
				i.setTargetPosY(24);
				i.approachTargetPos();
			} 
		}
		approachTargetPos();
	}
	
	public void draw(MainWindow m) {
		state.getMainWindow().setTexture("img\\menu.png");
		//draw the backing bar
		for (int i = 0; i < state.getMainWindow().getScreenWidth(); i+=TILE_SIZE) {
			state.getMainWindow().renderTile(i,y,128,128,0,0,32,32);
		}
		//draw all the menuitems
		drawSelections(m);
		//draw the text field that shows which option you are on
	} 
	
}
