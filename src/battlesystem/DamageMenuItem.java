package battlesystem;

import canvas.MainWindow;
import menu.MenuItem;
import menu.StartupNew;

/*draws the damage that an attack dealt*/
public class DamageMenuItem extends MenuItem {
	private int aliveTime;
	public DamageMenuItem(int dmg, int x, int y, StartupNew state) {
		super("" + dmg, x,y, state);
		targetY = y - 64;
	}


//	public void draw(MainWindow m) {
//		
//	}
	
	public void updateAnim() {
		if (aliveTime > 30) {
			state.getMenuStack().peek().setToRemove(this);
			BattleMenu bm = (BattleMenu) state.getMenuStack().peek();
			if (bm.turnStackIsEmpty() && bm.getCurrentActiveBattleAction().isComplete()) {
				bm.setPollForActions();
			}
			if (bm.getCurrentActiveBattleAction().isComplete()) {
				bm.setGetNextPrompt();
			} else {
				bm.setGetResultText();
			}
			
		}
		approachTargetPos();
		aliveTime++;
	}
	
}
