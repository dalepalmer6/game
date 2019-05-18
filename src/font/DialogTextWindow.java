package font;

import org.lwjgl.opengl.GL11;

import actionmenu.equipmenu.TextLabel;
import canvas.MainWindow;
import menu.StartupNew;

public class DialogTextWindow extends TextWindowWithPrompt {
	private String name;
	private TextLabel textName;
	
	public DialogTextWindow(String s, StartupNew state) {
		super(s,128,792,24,2,state);
//		textName = new Text(name,x,y-160,0,0,state.charList);
	} 
	
	public DialogTextWindow(String s, String name, StartupNew state) {
		super(s,128,792,24,2,state);
		this.name = name;
		textName = new TextLabel(name,x+TEXT_START_X,y-52,state);
	} 
	
	public void drawBlackWindow(MainWindow m) {
		//start at x = 0, to screenwidth, at y
		for (int i = 0; i < width*4 + 2*TILE_SIZE; i+=TILE_SIZE) {
			for (int j = 0; j < height*4 + 2*TILE_SIZE; j+=TILE_SIZE) {
				m.renderTile(x+(i),y+(j),64,64,48,16,8,8);
			}
		}
	}
	
	public void drawNameWindow(MainWindow m) {
		m.renderTile(x,y-64,32,64,48,0,8,16);
		//draw as many of these as needed to fit the name!
		int i = 1;
		int widthOfNameBox = 0;
		while (widthOfNameBox < textName.getWidthOfText()*4) {
			m.renderTile(x+32*i,y-64,32,64,56,0,8,16);
			i++;
			widthOfNameBox += 32;
		}
//		m.renderTile(x+32,y-64,32,64,56,0,8,16);
		m.renderTile(x+32*i,y-64,32,64,69,0,8,16);
		textName.draw(m,255,255,255);
	}
	
	public void drawText(MainWindow m) {
		Text.initDrawText(m);
		text.draw(m,255,255,255);
	}
	
	public void draw(MainWindow m) {
		initDrawWindow(m);
		drawBlackWindow(m);
		if (name != null) {
			drawNameWindow(m);
		}
		drawText(m);
	}
	
}
