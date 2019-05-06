package menu;

import java.io.IOException;

import battlesystem.BattleMenu;
import canvas.MainWindow;
import gamestate.TileMetadata;

public class AnimationFadeToBlack extends Animation {
	private float alpha;
	private double ticksPerFrame = 0.5;
	
	public AnimationFadeToBlack(StartupNew m) {
		super (m);
		alpha = 1.0f;
	}
	
	public void createAnimation() {
		
	}
	
	
	
	public void updateAnim() {
		System.out.println(tickCount);
		if (!complete) {
			tickCount += ticksPerFrame;
		} else if (!done){
//			state.getMenuStack().peek().setToRemove(this);
			state.setFadeOutDone();
			done = true;
		}
		
		
		if (tickCount >= 12) {
//			tickCount = 0;
			complete = true;
			return;
		}
		
		alpha = (float) (tickCount/12);
	}
	
	
	public void draw(MainWindow m) {
//		m.setTexture(state.getPathToAnims() + texture);
		m.renderNonTextured(x,y,width,height,alpha);
	}
}
