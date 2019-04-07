package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import battlesystem.options.BattleAction;
import font.TextWindowWithPrompt;

public class BattleEntity {
	protected String texture;
	protected String name;
	protected int width;
	protected int height;
	protected EntityStats stats;
	protected SpritesheetCoordinates spriteData;
	protected BattleAction actionToPerform;
	//position to be drawn on screen
	
	public EntityStats getStats() {
		return stats;
	}
	
	public void dealDamage(int x) {
		int hp = this.stats.getStat("HP");
		hp -= x;
		stats.replaceStat("HP",hp);
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
	
	public BattleEntity(String texture, String name, int hp, int pp) {
		this.texture = texture;
		this.name = name;
		this.spriteData = new SpritesheetCoordinates();
		spriteData.setPose("front_");
		spriteData.addStateToPose("front_",0,0,32,64);
		this.stats = new EntityStats(hp,pp,0,0,0,0,0,0);
	}
	
	public BattleEntity(String texture, String name, EntityStats es) {
		this.texture = texture;
		this.name = name;
		this.spriteData = new SpritesheetCoordinates();
		spriteData.setPose("front_");
		spriteData.addStateToPose("front_",0,0,32,64);
		this.stats = es;
	}
	
	public void ai() {
		
	}

	public String getTexture() {
		// TODO Auto-generated method stub
		return texture;
	}

	public TextWindowWithPrompt performBattleAction(BattleMenu bm, ArrayList<PCBattleEntity> party, ArrayList<Enemy> enemies) {
		// TODO Auto-generated method stub
		return null;
	}
}
