package menu;

public class AnimationMenu extends Menu {

	public AnimationMenu(StartupNew m) {
		super(m);
		// TODO Auto-generated constructor stub
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
