package mapeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
	private String mapId;
	private ArrayList<ArrayList<Integer>> tileMap = new ArrayList<ArrayList<Integer>>();
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
//		string = string.substring(0,string.lastIndexOf('')-1);
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
			
			
		} catch(FileNotFoundException e) {
			
		} 
//		String[] splitString = mapString.split("\n");
//		ArrayList<ArrayList<Integer>> tileMap = new ArrayList<ArrayList<Integer>>(); 
//		for (String s : splitString) {
//			ArrayList<Integer> curRow = new ArrayList<Integer>();
//			String[] row = s.split(",");
//			for (String i : row) {
//				curRow.add(Integer.parseInt(i));
//			}
//			tileMap.add(curRow);
//		}
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
		//return this.tileMap[y][x];
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
				list.add(7);
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
