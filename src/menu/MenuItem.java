package menu;

import java.awt.Color;

import menu.text.TextEngine;
import system.MainWindow;
import system.SystemState;
import system.interfaces.Clickable;
import system.interfaces.Drawable;
import system.interfaces.Hoverable;

public class MenuItem extends LeftClickableItem implements MenuItemInterface, Hoverable, Drawable, Clickable {
	protected String text;
	protected TextEngine textObject;
	private Color textColor = Color.white;
	private Color bgColor = Color.black;
	private String imageNeutral = "button.png";
	private String imageHovered = "button2.png";
	private String image = this.imageNeutral;
	protected SystemState state;
	protected double shakeApplyY = 0;
	protected int drawingY;
	protected boolean hovered;
	protected double targetX;
	protected double targetY;
	
	public void setState(SystemState state) {
		this.state = state;
	}
	
	public int getWidthOfText() {
		return textObject.getWidth();
	}
	
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
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public void setX(double x){
		this.x = x;
		this.textObject.setX(x);
	}
	public void setY(double y){
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
	
	public MenuItem(String t, double x, double y, SystemState m) {
		state = m;
		text = t;
		this.x = x;
		this.y = y;
		targetX = x;
		targetY = y;
		textObject = state.createTextEngine(true,t,(int)x,(int)y,0,0);
		textObject.setAsSingleString();
	}
	
	public MenuItem(String t, double x, double y, int width, int height, SystemState m) {
		state = m;
		text = t;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		targetY = y;
//		if (!t.equals("")) {
		this.textObject = state.createTextEngine(true,t,width,height,width,height);
//		}
		
		
	}
	
	public MenuItem( double x, double y, int width, int height, SystemState m) {
		state = m;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		targetY = y;
	}
	
	public void setText(String t) {
		this.text = t;
		textObject.setText(t);
	}
	
	public String getText() {
		return this.text;
	}
	
	@Override
	public void draw(MainWindow m) {
		//for each MenuItem, draw to the canvas
		textObject.draw(m);
	}

	public void setHovered(boolean b) {
		hovered = b;
	}

	public String prepareToExecute() {
		state.getSelectionStack().push(this);
		executeButton();
		return null;
	}
	
	public void updateAnim() {
		drawingY = (int) (y + shakeApplyY);
		approachTargetPos();
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

	public void setShakingY(double applyShake) {
		// TODO Auto-generated method stub
		shakeApplyY = applyShake;
	}

	public void approachTargetPos() {
		// TODO Auto-generated method stub
		if (targetY > y) {
			y+=8;
			if (textObject != null) {
				textObject.setY(textObject.getY()+8);
			}
			
		} else if (targetY < y) {
			y-=8;
			if (textObject != null) {
				textObject.setY(textObject.getY()-8);
			}
		}
	}

	public void setTargetPosY(int i) {
		// TODO Auto-generated method stub
		targetY = i;
	}

}
