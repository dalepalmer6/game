package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import font.Text;
import global.InputController;
import global.MenuStack;

public class MenuItem extends LeftClickableItem implements MenuItemInterface, Hoverable, Drawable, Clickable {
	protected String text;
	protected Text textObject;
	private Color textColor = Color.white;
	private Color bgColor = Color.black;
	private String imageNeutral = "button.png";
	private String imageHovered = "button2.png";
	private String image = this.imageNeutral;
	protected StartupNew state;
	
	public boolean hovered(double mousex, double mousey) {	
		if (mousex >= x && mousex <= x + width && mousey >= y && mousey <=y + height) {
			return true;
		}
		return false;
	}
	
	public void hoveredAction() {
		setHoveredImage();
	}
	
	public void unhoveredAction() {
		setNeutralImage();
	}
	
	public void setNeutralImage() {
		this.image = this.imageNeutral;
	}
	
	public void setHoveredImage() {
		this.image = this.imageHovered;
	}
	
	public Color getTextColor() {
		return this.textColor;
	}
	public Color getBGColor(){
		return this.bgColor;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public void setX(int x){
		this.x = x;
		this.textObject.setX(x);
	}
	public void setY(int y){
		this.y = y;
		this.textObject.setY(y);
	}
	public void setWidth(int wid){
		this.width = wid;
	}
	public void setHeight(int height){
		this.height = height;
	}
	public void setTextColor(Color color){
		this.textColor = color;
	}
	public void setBGColor(Color color){
		this.bgColor = color;
	}
	
	public MenuItem(String t, int x, int y, StartupNew m) {
		state = m;
		text = t;
		this.x = x;
		this.y = y;
		textObject = new Text(true,t,x,y,1000,1000,state.charList);
	}
	
	public MenuItem(String t, int x, int y, int width, int height, StartupNew m) {
		state = m;
		text = t;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.textObject = new Text(true,t,x,y,1000,1000,m.charList);
	}
	
	public void setText(String t) {
		this.text = t;
	}
	
	public String getText() {
		return this.text;
	}
	
	@Override
	public void draw(MainWindow m) {
		//for each MenuItem, draw to the canvas
//		m.setTexture("img/" + this.image);
//		m.renderTile(x,y,width,height,0,0,490,234);
		textObject.draw(m);
	}

	public String prepareToExecute() {
		state.getSelectionStack().push(this);
		executeButton();
		return null;
	}
	
	public void updateAnim() {
		
	}
	
	public String executeButton() {
		state.setSFX("curshoriz.wav");
		state.playSFX();
		return execute();
	}
	
	@Override
	public String execute() {
		// TODO Auto-generated method stub
		
		return null;
	}

}
