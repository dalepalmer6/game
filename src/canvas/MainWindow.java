package canvas;

import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class MainWindow {
	private Texture texture;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;

	public MainWindow(int sw, int sh) { 
		SCREEN_WIDTH = sw;
		SCREEN_HEIGHT = sh;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public int getScreenWidth() {
		return SCREEN_WIDTH;
	}
	
	public int getScreenHeight() {
		return SCREEN_HEIGHT;
	}

	public void setTexture(String pathToFile) {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pathToFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void renderBG(String bgImg) {
		setTexture("img/" +  bgImg);
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int TILE_SIZE = 100;
		for (int i = 0; i < SCREEN_WIDTH; i+=TILE_SIZE) {
			for (int j = 0; j < SCREEN_HEIGHT; j+=TILE_SIZE) {
				GL11.glPushMatrix();
			        GL11.glBegin(GL11.GL_QUADS);
						GL11.glTexCoord2f(0, 0);
						GL11.glVertex2f(i, j);
						GL11.glTexCoord2f(1, 0);
						GL11.glVertex2f(i + TILE_SIZE, j);
						GL11.glTexCoord2f(1, 1);
						GL11.glVertex2f(i + TILE_SIZE,j + TILE_SIZE);
						GL11.glTexCoord2f(0, 1);
						GL11.glVertex2f(i,j + TILE_SIZE);
			        GL11.glEnd();
		        GL11.glPopMatrix();
			}
		}
		
		
		
	}
	
	public void render(int x, int y, int width, int height) {
		//draw with no texture
		Color.white.bind();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
        GL11.glPushMatrix();
            GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(x, y);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex2f(x + width, y);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex2f(x + width, y + height);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex2f(x, y + height);
            GL11.glEnd();
        GL11.glPopMatrix();
	}
	
	public void render(String textureName, String text, int x, int y, int width, int height) {
		//render with text (buttons)
		setTexture("img/" + textureName);
		texture.bind(); // or GL11.glBind(texture.getTextureID());
        // draw quad
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
            GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0f, 0f);
				GL11.glVertex2f(x, y);
				GL11.glTexCoord2f(1f, 0f);
				GL11.glVertex2f(x + width, y);
				GL11.glTexCoord2f(1f, 1f);
				GL11.glVertex2f(x + width, y + height);
				GL11.glTexCoord2f(0f, 1f);
				GL11.glVertex2f(x, y + height);
            GL11.glEnd();
        GL11.glPopMatrix();
        
//        Color.white.bind();
//		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
//		TrueTypeFont font = new TrueTypeFont(awtFont, true);
//		font.drawString(x, y + height/2 - 25, text, Color.white);
	}
	
	public void bindTexture() {
		texture.bind(); // or GL11.glBind(texture.getTextureID());
	}
	
	public void renderTile(int x, int y, int width, int height, float dx, float dy, float dw, float dh) {
        GL11.glPushMatrix();
        	float xo = dx/texture.getImageWidth();
        	float xw = (dx+dw)/texture.getImageWidth();
        	float yo = dy/texture.getImageHeight();
        	float yw = (dy+dh)/texture.getImageHeight();
            GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(xo,yo);
				GL11.glVertex2f(x, y);
				GL11.glTexCoord2f(xw, yo);
				GL11.glVertex2f(x + width, y);
				GL11.glTexCoord2f(xw,yw);
				GL11.glVertex2f(x + width, y + height);
				GL11.glTexCoord2f(xo,yw);
				GL11.glVertex2f(x, y + height);
            GL11.glEnd();
        GL11.glPopMatrix();

	}
	
	public void render(String textureName, int x, int y, int width, int height) {
		setTexture("img/" + textureName);
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
            GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(x, y);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex2f(x + width, y);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex2f(x + width, y + height);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex2f(x, y + height);
            GL11.glEnd();
        GL11.glPopMatrix();

	}

	private void initGL() {
		try {
			Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
//		GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public void start() {
		initGL();
	}

	public void drawDrawables(List<Drawable> drawables) {
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		// draws all the objects that need to be drawn
		for (Drawable d : drawables) {
			d.draw(this);
		}
	}
	
//	public void drawDrawables() {
//		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
////		render(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
//		Point mouse = GlobalVars.mainWindow.getMouseCoordinates();
//		// draws all the objects that need to be drawn
//		for (Drawable d : GlobalVars.getDrawables()) {
//			d.draw();
//			if (d instanceof Hoverable) {
//				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
//					((Hoverable) d).hoveredAction();
//					if (d instanceof Clickable) {
//						if (mouseLeft()) {
//							((Clickable) d).execute();
//						}
//					}
//				} else {
//					((Hoverable) d).unhoveredAction();
//				}
//			}
//		}
//	}
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = SCREEN_HEIGHT - Mouse.getY();
		return new Point(x, y);
	}

}
