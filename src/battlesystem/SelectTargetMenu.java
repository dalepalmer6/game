package battlesystem;

import java.util.ArrayList;

import actionmenu.goodsmenu.InvisibleMenuItem;
import battlesystem.options.EnemyOption;
import battlesystem.options.EnemyOptionPanel;
import font.TextWindow;
import gamestate.BattleEntity;
import gamestate.Enemy;
import global.InputController;
import menu.Menu;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class SelectTargetMenu extends Menu {
	private InvisibleMenuItem invisMenuItem;
	private TextWindow targetName;
	private ArrayList<BattleEntity> targets;
	private String onWho = "On Who?: ";
	private boolean all;
	private EnemyOptionPanel eop;
	private boolean createdFeather;
	private FeatherMenuItem tmi;
	
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
		targetName = new TextWindow(true,"Name",0,0,4,0,state);
		addMenuItem(targetName);
	}
	
	public void update(InputController input) {
		boolean onFoes = targets.get(0) instanceof Enemy;
		
		if (all) {
			targetName.setText("ALL");
			
			if (onFoes) {
				state.battleMenu.getEnemyOptionPanel().setSelected(-1);
				if (!createdFeather) {
					for (EnemyOption eo : state.battleMenu.getEnemyOptionPanel().getEnemyOptions()) {
						FeatherMenuItem tmi = new FeatherMenuItem("Enemy",eo.getX() + eo.getWidth()/2,eo.getY() + eo.getHeight()/2,64,64,state,"battlechoicefeather.png",0,0,16,16);
						addMenuItem(tmi);
						createdFeather = true;
						state.battleMenu.getCurrentAction().setTargets(targets,null,true);
					}
				}
			} else {
				if (!createdFeather) {
					for (PlayerStatusWindow psw : state.battleMenu.getPSWs()) {
						state.battleMenu.getCurrentAction().setTargets(targets,null,true);
						FeatherMenuItem tmi = new FeatherMenuItem("Party",psw.getX() + psw.getWidth()/2,psw.getY() + psw.getHeight()/2,64,64,state,"battlechoicefeather.png",0,0,16,16);
						addMenuItem(tmi);
						createdFeather = true;
					}
				}
			}
		} else {
			int index = invisMenuItem.getIndex();
			if (state.battleMenu.getEnemyOptionPanel().getSelected() != index) {
				createdFeather = false;
				menuItems.remove(tmi);
			}
			targetName.setText(targets.get(invisMenuItem.getIndex()).getName());
			if (onFoes) {
				state.battleMenu.getCurrentAction().setTargets(targets,targets.get(invisMenuItem.getIndex()),false);
				state.battleMenu.getEnemyOptionPanel().setSelected(index);
				if (!createdFeather) {
					EnemyOption eo = state.battleMenu.getEnemyOptionPanel().getEnemyOption(index);
					tmi = new FeatherMenuItem("Enemy",eo.getX() + eo.getWidth()/2,eo.getY() + eo.getHeight()/2,64,64,state,"battlechoicefeather.png",0,0,16,16);
					addMenuItem(tmi);
					createdFeather = true;
				}
			} else {
				state.battleMenu.getCurrentAction().setTargets(targets,targets.get(invisMenuItem.getIndex()),false);
				PlayerStatusWindow psw = state.battleMenu.getPSWs().get(index);
				tmi = new FeatherMenuItem("Party",psw.getX() + psw.getWidth()/2,psw.getY() + psw.getHeight()/2,64,64,state,"battlechoicefeather.png",0,0,16,16);
				addMenuItem(tmi);
				createdFeather = true;
			}
			
		}
		
		state.battleMenu.getEnemyOptionPanel().updateAnim();
		
		if (invisMenuItem.getCanLoadInventory()) {
				state.battleMenu.setDoneAction();
				state.getMenuStack().clear();
				state.getMenuStack().push(state.battleMenu);
		}

		
	}
	
}
