package system;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;

public class TextureAtlas {
	private Texture atlas;
	private Map<String, Rectangle> rectsMap;
	private Rectangle currentRectangle;
	
	public Texture getTexture() {
		return atlas;
	}
	
	public void setTexture(Texture t) {
		atlas = t;
	}
	
	public void setRects(Map<String, Rectangle> rm) {
		this.rectsMap = rm;
	}
	
	public void setRectByName(String name) {
		currentRectangle = rectsMap.get(name);
	}
	
	public Rectangle getCurrentRectangle() {
		return currentRectangle;
	}
	
	public TextureAtlas() {
//		rectsMap = new Map<String, Rectangle>();
	}
}
