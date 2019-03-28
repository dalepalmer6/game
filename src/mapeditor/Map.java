package mapeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import gamestate.Entity;
import tiles.SingleInstanceTile;
import tiles.TileInstance;
import tiles.types.Tree;

public class Map {
	private String mapId;
	private ArrayList<ArrayList<Integer>> tileMap = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<TileInstance>> tileInstanceMap = new ArrayList<ArrayList<TileInstance>>();
	private ArrayList<Entity> entitiesInMap = new ArrayList<Entity>();
	private TileHashMap tilesGlobalMap;
	private int width;
	private int height;
	
	@Override
	public String toString() {
		String string = "";
		for (ArrayList<Integer> row : tileMap) {
			for (int i : row) {
				string += i + ",";
			}
			string += "\n";
		}
		return string;
	}
	
	public void parseMap(File map)  {
		ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(map));
			String row = "";
			try {
				while ((row = br.readLine()) != null) {
					String[] split = row.split(",");
					ArrayList<Integer> rowAsInts = new ArrayList<Integer>();
					for (String s : split) {
						rowAsInts.add(Integer.parseInt(s));
					}
					rows.add(rowAsInts);
				}
				br.close();
				tileMap = rows;
				this.width = tileMap.size();
				this.height = tileMap.get(0).size();
			}catch (IOException e) {
				
			}
			ArrayList<ArrayList<TileInstance>> rowstiles = new ArrayList<ArrayList<TileInstance>>();
			ArrayList<TileInstance> rowtiles = new ArrayList<TileInstance>();
			for (int j = 1; j < tileMap.size()-1; j++) {
				//for every row
				for (int i = 1; i < tileMap.get(0).size()-1; i++) {
					//for every column
					int val = this.tileMap.get(j).get(i);
					Tile tile = tilesGlobalMap.getTile(val);
					int instance = inspectSurroundings(i,j);
					rowtiles.add(tile.getInstance(instance));
				}
				rowstiles.add(rowtiles);
				rowtiles = new ArrayList<TileInstance>();
			}
			tileInstanceMap = rowstiles;
			
		} catch(FileNotFoundException e) {
			
		} 
	}
	
	public int inspectSurroundings(int x , int y) {
		//check the adjacent tiles, if they're the same, then draw the appropriate instance of the tile
		//middle tile that we are comparing with
		int instance = 0;
		if (tilesGlobalMap.getTile(tileMap.get(y).get(x)) instanceof SingleInstanceTile) {
			return 0;
		}
		if (x == 0 || y == 0) {
			instance = 0;
			return 0;
		}
		int mid = tileMap.get(y).get(x);
		int l = tileMap.get(y).get(x-1);
		int r = tileMap.get(y).get(x+1);
		int u = tileMap.get(y+1).get(x);
		int d = tileMap.get(y-1).get(x);
		if (mid != u && mid != l && mid == r && mid == d) {
			return 7;
		}
		if (mid != u && mid == l && mid == r && mid == d) {
			return 8;
		}
		if (mid != u && mid == l && mid != r && mid == d) {
			return 9;
		}
		if (mid == u && mid != l && mid == r && mid == d) {
			return 4;
		}
		if (mid == u && mid == l && mid == r && mid == d) {
			//check the diagonals for special cases
			if (tilesGlobalMap.getTile(tileMap.get(y).get(x)) instanceof Tree) {
				if (mid != tileMap.get(y+1).get(x+1)) {
					return 10;
				}
				if (mid != tileMap.get(y+1).get(x-1)) {
					return 11;
				}
			}
			if (tilesGlobalMap.getTile(tileMap.get(y).get(x)) instanceof tiles.types.Path) {
				if (mid != tileMap.get(y+1).get(x+1)) {
					return 11;
				}
				if (mid != tileMap.get(y+1).get(x-1)) {
					return 10;
				}
			}
			return 5;
		}
		if (mid == u && mid == l && mid != r && mid == d) {
			return 6;
		}
		if (mid == u && mid != l && mid == r && mid != d) {
			return 1;
		}
		if (mid == u && mid == l && mid == r && mid != d) {
			return 2;
		}
		if (mid == u && mid == l && mid != r && mid != d) {
			return 3;
		}
		return 0;
	}
	
	public void expandMap(int xExpand, int yExpand) {
		ArrayList<ArrayList<Integer>> newmap = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < tileMap.size(); i++) {
			ArrayList<Integer> row = tileMap.get(i);
			for (int c = 0; c < xExpand; c++) {
				row.add(7);
			}
			newmap.add(row);
		}
		for (int i = 0; i < yExpand; i++) {
			ArrayList<Integer> emptyRow = new ArrayList<Integer>();
			for (int c = 0; c < tileMap.get(0).size(); c++) {
				emptyRow.add(7);
			}
			newmap.add(emptyRow);
		}
		this.tileMap = newmap;
		this.width = newmap.size();
		this.height = newmap.get(0).size();
		System.out.println("New Size = " + newmap.size() + "width, by " + newmap.get(0).size() + "height");
	}
	
	public ArrayList<ArrayList<Integer>> getMap() {
		return tileMap;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Tile getTile(int x, int y) {
		int tId = getTileId(x,y);
		return tilesGlobalMap.getTile(tId);
	}
	
	public int getTileId(int x, int y) {
		return this.tileMap.get(y).get(x);
	}
	
	public void setTile(int tileId, int x, int y) {
		try {
			this.tileMap.get(y).set(x,tileId);
		} catch (IndexOutOfBoundsException e){
			System.err.println("Index out of bounds, cant add tile " + tileId +
					" to (" + x + ", " + y + ")");
		}
//		this.tileMap[y][x] = tileId;
	}
	
	public Map(String mId, int width, int height, TileHashMap tm) {
		this.width = width;
		this.height = height;
		tilesGlobalMap = tm;
		this.mapId = mId;
		for (int i = 0; i < height; i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < width; j++) {
				list.add(0);
			}
			tileMap.add(list);
		}
		//tileMap = new int[width][height];
	}

	public String getMapId() {
		// TODO Auto-generated method stub
		return mapId;
	}
}
