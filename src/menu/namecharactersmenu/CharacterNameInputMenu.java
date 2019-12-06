package menu.namecharactersmenu;

import gamestate.entities.Entity;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.SubmitButton;
import menu.TexturedMenuItem;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.SystemState;
import system.controller.InputController;

public class CharacterNameInputMenu extends Menu {
//	private String input = "";
	private SelectionTextWindow STW;		
	private TextWindow TW;
	private int index;
	
	public void onPop() {
		if (input.length() > 0) {
			input = input.substring(0,input.length()-1);
		}
		else {
			super.onPop();
		}
	}
	
	public void setInput(String s) {
		input =s;
	}
	
	public void appendInput(String s) {
		input +=s;
	}
	
	public String getInput() {
		return input;
	}
	public CharacterNameInputMenu(SystemState m, int index) {
		super(m);
		this.index = index;
		backShouldExit = false;
	}
	
	public void update(InputController inputC) {
		TW.setText(input);
		if (input.equals("")) {
			backShouldExit = true;
		}
	}
	
	public void create() {
//		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String entityId = "";
		switch(index) {
			case 0: entityId = SystemState.Characters.NINTEN.getId(); break;
			case 1: entityId = SystemState.Characters.ANA.getId(); break;
			case 2: entityId = SystemState.Characters.LOID.getId(); break;
			case 3: entityId = SystemState.Characters.TEDDY.getId(); break;
			case 4: entityId = SystemState.Characters.NINTEN.getId(); break;
		}
		Entity e = state.getEntityFromEnum(entityId).createCopy(0,0,16*4,24*4,"entity");
		//int x, int y, int width, int height,  SystemState state, String texture, int dx, int dy, int dw, int dh
		TexturedMenuItem tmi = new TexturedMenuItem("",150+6*64,0,24*4,32*4,state,entityId + ".png",0,0,24,32);
		addMenuItem(tmi);
		String alphabet = "ABCDEabcde01234FGHIJfghij56789KLMNOklmno* \"@@PQRSTpqrst\"\"'=/UVWXYuvwxy+- $ Z()  z!?., :;  ";
		SelectionTextWindow STW = new NameInputSelectionTextWindow("horizontal",150,300,25,8,state);		
//		STW.createGrid(15,7);
		STW.setSteps(64,0);
		TextWindow TW = new TextWindow(true,input,1402-36,300-152,6,1,state); 
		TextWindow descriptionTW = new TextWindow(true,state.characterNamingStrings[index],150,300-152,6,1,state);
//		int x = STW.getTextStartX();
		int x = 0;
		int y = 0;
		int i = 0;
		for (char key : alphabet.toCharArray()) {
			i++;
			STW.setSteps(80,0);
			if (i % 5 == 0) {
				STW.setSteps(288,0);
			}
			
//			STW.setCurrentOpen(x,y);
			STW.add(createMenuItem(key,x,y));
			x += 64;
//			if (x > 32 * 16) {
//				y += 32;
//				x = STW.getTextStartX();
//			}
		}
		STW.setSteps(80,0);
		STW.add(new DontCareButton(state,index));
		STW.add(null);
		STW.add(null);
		STW.add(new BackButton(state));
		for (int j = 0; j < 10; j++) {
			STW.setSteps(80,0);
			if ((j+1) % 5 == 0) {
				STW.setSteps(288,0);
			}
			STW.add(null);
		}
		STW.setSteps(80,0);
		STW.add(new SubmitButton(state));
		addMenuItem(TW);
		addMenuItem(STW);
		addMenuItem(descriptionTW);
		
		this.TW = TW;
		this.STW = STW;
	}
	
	public MenuItem createMenuItem(char c, int x , int y) {
		MenuItem mi = new CharacterInputButton(c,x,y,state);
		return mi;
	}
}
