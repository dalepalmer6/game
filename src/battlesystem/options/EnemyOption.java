package battlesystem.options;

import battlesystem.BattleMenu;
import canvas.MainWindow;
import gamestate.Enemy;
import gamestate.PCBattleEntity;
import gamestate.Pose;
import menu.MenuItem;
import menu.StartupNew;

public class EnemyOption extends MenuItem {
	private Enemy enemy;
	private int x;
	private int y;
	
	public Enemy getEnemy() {
		return enemy;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return enemy.getWidth();
	}
	
	public int getHeight() {
		return enemy.getHeight();
	}
	
	public EnemyOption(Enemy enemy,int x,int y, StartupNew s) {
		super(enemy.getName(),0,0,0,0,s);
		this.enemy = enemy;
		this.x = x;
		this.y = y;
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img\\enemies\\" + enemy.getTexture());
		String pose = "front";
		Pose p = enemy.getSpriteData().getPose(pose, "", "");
		m.renderTile(x,y,enemy.getWidth(),enemy.getHeight(),p.getStateByNum(0).getX(),p.getStateByNum(0).getY(),p.getStateByNum(0).getWidth()/4,p.getStateByNum(0).getHeight()/4);
	}
	
	public String execute() {
		//the enemy is selected, use the attack on them
		BattleMenu bm = ((BattleMenu)state.getMenuStack().peek());
		bm.getCurrentAction().setUser(bm.getCurrentPartyMember());
		bm.getCurrentAction().setTarget(enemy);
		bm.setDoneAction();
		return null;
	}
}
