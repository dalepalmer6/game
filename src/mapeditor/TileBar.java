package mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import org.lwjgl.input.Mouse;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import global.GlobalVars;

public class TileBar implements Drawable, Hoverable, Clickable {
	private int x;
	private int y;
	private int width;
	private int height;
	private int widthInTiles;
	private int heightInTiles;
	private int hoveredTileId;
	private int MAX_HEIGHT_IN_TILES = 10;
	private int MAX_WIDTH_IN_TILES =1;
	
	public TileBar() {
		//set location
		x = (21 + 3) * GlobalVars.TILE_SIZE;
		y = 3 * GlobalVars.TILE_SIZE;
		//create a grid that shows the tiles
		int numTiles = GlobalVars.tileMap.size();
		int i = 0; int j = 0;
		while (i < numTiles) {
			if (i % this.MAX_WIDTH_IN_TILES == 0) {
				j++;
			}
			i++;
		}
		if (j > 1) {
			i = this.MAX_WIDTH_IN_TILES;
		}
		this.width = i*GlobalVars.TILE_SIZE;
		this.height = j*GlobalVars.TILE_SIZE;
		this.widthInTiles = i;
		this.heightInTiles = j;
	}
	
	public Point getTilePosition() {
		double tileX = Math.floor((-this.x + GlobalVars.mainWindow.getMouseCoordinates().getX())/GlobalVars.TILE_SIZE);
		double tileY = Math.floor((-this.y + GlobalVars.mainWindow.getMouseCoordinates().getY())/GlobalVars.TILE_SIZE);
		if (tileX > this.widthInTiles || tileX < 0) {
			tileX = -1;
		}
		if (tileY > this.heightInTiles || tileY < 0) {
			tileY = -1;
		}
		Point xy = new Point();
		xy.setLocation(tileX,tileY);
		return xy;
	}
	
	public void setHoveredTileId() {
		Point xy = getTilePosition();
		int index = xy.x + (xy.y * this.widthInTiles);
		this.hoveredTileId = GlobalVars.tileMap.getTile(index).getId();
	}
	
	public int getHoveredTile() {
		return this.hoveredTileId;
	}
	
	public void drawCoordinates() {
		Point xy = getTilePosition();
//		System.out.println("(" + xy.getX() + "," + xy.getY() + ")");
	}
	
	@Override
	public void draw() {
		// read the tileMap and for each tile, draw to the next open spot
		TileHashMap tm = GlobalVars.tileMap;
		int i = 0;
		int j = 0;
		while (j < this.heightInTiles) {
			int drawingX = i * GlobalVars.TILE_SIZE;
			int drawingY = j * GlobalVars.TILE_SIZE;
			int tileNo =  i+(j*this.widthInTiles);
			GlobalVars.mainWindow.render(GlobalVars.tileMap.getTile(tileNo).getImageName(),drawingX + this.x,drawingY + this.y,GlobalVars.TILE_SIZE,GlobalVars.TILE_SIZE);
			i++;
			if (i == this.widthInTiles) {
				i=0;
				j++;
			}
		}
		drawCoordinates();
	}

	@Override
	public boolean hovered(double mousex, double mousey) {
		if (mousex >= this.x && mousex <= this.x + this.width && mousey >= this.y && mousey <= this.y + this.height) {
			return true;
		}
		return false;
	}

	@Override
	public void hoveredAction() {
		// TODO Auto-generated method stub
		setHoveredTileId();
	}

	@Override
	public void unhoveredAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		System.out.println("Setting edit tool to " + getHoveredTile());
		GlobalVars.mapPreviewEditTool = getHoveredTile();
	}

	@Override
	public boolean isMouseDown() {
		// TODO Auto-generated method stub
		if (Mouse.isButtonDown(0)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isMouseRightDown() {
		// TODO Auto-generated method stub
		return false;
	}

}
