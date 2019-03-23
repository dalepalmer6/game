package menu.namecharactersmenu;

import canvas.Drawable;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;
import menu.TextBox;

public class CharacterNamingMenu extends Menu{
	private int curX = 10;
	private int curY = 10;
	
	public CharacterNamingMenu(StartupNew m) {
		super(m);
	}
	
	@Override
	public MenuItem createMenuItem(String text) {
		MenuItem mi = new PromptForCharacterNameButton(text,curX,curY, state);
//		updateXY(mi.getWidth(), mi.getHeight()); 
//		numButtons++;
		return mi;
	}
	
//	public void updateXY(int width, int height) {
//		curX += width;
//		if (curX + width > state.getMainWindow().getScreenWidth()) {
//			curX = 10;
//			updateCurY(height);
//		} 
//	}
//	
//	public void updateCurY(int height) {
//		curY += height;
//		if (curY + height > state.getMainWindow().getScreenHeight()) {
//			curX = 10;
//			curY = 10;
//			System.err.println("Too many items on screen. Need new page.");
//			//needs logic if y overflows as well, create another page
//		} 
//	}
}
