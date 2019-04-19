package battlesystem.options;

import java.util.ArrayList;

import gamestate.Enemy;
import battlesystem.options.EnemyOption;
import canvas.MainWindow;
import menu.MenuItem;
import menu.StartupNew;

public class EnemyOptionPanel extends MenuItem {
	private ArrayList<EnemyOption> enemyOptions;
	private int allEnemyWidths;
	private int selected;
	
	
	public EnemyOptionPanel(StartupNew m) {
		super("", 0, 400, m.getMainWindow().getScreenWidth(), m.getMainWindow().getScreenHeight()-400, m);
		// TODO Auto-generated constructor stub
		enemyOptions = new ArrayList<EnemyOption>();
		allEnemyWidths = 0;
	}
	
	public void addEnemyOption(EnemyOption e) {
		this.enemyOptions.add(e);
		e.getEnemy().getWidth();
	}

	public void removeEnemyOption(EnemyOption e) {
		this.enemyOptions.remove(e);
	}
	
	public ArrayList<EnemyOption> getEnemyOptions() {
		return enemyOptions;
	}
	
	public void drawBG(MainWindow m) {
//		m.setUseShader(true);
		m.setTexture("img/battlebg.png");
		m.setUseShader(true);
		m.renderTile(0,0,m.getScreenWidth(),m.getScreenHeight(),0,0,256,256);
		m.setUseShader(false);
//		m.setUseShader(false);
	}
	
	public void draw(MainWindow m) {
		drawBG(m);
		for (EnemyOption eo : enemyOptions) {
			eo.draw(state.getMainWindow());
		}
	}
}
