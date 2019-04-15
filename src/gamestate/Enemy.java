package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;

public class Enemy extends BattleEntity {
	private int width;
	private int height;
	private int expYield;
	
	public Enemy clone() {
		Enemy enemyCopy = new Enemy(texture,name,width/4,height/4,stats.createCopy(),expYield);
		return enemyCopy;
	}
	
	public Enemy(String texture, String name, int width, int height, EntityStats es, int xp) {
		super(texture,name,es);
		this.width = width*4;
		this.height = height*4;
		expYield = xp;
	}
	
	public int getExpYield() {
		return expYield;
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}
}
