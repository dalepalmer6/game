package gamestate;

import canvas.MainWindow;
import menu.StartupNew;

public class Camera {
	private int x;
	private int y;
	private int dx;
	private int dy;
	private StartupNew state;
	private MapRenderer mapRenderer;
	
	public MainWindow getMainWindow() {
		return state.getMainWindow();
	}
	
	public StartupNew getState() {
		return state;
	}
	
	public int getDX() {
		return dx;
	}
	
	public int getDY() {
		return dy;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void changeX(int dx) {
		x += dx;
	}
	
	public void changeY(int dy) {
		y +=dy ;
	}
	
	public Camera (StartupNew state) {
		this(0,0,state);
	}
	
	public void setMapRenderer(MapRenderer mr) {
		this.mapRenderer = mr;
	}
	
	public Camera (int x, int y, StartupNew s) {
		this.x = x;
		this.y = y;
		this.state = s;
	}

	
	public void updateCamera(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		x+=dx;
		y+=dy;
		x=Math.max(0,this.x);
		x=Math.min(x,mapRenderer.getTileSize()*state.getGameState().getMap().getMap().size()-getMainWindow().getScreenWidth());
		y=Math.max(0,this.y);
		y = Math.min(y, mapRenderer.getTileSize()*state.getGameState().getMap().getMap().get(0).size()-getMainWindow().getScreenHeight());
	}

	public void snapToEntity(int x, int y) {
		this.x = Math.max(0,x - getMainWindow().getScreenWidth()/2);
		this.y = Math.max(0,y - getMainWindow().getScreenHeight()/2);
	}
	
}
