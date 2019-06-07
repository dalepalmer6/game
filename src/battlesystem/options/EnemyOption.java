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
	private boolean selected;
	
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
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setSelected(boolean b) {
		selected = b;
	}
	
	public EnemyOption(Enemy enemy,int x,int y, StartupNew s) {
		super(enemy.getName(),0,0,0,0,s);
		this.enemy = enemy;
		this.x = x;
		this.y = y;
		this.width = enemy.getWidth();
		this.height = enemy.getHeight();
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img\\enemies\\" + enemy.getTexture());
		String pose = "front";
		Pose p = enemy.getSpriteData().getPose(pose, "", "");
		if (selected) {
			m.setUseTransparency();
		}
		m.renderTile(x,y,width,height,p.getStateByNum(0).getX(),p.getStateByNum(0).getY(),p.getStateByNum(0).getWidth()/4,p.getStateByNum(0).getHeight()/4);
//		m.renderNonTextured(x,y,enemy.getWidth(),enemy.getHeight(),0.5f);
	}
	
	public String execute() {
		//the enemy is selected, use the attack on them
		BattleMenu bm = ((BattleMenu)state.getMenuStack().peek());
		bm.getCurrentAction().setUser(bm.getCurrentPartyMember());
		bm.getCurrentAction().setTarget(enemy);
		bm.setDoneAction();
		return null;
	}

	public void moveX(double d) {
		// TODO Auto-generated method stub
		x -= d;
	}

	public void moveY(double d) {
		// TODO Auto-generated method stub
		y -= d;
	}
}
