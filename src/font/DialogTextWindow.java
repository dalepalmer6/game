package font;

import org.lwjgl.opengl.GL11;

import actionmenu.equipmenu.TextLabel;
import canvas.MainWindow;
import menu.StartupNew;

public class DialogTextWindow extends TextWindowWithPrompt {
	private String name;
	private TextLabel textName;
	private String textString;
	private boolean textInitted;
	private boolean closing;
	
	public DialogTextWindow(String s, int x, int y, int width, int height, StartupNew state) {
		super(s,x,y,width,height,state);
		targetY = y;
		textString = s;
	}
	
	public DialogTextWindow(String s, StartupNew state) {
		super(s,0,792,29,3,state);
		y = state.getMainWindow().getScreenHeight();
		targetY = 792;
		textString = s;
//		textName = new Text(name,x,y-160,0,0,state.charList);
	} 
	
	public DialogTextWindow(String s, String name, StartupNew state) {
		super(s,0,792,29,3,state);
		y = 1080;//state.getMainWindow().getScreenHeight();
		targetY = 792;
		this.name = name;
		textString = s;
	} 
	
	public void approachTargetPos() {
		// TODO Auto-generated method stub
		if (targetY > y) {
			y+=32;
//			if (textInitted) {
//				text.setY(text.getY()+8);
//				textName.setY(text.getY()+8);
//			}
			
		} else if (targetY < y) {
			y-=32;
//			if (textInitted) {
//				text.setY(text.getY()-8);
//				textName.setY(text.getY()-8);
//			}
		}
	}
	
	public void next() {
		text.setFreeze(false);
		if (text.getDrawState()) {
			closing = true;
		}
	}
	
	public void updateAnim() {
		approachTargetPos();
		if (targetY == y && !textInitted) {
			textInitted = true;
			textName = new TextLabel(name,(int)x+24,(int)y-52,state);
			this.text = new Text(shouldDrawAll,textString,(int)x+TEXT_START_X,(int)y+TEXT_START_Y,this.width,this.height,m.charList);
			this.text.setRenderWindow(this);
			text.setState(state);
		}
		if (textInitted) {
			super.updateAnim();
		}
		if (closing) {
			targetY = state.getMainWindow().getScreenHeight();
			state.getMenuStack().peek().setToRemove(textName);
		}
		if (closing && targetY == y) {
			state.getMenuStack().pop();
		}
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
		while (widthOfNameBox < textName.getWidthOfText()) {
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
		if (name != null && textInitted) {
			drawNameWindow(m);
		}
		if (textInitted) {
			drawText(m);
		}
		
	}
	
}
