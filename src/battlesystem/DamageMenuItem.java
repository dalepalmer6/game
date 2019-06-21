package battlesystem;

import battlesystem.options.BattleTextWindow;
import canvas.MainWindow;
import menu.MenuItem;
import menu.StartupNew;

/*draws the damage that an attack dealt*/
public class DamageMenuItem extends MenuItem {
	private int aliveTime;
	private int damageValue;
	private boolean alive = true;
	private double xMovement;
	private boolean isHealing;
	private boolean damageOverTime;
	private boolean damage;
	int killTime = 120;
	private boolean disallowProgress;
	private boolean setMiss;
	
	public DamageMenuItem(int dmg, int x, int y, StartupNew state) {
		super("" + dmg, x,y, state);
		xMovement = Math.random()*4;
		if (Math.random() > 0.5) {
			xMovement *= -1;
		}
		else {
			targetY = y + 900;
		}
		if (dmg == -1) {
			setMiss = true;
		}
		damageValue = dmg;
	}


	public void drawDigit(MainWindow m,int digit, double curX, double y) {
		int coordX = 0;
		int coordY = 0;
		switch (digit) {
			case 0: coordX = 128; coordY = 0; break; 
			case 1: coordX = 137; coordY = 0; break;
			case 2: coordX = 146; coordY = 0; break;
			case 3: coordX = 155; coordY = 0; break;
			case 4: coordX = 164; coordY = 0; break;
			case 5: coordX = 173; coordY = 0; break;
			case 6: coordX = 182; coordY = 0; break;
			case 7: coordX = 191; coordY = 0; break;
			case 8: coordX = 200; coordY = 0; break;
			case 9: coordX = 209; coordY = 0; break;
		}
		m.renderTile(curX,y,9*4,9*4,coordX,coordY,9,9);
	}
	
	public void drawMissed(MainWindow m, double curX, double curY) {
		m.renderTile(curX,y,35*4,10*4,128,9,35,10);
	}
	
	public void draw(MainWindow m) {
		if (setMiss) {
			drawMissed(m,x,y);
		} else {
			String value = damageValue + "";
			char[] chars = value.toCharArray();
			double curX = x;
			for (char c : chars) {
				int val = Integer.parseInt(c + "");
				drawDigit(m,val,curX,y);
				curX += 36;
			}
		}
		
		
	}
	
	public void updateAnim() {
		if (aliveTime > killTime) {
			state.getMenuStack().peek().setToRemove(this);
		}
		if (aliveTime > 30 && alive) {
			BattleMenu bm = state.battleMenu;
			if (!disallowProgress) {
				if (bm.turnStackIsEmpty() && bm.getCurrentActiveBattleAction().isComplete()) {
					bm.setPollForActions();
				}
				if (bm.getCurrentActiveBattleAction().isComplete()) {
					bm.setGetNextPrompt();
				} else {
					if (bm.getCurrentActiveBattleAction().isContinuous()&& bm.getCurrentActiveBattleAction().needAnim() && !bm.getCurrentActiveBattleAction().isComplete()) {
						bm.setGetAnimation(true);
					} else {
						bm.setGetResultText();
					}
					
				}
				alive = false;
			}
		}
		approachTargetPos();
		aliveTime++;
	}
	
	public void approachTargetPos() {
		// TODO Auto-generated method stub
		//move in a downward parabola
		if (isHealing) {
			if (targetY < y) {
				y -= 8;
			}
			
		}
		else {
			x += xMovement;
			if (targetY > y) {
				y+=(aliveTime-15)*1;
//				textObject.setY(textObject.getY()+8);
			} else if (targetY < y) {
				y-=-(aliveTime-15)*1;
//				textObject.setY(textObject.getY()-8);
			}
		}
		
	}


	public void setIsHealing(boolean h) {
		// TODO Auto-generated method stub
		isHealing = h;
		targetY = y - 64;
		killTime = 60;
	}


	public void setDisallowAutoProgress(boolean disallowAutoProgress) {
		// TODO Auto-generated method stub
		disallowProgress = disallowAutoProgress;
	}
	
}
