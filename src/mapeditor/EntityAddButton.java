package mapeditor;

import gamestate.Entity;
import gamestate.MainCharacterStub;
import gamestate.Ness;
import mapeditor.tools.SingleEntity;
import menu.LeftClickableItem;
import menu.MenuItem;
import menu.StartupNew;

public class EntityAddButton extends MenuItem {
	
	public EntityAddButton(String t, int x, int y, int w, int h, StartupNew m) {
		super(t, x, y,w,h, m);
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		//create an entity to test
		Entity e = new MainCharacterStub(state);
		((MapEditMenu) state.getMenuStack().peek()).getMapPreview().setTool(new SingleEntity(e));
		return null;
	}
	
}
