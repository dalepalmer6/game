package battlesystem.options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import menu.MenuItem;
import system.MainWindow;
import system.MotherSystemState;
import system.SystemState;

public class EnemyOptionPanel extends MenuItem {
	private ArrayList<EnemyOption> enemyOptions;
	private int selected = -2;
	private Texture battleBGTexture;
	private float[] battleBGVars;
	private int entityToShake;
	private int timer = -1;
	private int killed = -1;
	private Texture palette;
	
	public void setSelected(int i) {
		selected = i;
	}
	
	public EnemyOption getEnemyOption(int i) {
		return enemyOptions.get(i);
	}
	
	@Override
	public void updateAnim() {
		if (killed != -1) {
			enemyOptions.get(entityToShake).setWidth((int)(enemyOptions.get(entityToShake).getWidth()*0.95));
			enemyOptions.get(entityToShake).setHeight((int)(enemyOptions.get(entityToShake).getHeight()*0.95));
			enemyOptions.get(entityToShake).moveY(1*(timer-15));
		}
		if (timer != -1) {
			timer--;
//			if (timer % 1 == 0) {
				enemyOptions.get(entityToShake).moveX(8*Math.cos(timer*Math.PI/8));
				enemyOptions.get(entityToShake).moveY(2*Math.cos(timer*Math.PI/2));
//			}
		}
		if (timer == 0) {
			if (killed != -1) {
				enemyOptions.get(entityToShake).setWidth(0);
			}
			timer = -1;
			killed = -1;
		}
		int i = 0;
		for (EnemyOption eo : enemyOptions) {
			if (selected == -2) {
				eo.setSelected(false);
			}
			if (selected == -1) {
				//targeting all
				eo.setSelected(true);
			} else {
				if (i == selected) {
					eo.setSelected(true);
				}  else {
					eo.setSelected(false);
				}
			}
			i++;
		}
//		selected = -2;
	}
	
	public EnemyOptionPanel(SystemState m) {
		super("", 0, 400, m.getMainWindow().getScreenWidth(), m.getMainWindow().getScreenHeight()-400, m);
		enemyOptions = new ArrayList<EnemyOption>();
	}
	
	public void addEnemyOption(EnemyOption e) {
		this.enemyOptions.add(e);
		String texture = enemyOptions.get(0).getEnemy().getBattleBG();
		if (enemyOptions.size() == 1) {
			try (BufferedReader br = new BufferedReader(new FileReader(new File("img/battlebgs/" + texture.substring(0,texture.indexOf(".")))))) {
				battleBGTexture = BufferedImageUtil.getTexture("", ImageIO.read(new File("img/battlebgs/" + texture)));
				palette = BufferedImageUtil.getTexture("", ImageIO.read(new File("img/" + "palettetest.png")));
				String row = br.readLine();
				String[] split = row.split(",");
				battleBGVars = new float[split.length];
				int i = 0;
				for (String s : split) {
					battleBGVars[i++] = Float.parseFloat(s);
				}
			} catch (IOException err) {
				err.printStackTrace();
			}
		}
	}

	public void removeEnemyOption(EnemyOption e) {
		this.enemyOptions.remove(e);
	}
	
	public ArrayList<EnemyOption> getEnemyOptions() {
		return enemyOptions;
	}
	
	public void drawBG(MainWindow m) {
		m.setBattleTexture(battleBGTexture,battleBGVars,palette);
		((MotherSystemState) state).setDrawBattleBG(true);
	}
	
	public void draw(MainWindow m) {
		drawBG(m);
		for (EnemyOption eo : enemyOptions) {
			eo.draw(state.getMainWindow());
		}
	}

	public int getSelected() {
		// TODO Auto-generated method stub
		return selected;
	}

	public void setToShake(int index) {
		// TODO Auto-generated method stub
		entityToShake = index;
		timer = 30;
	}

	public void setKilled(int index) {
		// TODO Auto-generated method stub
		killed = index;
	}
}
