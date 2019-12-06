package menu.animation;

import java.io.IOException;

import battlesystem.menu.BattleMenu;
import system.MainWindow;
import system.SystemState;
import system.sprites.TileMetadata;

public class AnimationFadeFromBlack extends Animation {
	private float alpha;
	private boolean complete;
	private double ticksPerFrame = 0.5;
	public AnimationFadeFromBlack(SystemState m) {
		super (m);
		alpha = 0.0f;
	}
	
	public void createAnimation() {
		
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void updateAnim() {
//		SystemState.out.println(tickCount);
		state.setHoldable(true);
		tickCount += ticksPerFrame;
		
		if (tickCount > 12) {
//			SystemState.out.println("Can load");
			state.getMenuStack().peek().setToRemove(this);
			tickCount = 0;
			complete = true;
//			state.canLoad = true;
			return;
		}
		
		alpha = (float) ((float) 1-(tickCount/12));
	}
	
	
	public void draw(MainWindow m) {
//		m.setTexture(state.getPathToAnims() + texture);
		m.renderNonTextured((int)x,(int)y,width,height,alpha);
	}
}
