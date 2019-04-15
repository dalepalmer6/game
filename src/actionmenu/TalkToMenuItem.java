package actionmenu;

import font.SelectionTextWindow;
import menu.MenuItem;
import menu.StartupNew;

public class TalkToMenuItem extends MenuItem {
	private SelectionTextWindow stw;
	
	public TalkToMenuItem(StartupNew state, SelectionTextWindow stw) {
		super("Talk",0,0,0,0,state);
		this.stw = stw;
	}
	
	public String execute() {
		state.getMenuStack().peek().setToRemove(stw);
		state.getGameState().getPlayer().talkTo();
		return null;
	}
	
}
