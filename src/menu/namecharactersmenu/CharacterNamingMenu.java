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
//		stw = new SelectionTextWindow("vertical",300,300,5,10,state);
//		int i = 0;
//		for (String name : state.namesOfCharacters) {
//			stw.add(createMenuItem(i++,name));
//		}
//		addMenuItem(stw);
	}
	
	public void update(InputController input) {
//		menuItems.remove(stw);
//		selectedIndex = stw.getSelectedIndex();
		addMenuItem(new MenuItem("test",0,0,state));
		if (inputtedName != null) {
			state.namesOfCharacters[index] = inputtedName;
			index++;
			inputtedName = null;
		}
//		stw.clearSelections();
////		stw.clearSelections();
//		int i = 0;
//		for (String name : state.namesOfCharacters) {
//			stw.add(createMenuItem(i++,name));
//		}
//		addMenuItem(stw);
		if (index >= state.defaultNames.length) {
			//create the starting textbox.
			IntroWindowMenu m = new IntroWindowMenu(state);
			state.getMenuStack().clear();
			state.getMenuStack().push(m);
			DialogTextWindow dtw = new DialogTextWindow("[_87C][_87D][_87E][_87F][_880][_881][STOPBGM][_882] ",150,150,15,15,state);
			m.addMenuItem(dtw);
			return;
		}
		CharacterNameInputMenu m = new CharacterNameInputMenu(state);
		m.create(index);
		state.getMenuStack().push(m);
	}
	
	public MenuItem createMenuItem(int index,String text) {
		MenuItem mi = new PromptForCharacterNameButton(index,text,curX,curY, state);
		return mi;
	}
	
}
