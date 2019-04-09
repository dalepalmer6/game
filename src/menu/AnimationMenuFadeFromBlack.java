package menu;

public class AnimationMenuFadeFromBlack extends AnimationMenu {

	public AnimationMenuFadeFromBlack(StartupNew m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	public void createAnimMenu() {
//		Animation anim = new AnimationFadeFromBlack(state);
		Animation anim = new AnimationFadeFromBlack(state);
		addMenuItem(anim);
	}
}
