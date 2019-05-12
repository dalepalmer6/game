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

public class TextWindow extends MenuItem implements Drawable{
	public Text text;
	private double tickCount;
	private double ticksPerFrame = 0.5;
	protected int TEXT_START_X = 24;
	protected int TEXT_START_Y = 24;
	protected long createdAt;
	protected long lastTime;
	protected long now;
	private int TILE_SIZE = 16;
	protected int width;
	private int height;
	protected int x;
	protected int y;
	protected StartupNew m;
	protected boolean shouldDrawAll;
	
	public void setIgnoreCodes() {
		text.setIgnoreCodes();
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
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
		this.tickCount = 0;
		this.x = x;
		this.y = y;
		this.width = width * TILE_SIZE;
		this.height = height * TILE_SIZE;
		this.text = new Text(shouldDrawAll,s,x+TEXT_START_X,y+TEXT_START_Y,this.width,this.height,m.charList);
		this.text.setRenderWindow(this);
		this.text.setState(state);
		this.m = m;
		this.shouldDrawAll = shouldDrawAll;
	}
	
	public void drawWindow(MainWindow m) {
		initDrawWindow(m);
		int tileWidth =  width/TILE_SIZE;
		int tileHeight = height/TILE_SIZE;
		int xCoord = 0;
		int yCoord = 0;
		int xPos = x;
		int yPos = y;
		//draw top left corner
		m.renderTile(xPos,yPos,
				64,64,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
		xCoord += TILE_SIZE;
		for (int i = 0; i < tileWidth; i++) {
			xPos+=64;
			m.renderTile(xPos,yPos,
						64,64,
						xCoord,yCoord,
						TILE_SIZE,TILE_SIZE);
		}
		xPos += 64;
		xCoord += TILE_SIZE;
		m.renderTile(xPos,yPos,
				64,64,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
		for (int i = 0; i < tileHeight; i++) {
			xPos = x;
			yPos += 64;
			xCoord = 0;
			yCoord = TILE_SIZE;
			m.renderTile(xPos,yPos,
					64,64,
					xCoord,yCoord,
					TILE_SIZE,TILE_SIZE);
			xCoord += TILE_SIZE;
			for (int j = 0; j < tileWidth; j++) {
				//draw the centers
				xPos += 64;
				m.renderTile(xPos,yPos,
						64,64,
						xCoord,yCoord,
						TILE_SIZE,TILE_SIZE);
			}
			xPos += 64;
			xCoord +=TILE_SIZE;
			m.renderTile(xPos,yPos,
					64,64,
					xCoord,yCoord,
					TILE_SIZE,TILE_SIZE);
			
		}
		xCoord = 0;
		yCoord = 2*TILE_SIZE;
		xPos = x;
		yPos += 64;
		//draw top left corner
		m.renderTile(xPos,yPos,
				64,64,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
		xCoord += TILE_SIZE;
		for (int i = 0; i < tileWidth; i++) {
			xPos+=64;
			m.renderTile(xPos,yPos,
						64,64,
						xCoord,yCoord,
						TILE_SIZE,TILE_SIZE);
		}
		xPos += 64;
		xCoord += TILE_SIZE;
		m.renderTile(xPos,yPos,
				64,64,
				xCoord,yCoord,
				TILE_SIZE,TILE_SIZE);
	}
	
	public void updateAnim() {
		text.update();
		tickCount += ticksPerFrame;
		if (!shouldDrawAll && !text.getDoneState() && tickCount % 2 == 0) {
			state.setSFX("text.wav");
			state.playSFX();
		}
		
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
		m.setTexture("img\\window.png");
//		m.bindTexture();
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	public void next() {
		//if there is more text, move the text up, and print until the next line or whatever the case may be
		//for now, just drop the box.
		text.setFreeze(false);
		if (text.getDrawState()) {
			state.getMenuStack().peek().setToRemove(this);
		}
//		
	}
}
