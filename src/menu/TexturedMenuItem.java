package menu;

import canvas.MainWindow;

public class TexturedMenuItem extends MenuItem {
	protected int dx;
	protected int dy;
	protected int dw;
	protected int dh;
	protected String texture;
	private int borderDX;
	private int borderDY;
	private int borderDW;
	private int borderDH;
	private int hoveredDy;
	
	public TexturedMenuItem(String text, double x, double y, int width, int height,  StartupNew state, String texture, int dx, int dy, int dw, int dh) {
		super(text,x,y,width,height,state);
		this.texture = texture;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
		this.borderDX = 88;
		this.borderDY = 0;
		this.borderDW = 18;
		this.borderDH = 16;
		this.hoveredDy = dy + dh;
	}
	
	public TexturedMenuItem(double x, double y, int width, int height,  StartupNew state, String texture, int dx, int dy, int dw, int dh) {
		super(x,y,width,height,state);
		this.texture = texture;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
		this.borderDX = 88;
		this.borderDY = 0;
		this.borderDW = 18;
		this.borderDH = 16;
		this.hoveredDy = dy + dh;
	}
	
	public void setHovered(int hdx, int hdy, int hdw, int hdh) {
		this.borderDX = hdx;
		this.borderDY = hdy;
		this.borderDW = hdw;
		this.borderDH = hdh;
	}
	
	public void draw(MainWindow m) {
		state.getMainWindow().setTexture("img\\" + texture);
		if (hovered) {
			m.renderTile(x-12,y-8,borderDW*4,borderDH*4,borderDX,borderDY,borderDW,borderDH);
			m.renderTile(x,y,width,height,dx,hoveredDy,dw,dh);
		} else {
			m.renderTile(x,y,width,height,dx,dy,dw,dh);
		}
		
		
	}
	
}
