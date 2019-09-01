package canvas;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import global.Shaders;
import global.TextureAtlas;
import global.TexturedVertex;
import menu.DrawableObject;

public class MainWindow {
//	private Texture texture;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private TextureAtlas textureAtlas;
	private int program;
	private boolean useShader;
	private int vsId;
	private int fsId;
	private int pId;
	private float time = System.nanoTime();
	private int vboid;
	private int vboiId;
	private int indicesCount;
	private int vaoId;
	private Texture tilesetTexture;
	private Texture battleBGTexture;
	private Texture battleBGPalette;
	private float[] battleBGVars;
	private boolean useTransparency;
	int counter = 0;
	
	public void setTextureAtlas(TextureAtlas ta) {
		this.textureAtlas = ta;
	}
	
	public void setTilesetTexture(Texture t) {
		tilesetTexture = t;
	} 
	
	public void setUseShader(boolean b) {
		useShader = b;
	}
	
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
		setTexture("img\\" +  bgImg);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int TILE_SIZE = 100;
		for (int i = 0; i < SCREEN_WIDTH; i+=TILE_SIZE) {
			for (int j = 0; j < SCREEN_HEIGHT; j+=TILE_SIZE) {
				renderTile(i,j,TILE_SIZE,TILE_SIZE,0,0,64,64);
			}
		}
	}
	
	public void renderNonTextured(int x, int y, int width, int height, float alpha) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
			// set the color of the quad (R,G,B,A)
			GL11.glColor4f(0f,0f,0f,alpha);
			
			// draw quad
			GL11.glBegin(GL11.GL_QUADS);
			    GL11.glVertex2f(x,y);
			    GL11.glVertex2f(x+width,y);
			    GL11.glVertex2f(x+width,y+height);
			    GL11.glVertex2f(x,y+height);
			GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1f,1f,1f,1f);
	}
	
	public void renderTile(double x, double y, int width, int height, float dx, float dy, float dw, float dh) {
		renderTile(x,y,width,height,dx,dy,dw,dh,false);
	}
	
	public void renderAnimation(Texture t, double x, double y, int width, int height, float dx, float dy, float dw, float dh, boolean needToFlip) {
		if (useShader) { 
		} 
		else {ARBShaderObjects.glUseProgramObjectARB(0);}
        GL11.glPushMatrix();
        Rectangle rect = textureAtlas.getCurrentRectangle();
	        float xo = (dx)/t.getTextureWidth();
	    	float xw = (dx+dw)/t.getTextureWidth();
	    	float yo = (dy)/t.getTextureHeight();
	    	float yw = (dy+dh)/t.getTextureHeight();
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
			GL11.glVertex2d(-correction + scaleX*x, y);
			GL11.glTexCoord2f(xw, yo);
			GL11.glVertex2d(-correction + scaleX*x + width, y);
			GL11.glTexCoord2f(xw,yw);
			GL11.glVertex2d(-correction + scaleX*x + width, y + height);
			GL11.glTexCoord2f(xo,yw);
			GL11.glVertex2d(-correction + scaleX*x, y + height);
            GL11.glEnd();
        GL11.glPopMatrix();

	}
	
	public void drawBattleBG() {
		
		int TILE_SIZE = 100;
//		for (int i = 0; i < SCREEN_WIDTH; i+=TILE_SIZE) {
//			for (int j = 0; j < SCREEN_HEIGHT; j+=TILE_SIZE) {
				renderTile(0,0,256,256,0,0,256,256);
//			}
//		}
		
	}
	
	public void useShader() {
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		float dTime = time - System.nanoTime();
		GL20.glUseProgram(pId);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, battleBGTexture.getTextureID());
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11. glShadeModel(GL11.GL_FLAT);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, battleBGPalette.getTextureID());
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
//        int loc = GL20.glGetUniformLocation(pId, "s_baseMap");
//        GL20.glUniform1i(loc,1);
		
		int loc = GL20.glGetUniformLocation(pId, "time");
        int fx = GL20.glGetUniformLocation(pId, "freqx");
        int fy = GL20.glGetUniformLocation(pId, "freqy");
        int ax = GL20.glGetUniformLocation(pId, "amplitudex");
        int ay = GL20.glGetUniformLocation(pId, "amplitudey");
        int sx = GL20.glGetUniformLocation(pId, "speedx");
        int sy = GL20.glGetUniformLocation(pId, "speedy");
        int tex = GL20.glGetUniformLocation(pId, "texture_diffuse");
        int pal = GL20.glGetUniformLocation(pId, "texture_color_palette");
        int frameCount = GL20.glGetUniformLocation(pId, "frames");
        
        counter++;
        
        GL20.glUniform1i(tex,0);
        GL20.glUniform1i(pal,2);
        GL20.glUniform1f(frameCount,counter);
        GL20.glUniform1f(loc,dTime/10e9f);
        GL20.glUniform1f(fx,battleBGVars[1]);
        GL20.glUniform1f(fy,battleBGVars[4]);
        GL20.glUniform1f(ax,battleBGVars[0]);
        GL20.glUniform1f(ay,battleBGVars[3]);
        GL20.glUniform1f(sx,battleBGVars[2]);
        GL20.glUniform1f(sy,battleBGVars[5]);
        
        TexturedVertex v0 = new TexturedVertex(); 
        v0.setXYZ(-1f, 1f, 0); v0.setRGB(1, 0, 0); v0.setST(0, 0);
        TexturedVertex v1 = new TexturedVertex(); 
        v1.setXYZ(-1, -1f, 0); v1.setRGB(0, 1, 0); v1.setST(0, 1);
        TexturedVertex v2 = new TexturedVertex(); 
        v2.setXYZ(1f, -1f, 0); v2.setRGB(0, 0, 1); v2.setST(1, 1);
        TexturedVertex v3 = new TexturedVertex(); 
        v3.setXYZ(1f, 1f, 0); v3.setRGB(1, 1, 1); v3.setST(1, 0);
        
