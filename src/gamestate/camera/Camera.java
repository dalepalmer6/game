package gamestate.camera;

import gamestate.map.MapRenderer;
import system.MainWindow;
import system.SystemState;

public class Camera {
	private double x;
	private double y;
	private double dx;
	private double dy;
	private SystemState state;
	private MapRenderer mapRenderer;
	private boolean stopped;
	
	public MainWindow getMainWindow() {
		return state.getMainWindow();
	}
	
	public SystemState getState() {
		return state;
	}
	
	public double getDX() {
		return dx;
	}
	
	public double getDY() {
		return dy;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void changeX(int dx) {
		x += dx;
	}
	
	public void changeY(int dy) {
		y +=dy ;
	}
	
	public Camera (SystemState state) {
		this(0,0,state);
	}
	
	public void setMapRenderer(MapRenderer mr) {
		this.mapRenderer = mr;
	}
	
	public Camera (int x, int y, SystemState s) {
		this.x = x;
		this.y = y;
		this.state = s;
	}

	
	public void updateCamera(double dx, double dy) {
		if (!stopped) {
			this.dx = dx;
			this.dy = dy;
			x+=dx;
			y+=dy;
			x=Math.max(0,this.x);
			x=Math.min(x,mapRenderer.getTileSize()*state.getGameState().getMap().getMap().size()-getMainWindow().getScreenWidth());
			y=Math.max(0,this.y);
			y = Math.min(y, mapRenderer.getTileSize()*state.getGameState().getMap().getMap().get(0).size()-getMainWindow().getScreenHeight());
			state.getGameState().updateEntityPositions();
		}
	}

	public void snapToEntity(double x, double y) {
		if (!stopped) {
			this.x = Math.max(0,x - getMainWindow().getScreenWidth()/2);
			this.y = Math.max(0,y - getMainWindow().getScreenHeight()/2);
		}
		
	}
	
	public void setStop(boolean b) {
		stopped = b;
	}
	
}
