package menu;

import java.io.IOException;

import battlesystem.BattleMenu;
import canvas.MainWindow;
import gamestate.TileMetadata;

public class AnimationFadeToBlack extends Animation {
	private float alpha;
	private boolean complete;
	private double ticksPerFrame = 0.5;
	
	public AnimationFadeToBlack(StartupNew m) {
		super (m);
		alpha = 1.0f;
	}
	
	public void createAnimation() {
		
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void updateAnim() {
		System.out.println(tickCount);
		tickCount += ticksPerFrame;
		
		if (tickCount > 12) {
			state.getMenuStack().peek().setToRemove(this);
			tickCount = 0;
			state.canLoad = true;
			complete = true;
			return;
		}
		
		alpha = (float) ((float) (tickCount/12));
	}
	
	
	public void draw(MainWindow m) {
//		m.setTexture(state.getPathToAnims() + texture);
		m.renderNonTextured(x,y,width,height,alpha);
	}
}
