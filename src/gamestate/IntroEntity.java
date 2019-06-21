package gamestate;

import canvas.MainWindow;
import menu.StartupNew;

public class IntroEntity extends CameraControllingEntity {

	public IntroEntity(String texture,double x, double y, int width, int height, Camera c, StartupNew m,String name) {
		super(texture,x,y,width,height,c,m,name);
		stepSizeX = 10;
		stepSizeY = 10;
	}
	
	public void draw(MainWindow m ) {
		
	}
	
}
