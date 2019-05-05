package menu;

public class AnimationMenu extends Menu {

	public AnimationMenu(StartupNew m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isSwirl() {
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
	
	public void createAnimMenu() {
//		Animation anim = new AnimationFadeFromBlack(state);
		Animation anim = new AnimationFadeToBlack(state);
		addMenuItem(anim);
	}
	
//	public void update() {
//		
//	}

}
