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
	
	public EnemyOption(Enemy enemy,int x,int y, StartupNew s) {
		super(enemy.getName(),0,0,0,0,s);
		this.enemy = enemy;
		this.x = x;
		this.y = y;
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img/" + enemy.getTexture());
		String pose = "front";
		Pose p = enemy.getSpriteData().getPose(pose, "", "");
		m.renderTile(x,y,enemy.getWidth(),enemy.getHeight(),p.getStateByNum(0).getX(),p.getStateByNum(0).getY(),p.getStateByNum(0).getWidth(),p.getStateByNum(0).getHeight());
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
