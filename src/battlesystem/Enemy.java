package battlesystem;

import java.awt.Rectangle;
import java.util.ArrayList;

import gamestate.EntityStats;
import menu.windows.TextWindowWithPrompt;
import system.SystemState;
import system.sprites.SpritesheetCoordinates;

public class Enemy extends BattleEntity {
	private int width;
	private int height;
	private int expYield;
	private String entityId;
	private int moneyYield;
	private int id;
	private EnemyAction[] actions;
	private String battleBG;
	private String battleBGM;
	private int maxAllies = 0; 
	
	public int getMoneyYield() {
		return moneyYield;
	}
	
	public String getEntityId() {
		return entityId;
	}
	
	@Override
	public Enemy clone() {
		Enemy enemyCopy = new Enemy(id,texture,name,stats.createCopy(),expYield,moneyYield, entityId,systemState);
		enemyCopy.setActions(actions);
		enemyCopy.setBGM(battleBGM);
		enemyCopy.setBattleBG(battleBG);
		enemyCopy.setResistances(resistances);
		enemyCopy.setMaxAllies(maxAllies);
		enemyCopy.setPredicate(predicate);
		return enemyCopy;
	}
	
	public Enemy(int id, String texture, String name, EntityStats es, int xp, int money, String entityName, SystemState systemState) {
		super(texture,name,es, systemState);
		this.id = id;
		systemState.getMainWindow().setTexture("img\\enemies\\" + texture);
		Rectangle rect = systemState.getTextureAtlas().getCurrentRectangle();
//		this.width = width*4;
//		this.height = height*4;
		this.width = rect.width*4;
		this.height = rect.height*4;
		this.moneyYield = money;
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

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setActions(EnemyAction[] enemyActions) {
		// TODO Auto-generated method stub
		this.actions = enemyActions;
	}
	
	public EnemyAction getRandomAction() {
		int length = actions.length;
		double randomVal = Math.floor(Math.random() * length);
		return actions[(int)randomVal];
	}

	public void setBattleBG(String battleBG) {
		// TODO Auto-generated method stub
		this.battleBG = battleBG;
	}
	
	public String getBattleBG() {
		return battleBG;
	}
	
	public void setBGM(String n) {
		battleBGM = n;
	}
	
	public String getBGM() {
		// TODO Auto-generated method stub
		return battleBGM;
	}

	public void setMaxAllies(int numberAllies) {
		// TODO Auto-generated method stub
		this.maxAllies  = numberAllies;
	}
	
	public int getMaxAllies() {
		return maxAllies;
	}

	

	
}
