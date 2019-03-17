package canvas;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;

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

import global.GlobalVars;

public class MainWindow {
	private Texture texture;
	private int SCREEN_WIDTH = 1280;
	private int SCREEN_HEIGHT = 720;

	
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
		setTexture("img/" + bgImg);
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(1280, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(1280, 720);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, 720);
//		GL11.glVertex2f(SCREEN_WIDTH, 0);
//		GL11.glTexCoord2f(1, 1);
//		GL11.glVertex2f(SCREEN_WIDTH, SCREEN_HEIGHT);
//		GL11.glTexCoord2f(0, 1);
//		GL11.glVertex2f(0, SCREEN_HEIGHT);
		GL11.glEnd();
	}
	
	public void render(int x, int y, int width, int height) {
		//draw with no texture
		Color.white.bind();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
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
	}
	
	public void render(String textureName, String text, int x, int y, int width, int height) {
		//render with text (buttons)
		setTexture("img/" + textureName);
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
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
		
		Color.white.bind();
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
		TrueTypeFont font = new TrueTypeFont(awtFont, true);
		font.drawString(x, y + height/2 - 25, text, Color.white);
	}
	
	public void render(String textureName, int x, int y, int width, int height) {
//		Color.white.bind();
		setTexture("img/" + textureName);
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
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
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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

	public void drawDrawables() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		// draws all the objects that need to be drawn
		for (Drawable d : GlobalVars.getDrawables()) {
			d.draw();
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

	public boolean mouseLeft() {
		if (Mouse.isButtonDown(0)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = SCREEN_HEIGHT - Mouse.getY();
		return new Point(x, y);
	}

	public Component getCanvas() {
		// TODO Auto-generated method stub
		return null;
	}

}
