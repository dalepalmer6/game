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
import global.InputController;
import global.MenuStack;

public class MenuItem extends LeftClickableItem implements MenuItemInterface, Hoverable, Drawable, Clickable {
	protected String text;
	private Color textColor = Color.white;
	private Color bgColor = Color.black;
	private String imageNeutral = "button.png";
	private String imageHovered = "button2.png";
	private String image = this.imageNeutral;
	private int width = 300;
	private int height = 100;
	private int x;
	private int y;
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
	}
	public void setY(int y){
		this.y = y;
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
	}
	
	public MenuItem(String t, int x, int y, int width, int height, StartupNew m) {
		state = m;
		text = t;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
//		m.render(this.image,this.text,x,y,width,height);
	}

	public String prepareToExecute() {
		state.getSelectionStack().push(this);
		execute();
		return null;
	}
	
	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
