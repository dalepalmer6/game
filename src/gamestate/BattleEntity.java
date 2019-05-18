package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import battlesystem.options.BattleAction;
import font.TextWindowWithPrompt;
import menu.StartupNew;

public class BattleEntity {
	protected String texture;
	protected String name;
	protected String state;
	protected int width;
	protected int height;
	protected EntityStats stats;
	protected SpritesheetCoordinates spriteData;
	protected BattleAction actionToPerform;
	//position to be drawn on screen
	protected StartupNew systemState;
	
	public void setState(String string) {
		// TODO Auto-generated method stub
		state = string;
	}
	
	public EntityStats getStats() {
		return stats;
	}
	
	public SpritesheetCoordinates getSpriteData() {
		return spriteData;
	}
	
	public String getName() {
		return name;
	}
	
	public void setWidth(int i) {
		this.width = i;
	}
	
	public void setHeight(int i) {
		this.height = i;
	}
	
	public int getWidth() {
		return width;
	}
	
//	public BattleEntity(String texture, String name, int hp, int pp) {
//		this.texture = texture;
//		this.name = name;
//		this.spriteData = new SpritesheetCoordinates();
//		spriteData.setPose("front_");
//		spriteData.addStateToPose("front_",0,0,32,64);
//		this.stats = new EntityStats(hp,pp,0,0,0,0,0,0,0,0,0,0,0);
//	}
//	
	public BattleEntity(String texture, String name, EntityStats es, StartupNew systemState) {
		this.texture = texture;
		this.systemState=systemState;
		this.name = name;
		this.stats = es;
		this.state = "normal";
	}
	
	public void ai() {
		
	}

	public String getTexture() {
		// TODO Auto-generated method stub
		return texture;
	}

	public void takeDamage(int damage) {
		// TODO Auto-generated method stub
		int hp = this.stats.getStat("CURHP");
		hp -= damage;
		hp = Math.max(0,hp);
		stats.replaceStat("CURHP",hp);
	}

	public String getState() {
		// TODO Auto-generated method stub
		return state;
	}
}