//        TexturedVertex v0 = new TexturedVertex(); 
//        v0.setXYZ(-1f, 2, 0); v0.setRGB(1, 0, 0); v0.setST(0, 0);
//        TexturedVertex v1 = new TexturedVertex(); 
//        v1.setXYZ(-1, -2, 0); v1.setRGB(0, 1, 0); v1.setST(0, 1);
//        TexturedVertex v2 = new TexturedVertex(); 
//        v2.setXYZ(1f, -2, 0); v2.setRGB(0, 0, 1); v2.setST(1, 1);
//        TexturedVertex v3 = new TexturedVertex(); 
//        v3.setXYZ(1f, 2, 0); v3.setRGB(1, 1, 1); v3.setST(1, 0);
        
        TexturedVertex[] vertices = new TexturedVertex[] {v0, v1, v2, v3};
        // Put each 'Vertex' in one FloatBuffer
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length *
                TexturedVertex.elementCount);
        for (int i = 0; i < vertices.length; i++) {
            // Add position, color and texture floats to the buffer
            verticesBuffer.put(vertices[i].getElements());
        }
        verticesBuffer.flip();  
        // OpenGL expects to draw vertices in counter clockwise order by default
        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };
        indicesCount = indices.length;
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
         
        // Create a new Vertex Array Object in memory and select it (bind)
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
         
        // Create a new Vertex Buffer Object in memory and select it (bind)
        vboid = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
         
        // Put the position coordinates in attribute list 0
        GL20.glVertexAttribPointer(0, TexturedVertex.positionElementCount, GL11.GL_FLOAT, 
                false, TexturedVertex.stride, TexturedVertex.positionByteOffset);
        // Put the color components in attribute list 1
        GL20.glVertexAttribPointer(1, TexturedVertex.colorElementCount, GL11.GL_FLOAT, 
                false, TexturedVertex.stride, TexturedVertex.colorByteOffset);
        // Put the texture coordinates in attribute list 2
        GL20.glVertexAttribPointer(2, TexturedVertex.textureElementCount, GL11.GL_FLOAT, 
                false, TexturedVertex.stride, TexturedVertex.textureByteOffset);
         
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
         
        // Create a new VBO for the indices and select it (bind) - INDICES
        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
//        GL20.glUseProgram(pId);
         
        // Bind the texture
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIds[textureSelector]);
         
        // Bind to the VAO that has all the information about the vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
         
        // Bind to the index VBO that has all the information about the order of the vertices
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
         
        // Draw the vertices
        GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
         
        // Put everything back to default (deselect)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        
