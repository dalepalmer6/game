package menu.namecharactersmenu;

import canvas.Drawable;
import font.DialogTextWindow;
import font.SelectionTextWindow;
import global.InputController;
import menu.BackButton;
import menu.IntroWindowMenu;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class CharacterNamingMenu extends Menu{
	private int curX = 10;
	private int curY = 10;
	private SelectionTextWindow stw;
	private int selectedIndex;
	private String inputtedName;
	private int index;
	private int changeIndex=0;
	
	public void setInput(String s) {
		inputtedName = s;
	}
	
	public CharacterNamingMenu(StartupNew m) {
		super(m);
		state.namesOfCharacters = new String[state.defaultNames.length];
		int j = 0;
		for (String name : state.defaultNames) {
			state.namesOfCharacters[j++] = name;
		}
		index = 0;
	}
	
	public void increaseIndex(int i) {
		// TODO Auto-generated method stub
		changeIndex = i;
	}
	
	public void reloadActionOnPop() {
		
	}
	
	public void update(InputController input) {
		index+=changeIndex;
		changeIndex = -1;
		if (index < 0) {
			index = 0;
		}
		addMenuItem(new MenuItem("test",0,0,state));
		if (inputtedName != null && index < state.defaultNames.length) {
			state.namesOfCharacters[index] = inputtedName;
			inputtedName = null;
		}
		if (index >= state.defaultNames.length) {
			//create the starting textbox.
			IntroWindowMenu m = new IntroWindowMenu(state);
			state.getMenuStack().clear();
			state.getMenuStack().push(m);
			DialogTextWindow dtw = new DialogTextWindow("[_87C][_87D][_87E][_87F][_880][_881][STOPBGM][_882] ",150,150,22,15,state);
			m.addMenuItem(dtw);
			return;
		}
		CharacterNameInputMenu m = new CharacterNameInputMenu(state,index);
		m.create();
		state.getMenuStack().push(m);
	}
	
	public MenuItem createMenuItem(int index,String text) {
		MenuItem mi = new PromptForCharacterNameButton(index,text,curX,curY, state);
		return mi;
	}
	
}
