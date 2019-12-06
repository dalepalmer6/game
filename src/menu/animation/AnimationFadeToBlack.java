package menu.animation;

import system.MainWindow;
import system.SystemState;

public class AnimationFadeToBlack extends Animation {
	private float alpha;
	private double ticksPerFrame = 0.5;
	
	public AnimationFadeToBlack(SystemState m) {
		super (m);
		alpha = 1.0f;
	}
	
	public void createAnimation() {
		
	}
	
	
	
	public void updateAnim() {
//		SystemState.out.println(tickCount);
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
		m.renderNonTextured((int)x,(int)y,width,height,alpha);
	}
}
