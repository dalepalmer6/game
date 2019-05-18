package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import font.TextWindowWithPrompt;
import menu.StartupNew;

public class Enemy extends BattleEntity {
	private int width;
	private int height;
	private int expYield;
	private String entityId;
	
	public String getEntityId() {
		return entityId;
	}
	
	@Override
	public Enemy clone() {
		Enemy enemyCopy = new Enemy(texture,name,width/4,height/4,stats.createCopy(),expYield,entityId,systemState);
		return enemyCopy;
	}
	
	public Enemy(String texture, String name, int width, int height, EntityStats es, int xp, String entityName, StartupNew systemState) {
		super(texture,name,es, systemState);
		this.width = width*4;
		this.height = height*4;
		expYield = xp;
		entityId = entityName;
		this.spriteData = new SpritesheetCoordinates();
		spriteData.setPose("front_");
		spriteData.addStateToPose("front_",0,0,width,height);
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
