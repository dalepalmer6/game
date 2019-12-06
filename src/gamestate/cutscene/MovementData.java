package gamestate.cutscene;

/*
 * Data structure that stores the direction, state, and magnitude of movement
 * 
 * */
public class MovementData {
	private double x;
	private double y;
	private String state;
	private String dirX;
	private String dirY;
	
	public MovementData(double x, double y, String s, String dirX, String dirY) {
		this.x = x;
		this.y = y;
		this.state = s;
		this.dirX = dirX;
		this.dirY = dirY;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public String getState() {
		return state;
	}
	
	public String getDirectionX() {
		return dirX;
	}
	
	public String getDirectionY() {
		return dirY;
	}

	public void setX(double x) {
		// TODO Auto-generated method stub
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setState(String state) {
		this.state = state;
	}

	public void setDirection(String dirX,String dirY) {
		this.dirX = dirX;
		this.dirY = dirY;
	}
	
	
}
