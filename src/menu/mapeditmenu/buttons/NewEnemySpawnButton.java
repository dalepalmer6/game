package menu.mapeditmenu.buttons;

import gamestate.entities.EnemySpawnEntity;
import gamestate.entities.Entity;
import menu.ButtonMenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class NewEnemySpawnButton extends ButtonMenuItem {

	//when clicking the button, bring up a big grid of all the enemies that can be added
	
	public NewEnemySpawnButton(int x, int y, int width, int height, SystemState m) {
		super("New Enemy Spawner", x, y, width, height, m);
	}
	
	public String execute() {
		state.setHoldable(false);
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		Entity e = new EnemySpawnEntity(0,0,0,32,32,state,"");
		e.setText("New Entity");
		m.getEntities().add(e);
		return null;
	}
	
}
