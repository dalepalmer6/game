package menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import global.GlobalVars;

public class MenuItem implements MenuItemInterface, Hoverable, Drawable, Clickable{
	private String text;
	private Color textColor = Color.white;
	private Color bgColor = Color.black;
	private Color altBGColor = Color.LIGHT_GRAY;
	private String imageNeutral = "button.png";
	private String imageHovered = "button2.png";
	private String image = this.imageNeutral;
	private int width = 300;
	private int height = 100;
	private int x;
	private int y;
	
	public boolean hovered(double mousex, double mousey) {	
		if (mousex >= x && mousex <= x + width && mousey >= y && mousey <=y + height) {
			System.out.println(mousex + ", " + mousey);
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
	
	public MenuItem(String t, int x, int y) {
		text = t;
		this.x = x;
		this.y = y;
	}
	
	public MenuItem(String t, int x, int y, int width, int height) {
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
	public void draw() {
		//for each MenuItem, draw to the canvas
		GlobalVars.mainWindow.render(this.image,this.text,x,y,width,height);
	}
	
	@Override
	public void execute() {
		//extending classes need to define this method
	}

	@Override
	public boolean isMouseDown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMouseRightDown() {
		// TODO Auto-generated method stub
		return false;
	}
}
