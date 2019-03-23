package gamestate;

import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import font.SimpleDialogMenu;
import font.TextWindow;
import global.InputController;
import menu.StartupNew;

public class Player extends CameraControllingEntity implements Drawable, Controllable{
	
//	public Entity(String texture, int x, int y, int width, int height,StartupNew m) {
	public Player(String texture,int x, int y, int width, int height, int dw,int dh,Camera camera,StartupNew s) {
		super(texture,x,y,width,height,dw,dh,camera,s);
		this.width = width;
		this.height = height;
		this.dw =dw;
		this.dh = dh;
	}
	
	public void act() {
		if (interactables.size() != 0) {
			SimpleDialogMenu.createDialogBox(state,interactables.get(0).interactText());
		}
		
	}
	
	@Override
	public void handleInput(InputController input) {
		input.setHoldable(true);
		if (input.getSignals().get("UP")) {
			move(0,-stepSize);
		} if (input.getSignals().get("DOWN")) {
			move(0,stepSize);
		}if (input.getSignals().get("RIGHT")) {
			move(stepSize,0);
		} if (input.getSignals().get("LEFT")) {
			move(-stepSize,0);
		}  if (input.getSignals().get("CONFIRM")) {
			System.out.println("Acting");
			act();
		}
}
	
}