//        GL11.glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
//        GL11.glPushMatrix();
//	    	GL11.glBegin(GL11.GL_QUADS);
//			GL11.glTexCoord2f(0f,0f);
//			GL11.glVertex2f(1920f,1080f);
//			GL11.glTexCoord2f(1f,0f);
//			GL11.glVertex2f(0f,1080f);
//			GL11.glTexCoord2f(1f,1f);
//			GL11.glVertex2f(0f,0f);
//			GL11.glTexCoord2f(0f,1f);
//			GL11.glVertex2f(1920f,0f);
//            GL11.glEnd();
//        GL11.glPopMatrix();
        GL20.glUseProgram(0);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureAtlas.getTexture().getTextureID());
	}
	
	public void setUseTransparency() {
		useTransparency = true;
	}
	
	public void renderTile(double x, double y, int width, int height, float dx, float dy, float dw, float dh, boolean needToFlip) {
//		GL11.glColor4f(0.0f,0.0f,0.0f,0.0f);
		if (useShader) { 
			useShader();
			return;
		} 
		else {GL20.glUseProgram(0);}
		if (useTransparency) {
			GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
			GL11.glColor4f(255f,255f,255f,0.2f);
		}
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
			GL11.glVertex2d(-correction + scaleX*x, y);
			GL11.glTexCoord2f(xw, yo);
			GL11.glVertex2d(-correction + scaleX*x + width, y);
			GL11.glTexCoord2f(xw,yw);
			GL11.glVertex2d(-correction + scaleX*x + width, y + height);
			GL11.glTexCoord2f(xo,yw);
			GL11.glVertex2d(-correction + scaleX*x, y + height);
            GL11.glEnd();
        GL11.glPopMatrix();
        if (useTransparency) {
			GL11.glPopAttrib();
			useTransparency = false;
		}
        
	}
	
	public void renderTiles(double x, double y, int width, int height, float dx, float dy, float dw, float dh, boolean needToFlip) {
//		GL11.glColor4f(0.0f,0.0f,0.0f,0.0f);
		if (useShader) { 
			useShader();
			return;
		} 
		else {GL20.glUseProgram(0);}
			float xo = (dx)/tilesetTexture.getTextureWidth();
	    	float xw = (dx+dw)/tilesetTexture.getTextureWidth();
	    	float yo = (dy)/tilesetTexture.getTextureHeight();
	    	float yw = (dy+dh)/tilesetTexture.getTextureHeight();
        GL11.glPushMatrix();
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
			GL11.glVertex2d(-correction + scaleX*x, y);
			GL11.glTexCoord2f(xw, yo);
			GL11.glVertex2d(-correction + scaleX*x + width, y);
			GL11.glTexCoord2f(xw,yw);
			GL11.glVertex2d(-correction + scaleX*x + width, y + height);
			GL11.glTexCoord2f(xo,yw);
			GL11.glVertex2d(-correction + scaleX*x, y + height);
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
		
//		GL11.glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);        // Reset The Current Viewport
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		setupShaders();
		
	}

	public void start() {
		initGL();
	}

	 private void setupShaders() {       
	        // Load the vertex shader
	        vsId = Shaders.loadShader("src/shaders/vertex_textured.glsl", GL20.GL_VERTEX_SHADER);
	        // Load the fragment shader
	        fsId = Shaders.loadShader("src/shaders/fragment_textured.glsl", GL20.GL_FRAGMENT_SHADER);
	         
	        // Create a new shader program that links both shaders
	        pId = GL20.glCreateProgram();
	        GL20.glAttachShader(pId, vsId);
	        GL20.glAttachShader(pId, fsId);

	        GL20.glLinkProgram(pId);
	        GL20.glValidateProgram(pId);
	    }
	
	
	public void drawDrawables(List<DrawableObject> drawables) {
		
		//sort the list, so that the objects with the lowest y-value are first
//		drawables = sortDrawables(drawables);
		// draws all the objects that need to be drawn
		for (DrawableObject d : drawables) {
			if (d != null) {
				d.draw(this);
			}
			
		}
	}
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = SCREEN_HEIGHT - Mouse.getY();
		return new Point(x, y);
	}

	public void setBattleTexture(Texture t, float[] vals, Texture p) {
		if (battleBGTexture == null) {
			battleBGTexture = t;
			battleBGVars = vals;
			battleBGPalette = p;
		}
	}

}
