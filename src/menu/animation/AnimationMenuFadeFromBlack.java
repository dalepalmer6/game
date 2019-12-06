package menu.animation;

import system.SystemState;

public class AnimationMenuFadeFromBlack extends AnimationMenu {

	public AnimationMenuFadeFromBlack(SystemState m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	public void createAnimMenu() {
//		Animation anim = new AnimationFadeFromBlack(state);
		Animation anim = new AnimationFadeFromBlack(state);
		addMenuItem(anim);
	}
}
