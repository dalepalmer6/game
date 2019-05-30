package menu;

import global.InputController;

public class AnimationMenu extends Menu {
	private boolean complete = false;
	
	public AnimationMenu(StartupNew m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	@Override
	public void doDoneFadeOutAction() {
		if (isSwirl()) {
			((SwirlAnimation)menuItems.get(0)).startBattle();
		}
	}
	
	public boolean isSwirl() {
		if (menuItems.isEmpty()) {
			return false;
		}
		if (menuItems.get(0) instanceof SwirlAnimation) {
			return true;
		}
		return false;
	}
	
	public void createAnimMenu(Animation anim) {
		addMenuItem(anim);
	}
	
	public Animation getAnimation() {
		return (Animation) menuItems.get(0);
	}
	
	public void update(InputController input) {
		if (menuItems.isEmpty()) {
			return;
		} else {
			complete = ((Animation)menuItems.get(0)).isComplete();
		}
	}
	
	public void createAnimMenu() {
//		Animation anim = new AnimationFadeFromBlack(state);
		Animation anim = new AnimationFadeToBlack(state);
		addMenuItem(anim);
	}
	
//	public void update() {
//		
//	}

}
