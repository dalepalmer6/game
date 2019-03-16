package menu;

import java.awt.Color;
import java.awt.Graphics;

import canvas.Drawable;

public class TextBox implements Drawable {
	private String text;
	private String portraitImage;
	private int x;
	private int y;
	private int width;
	private int height;
	
	public TextBox(String text) {
		//default location
		this.x = 0;
		this.y = 300;
		this.width = 400;
		this.height = 100;
		this.text = text;
	}
	
//	@Override
//	public void draw(Graphics g) {
//		// TODO Auto-generated method stub
//		g.fillRect(this.x,this.y,this.width,this.height);
//		Color c = g.getColor();
//		g.setColor(Color.white);
//		g.drawString(this.text, this.x + 5, this.y+15);
//		g.setColor(c);
//	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}
	
}
