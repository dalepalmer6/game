package battlesystem.options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import gamestate.Enemy;
import battlesystem.options.EnemyOption;
import canvas.MainWindow;
import menu.MenuItem;
import menu.StartupNew;

public class EnemyOptionPanel extends MenuItem {
	private ArrayList<EnemyOption> enemyOptions;
	private int allEnemyWidths;
	private int selected;
	private Texture battleBGTexture;
	private float[] battleBGVars;
	
	public EnemyOptionPanel(StartupNew m) {
		super("", 0, 400, m.getMainWindow().getScreenWidth(), m.getMainWindow().getScreenHeight()-400, m);
		// TODO Auto-generated constructor stub
		enemyOptions = new ArrayList<EnemyOption>();
		allEnemyWidths = 0;
	
	}
	
	public void addEnemyOption(EnemyOption e) {
		this.enemyOptions.add(e);
		if (enemyOptions.size() == 1) {
			try {
				String texture = enemyOptions.get(0).getEnemy().getBattleBG();
				battleBGTexture = BufferedImageUtil.getTexture("", ImageIO.read(new File("img/battlebgs/" + texture)));
				BufferedReader br = new BufferedReader(new FileReader(new File("img/battlebgs/" + texture.substring(0,texture.indexOf(".")))));
				String row = br.readLine();
				String[] split = row.split(",");
				battleBGVars = new float[split.length];
				int i = 0;
				for (String s : split) {
					battleBGVars[i++] = Float.parseFloat(s);
				}
			} catch (IOException err) {
				// TODO Auto-generated catch block
				err.printStackTrace();
			}
		}
//		e.getEnemy().getWidth();
	}

	public void removeEnemyOption(EnemyOption e) {
		this.enemyOptions.remove(e);
	}
	
	public ArrayList<EnemyOption> getEnemyOptions() {
		return enemyOptions;
	}
	
	public void drawBG(MainWindow m) {
//		m.setUseShader(true);
//		m.setTexture("img\\battlebg.png");
		m.setBattleTexture(battleBGTexture,battleBGVars);
		m.setUseShader(true);
		m.drawBattleBG();
//		m.renderTile(0,0,m.getScreenWidth(),m.getScreenHeight(),0,0,256,256);
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
