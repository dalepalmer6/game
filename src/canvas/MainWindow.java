package canvas;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import global.TextureAtlas;
import menu.DrawableObject;

public class MainWindow {
//	private Texture texture;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private TextureAtlas textureAtlas;

	public MainWindow(TextureAtlas ta, int sw, int sh) { 
		textureAtlas = ta;
		SCREEN_WIDTH = sw;
		SCREEN_HEIGHT = sh;
	}
	
//	public Texture getTexture() {
//		return texture;
//	}
	
	public int getScreenWidth() {
		return SCREEN_WIDTH;
	}
	
	public int getScreenHeight() {
		return SCREEN_HEIGHT;
	}

	public void setTexture(String pathToFile) {
//		try {
//			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(pathToFile));
			textureAtlas.setRectByName(pathToFile);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	//renderTile(int x, int y, int width, int height, float dx, float dy, float dw, float dh)
	public void renderBG(String bgImg) {
		setTexture("img/" +  bgImg);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int TILE_SIZE = 100;
		for (int i = 0; i < SCREEN_WIDTH; i+=TILE_SIZE) {
			for (int j = 0; j < SCREEN_HEIGHT; j+=TILE_SIZE) {
				renderTile(i,j,TILE_SIZE,TILE_SIZE,0,0,64,64);
			}
		}
		
		
		
	}
	
//	public void render(int x, int y, int width, int height) {
//		//draw with no texture
//		Color.white.bind();
//		
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		
//        GL11.glPushMatrix();
//            GL11.glBegin(GL11.GL_QUADS);
//				GL11.glTexCoord2f(0, 0);
//				GL11.glVertex2f(x, y);
//				GL11.glTexCoord2f(1, 0);
//				GL11.glVertex2f(x + width, y);
//				GL11.glTexCoord2f(1, 1);
//				GL11.glVertex2f(x + width, y + height);
//				GL11.glTexCoord2f(0, 1);
//				GL11.glVertex2f(x, y + height);
//            GL11.glEnd();
//        GL11.glPopMatrix();
//	}

	public void renderTile(int x, int y, int width, int height, float dx, float dy, float dw, float dh) {
		renderTile(x,y,width,height,dx,dy,dw,dh,false);
	}
	
	public void renderTile(int x, int y, int width, int height, float dx, float dy, float dw, float dh, boolean needToFlip) {
        GL11.glPushMatrix();
        Rectangle rect = textureAtlas.getCurrentRectangle();
	        float xo = (rect.x + dx)/textureAtlas.getTexture().getTextureWidth();
	    	float xw = (rect.x + dx+dw)/textureAtlas.getTexture().getTextureWidth();
	    	float yo = (rect.y + dy)/textureAtlas.getTexture().getTextureHeight();
	    	float yw = (rect.y + dy+dh)/textureAtlas.getTexture().getTextureHeight();
	    	float scaleX = 1;
	    	int correction = 0;
	    	if (needToFlip) {
	    		scaleX = -1.0f;
//	    		GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            	GL11.glScalef(-1.0f, 1.0f, 1.0f);
            	correction = width;
            }
	    	GL11.glBegin(GL11.GL_QUADS);
            
			GL11.glTexCoord2f(xo,yo);
			GL11.glVertex2f(-correction + scaleX*x, y);
			GL11.glTexCoord2f(xw, yo);
			GL11.glVertex2f(-correction + scaleX*x + width, y);
			GL11.glTexCoord2f(xw,yw);
			GL11.glVertex2f(-correction + scaleX*x + width, y + height);
			GL11.glTexCoord2f(xo,yw);
			GL11.glVertex2f(-correction + scaleX*x, y + height);
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

	
	
	public void drawDrawables(List<DrawableObject> drawables) {
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		//sort the list, so that the objects with the lowest y-value are first
//		drawables = sortDrawables(drawables);
		// draws all the objects that need to be drawn
		for (DrawableObject d : drawables) {
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
