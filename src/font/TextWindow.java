package font;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import global.InputController;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class TextWindow extends MenuItem implements Drawable, Controllable{
	public Text text;
	protected int TEXT_START_X = 16;
	protected int TEXT_START_Y = 16;
	private long createdAt;
	private long lastTime;
	private long now;
	private int TILE_SIZE = 16;
	private int width;
	private int height;
	protected int x;
	protected int y;
	protected StartupNew m;
	private boolean shouldDrawAll;
	
	public TextWindow(String s, int x, int y, StartupNew m) {
		this(false,s,x,y,m);
	}
	
	public void setText(String s) {
		text.setText(s);
	}
	
	public TextWindow(boolean shouldDrawAll, String s, int x, int y, StartupNew m) {
		this(shouldDrawAll,s,x,y,15,6,m);
	}
	
	public TextWindow(boolean shouldDrawAll, String s, int x, int y, int width, int height, StartupNew m) {
		super("",x,y,m);
		this.createdAt = m.now;
		this.now = createdAt;
		this.x = x;
		this.y = y;
		this.width = width * TILE_SIZE;
		this.height = height * TILE_SIZE;
		this.text = new Text(shouldDrawAll,s,x+TEXT_START_X,y+TEXT_START_Y,this.width+8,this.height,m.charList);
		this.m = m;
		this.shouldDrawAll = shouldDrawAll;
	}
	
	public void drawWindow(MainWindow m) {
		initDrawWindow(m);
		int tileWidth =  width/TILE_SIZE - 2;
		int tileHeight = height/TILE_SIZE - 2;
		int xCoord = 0;
		int yCoord = 0;
		int xPos = x;
		int yPos = y;
		//draw top left corner
		m.renderTile(xPos,yPos,
				32,32,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
		xCoord += TILE_SIZE;
		for (int i = 0; i < tileWidth; i++) {
			xPos+=32;
			m.renderTile(xPos,yPos,
						32,32,
						xCoord,yCoord,
						TILE_SIZE,TILE_SIZE);
		}
		xPos += 32;
		xCoord += TILE_SIZE;
		m.renderTile(xPos,yPos,
				32,32,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
		for (int i = 0; i < tileHeight; i++) {
			xPos = x;
			yPos += 32;
			xCoord = 0;
			yCoord = TILE_SIZE;
			m.renderTile(xPos,yPos,
					32,32,
					xCoord,yCoord,
					TILE_SIZE,TILE_SIZE);
			xCoord += TILE_SIZE;
			for (int j = 0; j < tileWidth; j++) {
				//draw the centers
				xPos += 32;
				m.renderTile(xPos,yPos,
						32,32,
						xCoord,yCoord,
						TILE_SIZE,TILE_SIZE);
			}
			xPos += 32;
			xCoord +=TILE_SIZE;
			m.renderTile(xPos,yPos,
					32,32,
					xCoord,yCoord,
					TILE_SIZE,TILE_SIZE);
			
		}
		xCoord = 0;
		yCoord = 2*TILE_SIZE;
		xPos = x;
		yPos += 2*TILE_SIZE;
		//draw top left corner
		m.renderTile(xPos,yPos,
				32,32,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
		xCoord += TILE_SIZE;
		for (int i = 0; i < tileWidth; i++) {
			xPos+=32;
			m.renderTile(xPos,yPos,
						32,32,
						xCoord,yCoord,
						TILE_SIZE,TILE_SIZE);
		}
		xPos += 32;
		xCoord += TILE_SIZE;
		m.renderTile(xPos,yPos,
				32,32,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
	}
	
	public void drawText(MainWindow m) {
		Text.initDrawText(m);
		text.draw(m);
	}
	
	@Override//renderTile(int x, int y, int width, int height, float dx, float dy, float dw, float dh) {
	public void draw(MainWindow m) {
		// TODO Auto-generated method stub
		drawWindow(m);
		drawText(m);
	}
	
	public static void initDrawWindow(MainWindow m) {
		m.setTexture("img/window.png");
//		m.bindTexture();
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		if (!shouldDrawAll) {
			this.lastTime = this.now;
			this.now = System.nanoTime();
//			long delta = (long) ((this.now - this.lastTime)/(1000000000.0/60));
			long delta = (long) ((now - createdAt)/(1000000000.0/60.0));
			if (delta % 2 == 0) this.text.incrementDrawUntil();
			
			System.out.println(delta);
		}
		
		if (input.getSignals().get("CONFIRM")) {
			next();
		}
	}
	
	public void next() {
		//if there is more text, move the text up, and print until the next line or whatever the case may be
		//for now, just drop the box.
		state.getMenuStack().pop();
	}
}