package mapeditor;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import canvas.Clickable;
import canvas.Controllable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import global.InputController;
import mapeditor.tools.MapTool;
import menu.MenuItem;
import menu.StartupNew;

public class TilePreview extends MenuItem implements Controllable, Drawable, Hoverable, Clickable{
	private Tile currentTile;
	private int currentCollisionEditValue = 1;
	
	public void drawGrid(MainWindow m) {
		m.setTexture("img\\line.png");
		for (int i = (int) this.x; i <= this.x + width ; i+= 64) {
			//for every row
			for (int j = (int) this.y; j <= this.y + height; j+= 64) {
				//for every column
				m.renderTile(this.x,j,width,1,1,1,1,1);
			}
			m.renderTile(i,this.y,1,height,1,1,1,1);
		}
	}
	
	public void draw(MainWindow m) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,state.tilesetTexture.getTextureID());
		m.renderTiles(x, y, 256,
				256, currentTile.getDx(0), currentTile.getDy(0), currentTile.getDw(0), currentTile.getDh(0),false);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,state.getTextureAtlas().getTexture().getTextureID());
		drawGrid(m);
	}
	
	public TilePreview(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		width = 256;
		height = 256;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		editMap(currentCollisionEditValue);
		return null;
	}
	
	public Point getMouseCoordinates() {
		//relative to the tool
		int x = (int) (Mouse.getX() - this.x);
		int y = (int) (Display.getHeight() - Mouse.getY() - this.y);
		return new Point(x, y);
	}
	
	public Point getTilePosition() {
		double tileX = Math.floor((getMouseCoordinates().getX())/64+1);
		double tileY = Math.floor((getMouseCoordinates().getY())/64+1);
		if (tileX > 4 || tileX < 0) {
			tileX = -1;
		}
		if (tileY > 4 || tileY < 0) {
			tileY = -1;
		}
		Point xy = new Point();
		xy.setLocation(tileX,tileY);
		return xy;
	}
	
	public void editMap(int id) {
		//on click, change the collision data to the current Collision value
//			input.setHoldableState(true);
			Point xyMouse = getMouseCoordinates();
//			int xMouse = (int) xyMouse.getX();
//			int yMouse = (int) xyMouse.getY();
			Point xy = getTilePosition();
			int x = (int) xy.getX() - 1;
			int y = (int) xy.getY() - 1;
			if (x < 0 || x > 4) {
				return;
			}
			if (y < 0 || y > 4) {
				return;
			}
			editCollisionData(x,y,id);
			
			String vals = currentTile.getInstance(0).getCollisionData();
			System.out.println(vals);
	}

	private void editCollisionData(int x, int y, int colval) {
		currentTile.setNewCollisionValue(x,y,colval);
	}
	
	public boolean hovered(double mousex, double mousey) {
		if (mousex >= this.x && mousex <= this.x + this.width && mousey >= this.y && mousey <= this.y + this.height) {
			return true;
		}
		return false;
	}
	
	@Override
	public void handleInput(InputController input) {
		input.setHoldable(true);
		if (input.getSignals().get("ENTER")) {
			saveTilesetData();
		} else if (input.getSignals().get("MOUSE_RIGHT_DOWN")) {
			executeRightClick();
		} else if (input.getSignals().get("MOUSE_LEFT_DOWN")) {
			execute();
		}
	}
	
	private void executeRightClick() {
		// TODO Auto-generated method stub
		editMap(0);
	}

	public void saveTilesetData() {
		try {
			PrintWriter pw = new PrintWriter(new File("output.ts"));
			pw.print(state.tileMap.tilesOutput());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void hoveredAction() {
	}

	@Override
	public void unhoveredAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTile(Tile tile) {
		// TODO Auto-generated method stub
		currentTile = tile;
	}

}
