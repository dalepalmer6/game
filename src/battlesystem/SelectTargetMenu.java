package battlesystem;

import java.util.ArrayList;

import actionmenu.goodsmenu.InvisibleMenuItem;
import font.TextWindow;
import gamestate.BattleEntity;
import gamestate.Enemy;
import menu.Menu;
import menu.StartupNew;

public class SelectTargetMenu extends Menu {
	private InvisibleMenuItem invisMenuItem;
	private TextWindow targetName;
	private ArrayList<BattleEntity> targets;
	private String onWho = "On Who?: ";
	private boolean all;
	
	public SelectTargetMenu(StartupNew state, boolean all, ArrayList<BattleEntity> arrayList) {
		super(state);
		int size = 0;
		if (!all) {
			size = arrayList.size();
		} else if (all) {
			size = 1;
		}
		this.all = all;
		invisMenuItem = new InvisibleMenuItem(state,size);
		addMenuItem(invisMenuItem);
		targets = arrayList;
		targetName = new TextWindow(true,onWho + "Name",0,0,4,0,state);
		addMenuItem(targetName);
	}
	
	public void update() {
		if (all) {
			targetName.setText(onWho + "ALL");
			state.battleMenu.getCurrentAction().setTargets(targets,null,true);
		} else {
			targetName.setText(onWho + targets.get(invisMenuItem.getIndex()).getName());
			state.battleMenu.getCurrentAction().setTargets(targets,targets.get(invisMenuItem.getIndex()),false);
		}
		
			if (invisMenuItem.getCanLoadInventory()) {
				state.battleMenu.setDoneAction();
				state.getMenuStack().clear();
				state.getMenuStack().push(state.battleMenu);
			}

		
	}
	
}
