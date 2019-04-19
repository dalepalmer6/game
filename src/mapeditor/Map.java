package mapeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import gamestate.DoorEntity;
import gamestate.Entity;
import gamestate.Player;
import gamestate.SpritesheetCoordinates;
import menu.StartupNew;
import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;
import tiles.TileInstance;
import tiles.types.Tree;

public class Map {
	private String mapId;
//	public ArrayList<ArrayList<Integer>> tileMap = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tileMapBG = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tileMapFG = new ArrayList<ArrayList<Integer>>();
	
//	public ArrayList<ArrayList<TileInstance>> tileInstanceMap = new ArrayList<ArrayList<TileInstance>>();
	public ArrayList<ArrayList<TileInstance>> tileInstanceMapBG = new ArrayList<ArrayList<TileInstance>>();
	public ArrayList<ArrayList<TileInstance>> tileInstanceMapFG = new ArrayList<ArrayList<TileInstance>>();
	
	public String currentMapLayer;
	
	public ArrayList<ArrayList<Integer>> layerMap;
	public ArrayList<ArrayList<TileInstance>> layerMapTileInstance;
	
	private ArrayList<Entity> entitiesInMap = new ArrayList<Entity>();
	private TileHashMap tilesGlobalMap;
	private int width;
	private int height;
	private StartupNew state;
	private String tileset;
	private String pathToMaps;
	private String pathToTilesets;
	
	public void setTileset(String s) {
		tileset = s;
	}
	
	public String getTileset() {
		return tileset;
	}
	
	public ArrayList<ArrayList<Integer>> getLayer() {
		return layerMap;
	}
	
	public void readTileSet() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathToMaps + mapId + "/properties.csv"));
			br.readLine();
			String row = "";
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				tileset = pathToTilesets + split[0] + ".png";
			}
			state.loadAllTiles(tileset);
//			state.setTileSet(tileset);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setChangeMap(String layer) {
		switch (layer) {
		case "BG" : currentMapLayer = layer;
					layerMap = tileMapBG;
					layerMapTileInstance = tileInstanceMapBG;
					break;
//		case "MID" : layerMap = tileMap;
//					layerMapTileInstance = tileInstanceMap;
//					break;
		case "FG" : layerMap = tileMapFG;
					currentMapLayer = layer;
					layerMapTileInstance = tileInstanceMapFG;
					break;
		}
	}
	
	public void saveMap() {
		try {
			PrintWriter pw = new PrintWriter(new File(pathToMaps + mapId + "/fg.map"));
			pw.write(toString(tileMapFG));
			pw.flush();
			pw.close();
			pw = new PrintWriter(new File(pathToMaps +mapId + "/bg.map"));
			pw.write(toString(tileMapBG));
			pw.flush();
			pw.close();
			pw = new PrintWriter(new File(pathToMaps + mapId +"/entities.csv"));
			String writeEntities = "TEXTURE,X,Y,WIDTH,HEIGHT,TEXT,NAME\n";
			for (Entity e : entitiesInMap) {
				if (!(e instanceof DoorEntity)) {
					writeEntities += e.getTexture() + "," + (e.getX()) + "," + (e.getY()) + "," + e.getWidth() + "," + e.getHeight() + "," + e.getText() +  "," + e.getName() +"\n"; 
				}
			}
			pw.write(writeEntities);
			pw.flush();
			pw.close();
			pw = new PrintWriter(new File(pathToMaps + mapId +"/doors.csv"));
			String writeDoors = "DESCRIPTION,X,Y,WIDTH,HEIGHT,DESTINATION_MAP,DESTX,DESTY,TEXT\n";
			for (Entity e : entitiesInMap) {
				if ((e instanceof DoorEntity)) {
					writeDoors += ((DoorEntity)e).getDesc() + "," + (e.getX()) + "," + (e.getY()) + "," + e.getWidth() + "," + e.getHeight() + "," + ((DoorEntity)e).getDestMap() + "," + (((DoorEntity)e).getDestX()) + "," + (((DoorEntity)e).getDestY()) + "," + e.getText() +"\n"; 
				}
			}
			pw.write(writeDoors);
			pw.flush();
			pw.close();
		} catch(FileNotFoundException e) {
			
		} 
		System.out.println("Successfully saved.");
	}
	
	public String toString(ArrayList<ArrayList<Integer>> tileMap) {
		String string = "";
		for (ArrayList<Integer> row : tileMap) {
			for (int i : row) {
				string += i + ",";
			}
			string += "\n";
		}
		return string;
	}
	
	public void parseMap(int scale, String mapId) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Entity e : entitiesInMap) {
			if (e instanceof Player) {
				players.add((Player) e);
			}
		}
		
		this.entitiesInMap.clear();
		entitiesInMap.addAll(players);
		this.mapId = mapId;
		readTileSet();
		String pathToCurrentMap = pathToMaps + mapId + "/";
		parseMapFG(new File(pathToCurrentMap + "fg.map"));
		parseMapBG(new File(pathToCurrentMap + "bg.map"));
		parseEntities(new File(pathToCurrentMap + "entities.csv"),scale);
		parseDoors(new File(pathToCurrentMap + "doors.csv"),scale);
	}
	
	public void parseDoors(File ent,int scale) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(ent));
			String row = "";
			br.readLine();//skip headers
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				String desc = split[0];
				int x = Integer.parseInt(split[1]);
				int y = Integer.parseInt(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);
				String destMap = split[5];
				int destX = Integer.parseInt(split[6]);
				int destY = Integer.parseInt(split[7]);
				String text = split[8];
//				Entity e = state.getEntityFromEnum(name).createCopy(scale*x,scale*y,scale*width,scale*height);
//				e.setText(text);
				DoorEntity e = new DoorEntity(desc,x*scale,y*scale,width*scale,height*scale,state,destX*scale,destY*scale,destMap,text);
				entitiesInMap.add(e);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseEntities(File ent,int scale) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(ent));
			String row = "";
			br.readLine();//skip headers
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				String name = split[6];
				int x = Integer.parseInt(split[1]);
				int y = Integer.parseInt(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);
				String text = split[5];
				Entity e = state.getEntityFromEnum(name).createCopy(scale*x,scale*y,scale*width,scale*height);
				e.setText(text);
				entitiesInMap.add(e);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void parseMapFG(File map)  {
		
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
				tileMapFG = rows;
				this.width = tileMapFG.size();
				this.height = tileMapFG.get(0).size();
			}catch (IOException e) {
				
			}
			ArrayList<ArrayList<TileInstance>> rowstiles = new ArrayList<ArrayList<TileInstance>>();
			ArrayList<TileInstance> rowtiles = new ArrayList<TileInstance>();
			for (int j = 1; j < tileMapFG.size()-1; j++) {
				//for every row
				for (int i = 1; i < tileMapFG.get(0).size()-1; i++) {
					//for every column
					int val = this.tileMapFG.get(j).get(i);
					Tile tile = tilesGlobalMap.getTile(val);
					layerMap = tileMapFG;
					int instance = inspectSurroundings(i,j);
					rowtiles.add(tile.getInstance(instance));
				}
				rowstiles.add(rowtiles);
				rowtiles = new ArrayList<TileInstance>();
			}
			tileInstanceMapFG = rowstiles;
				
			} catch(FileNotFoundException e) {
				
			} 
		}
		
	
	public void parseMapBG(File map)  {
		
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
				tileMapBG = rows;
				this.width = tileMapBG.size();
				this.height = tileMapBG.get(0).size();
			}catch (IOException e) {
				
			}
			ArrayList<ArrayList<TileInstance>> rowstiles = new ArrayList<ArrayList<TileInstance>>();
			ArrayList<TileInstance> rowtiles = new ArrayList<TileInstance>();
			for (int j = 1; j < tileMapBG.size()-1; j++) {
				//for every row
				for (int i = 1; i < tileMapBG.get(0).size()-1; i++) {
					//for every column
					int val = this.tileMapBG.get(j).get(i);
					Tile tile = tilesGlobalMap.getTile(val);
					layerMap = tileMapBG;
					int instance = inspectSurroundings(i,j);
					rowtiles.add(tile.getInstance(instance));
				}
				rowstiles.add(rowtiles);
				rowtiles = new ArrayList<TileInstance>();
			}
			tileInstanceMapBG = rowstiles;
				
			} catch(FileNotFoundException e) {
				
			} 
		}
	
	public int inspectSurroundings(int x , int y) {
		//check the adjacent tiles, if they're the same, then draw the appropriate instance of the tile
		//middle tile that we are comparing with
		int instance = 0;
		if (tilesGlobalMap.getTile(layerMap.get(y).get(x)) instanceof SingleInstanceTile) {
			return 0;
		}
		if (x == 0 || y == 0) {
			instance = 0;
			return 0;
		}
		int mid = layerMap.get(y).get(x);
		
		if (tilesGlobalMap.getTile(layerMap.get(y).get(x)) instanceof PremadeTileObject) {
			int index = 0;
			int i = 1;
			int j = 1;
			while (i < tilesGlobalMap.getTile(mid).getWidth()) {
				if (x-i < 0) {
					break;
				}
				if (layerMap.get(y).get(x-i) == mid) {
					index++;
				} else {
					break;
				}
				i++;
			}
			while (j < tilesGlobalMap.getTile(mid).getHeight()) {
				if (y-j < 0) {
					break;
				}
				if (layerMap.get(y-j).get(x) == mid) {
					index+=tilesGlobalMap.getTile(mid).getWidth();
				} else {
					break;
				}
				j++;
			}
			return index;		
		}
		
		int l = layerMap.get(y).get(x-1);
		int r = layerMap.get(y).get(x+1);
		int u = layerMap.get(y+1).get(x);
		int d = layerMap.get(y-1).get(x);
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
			if (tilesGlobalMap.getTile(layerMap.get(y).get(x)) instanceof MultiInstanceTile) {
				if (tilesGlobalMap.getTile(layerMap.get(y).get(x)).getNumInstances() > 10) {
					if (mid != layerMap.get(y-1).get(x+1)) {
						return 10; 
					}
				}
				if (tilesGlobalMap.getTile(layerMap.get(y).get(x)).getNumInstances() > 11) {
					if (mid != layerMap.get(y+1).get(x-1)) {
						return 11;
					}
				}
				if (tilesGlobalMap.getTile(layerMap.get(y).get(x)).getNumInstances() > 12) {
					if (mid != layerMap.get(y-1).get(x-1)) {
						return 12;
					}
				}
				if (tilesGlobalMap.getTile(layerMap.get(y).get(x)).getNumInstances() > 13) {
					if (mid != layerMap.get(y+1).get(x+1)) {
						return 13;
					}
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
	
	public void expandMap(int expand) {
		ArrayList<ArrayList<Integer>> newmapBG = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> newmapFG = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < tileMapBG.size(); i++) {
			ArrayList<Integer> rowBG = tileMapBG.get(i);
			ArrayList<Integer> rowFG = tileMapFG.get(i);
			for (int c = 0; c < expand; c++) {
				rowBG.add(0);
				rowFG.add(0);
			}
//			newmap.add(row);
			newmapBG.add(rowBG);
			newmapFG.add(rowFG);
		}
		for (int i = 0; i < expand; i++) {
//			ArrayList<Integer> emptyRow = new ArrayList<Integer>();
			ArrayList<Integer> emptyRowBG = new ArrayList<Integer>();
			ArrayList<Integer> emptyRowFG = new ArrayList<Integer>();
			for (int c = 0; c < newmapBG.get(0).size(); c++) {
//				emptyRow.add(0);
				emptyRowBG.add(0);
				emptyRowFG.add(0);
			}
//			newmap.add(emptyRow);
			newmapBG.add(emptyRowBG);
			newmapFG.add(emptyRowFG);
		}
//		this.tileMap = newmap;
		this.tileMapBG = newmapBG;
		this.tileMapFG = newmapFG;
		setChangeMap(currentMapLayer);
		this.width = newmapBG.size();
		this.height = newmapBG.get(0).size();
		System.out.println("New Size = " + newmapBG.size() + "width, by " + newmapBG.get(0).size() + "height");
	}
	
	public ArrayList<ArrayList<Integer>> getMap() {
		return layerMap;
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
		return layerMap.get(y).get(x);
	}
	
	public void setTile(int tileId, int x, int y) {
		try {
			this.layerMap.get(y).set(x,tileId);
		} catch (IndexOutOfBoundsException e){
			System.err.println("Index out of bounds, cant add tile " + tileId +
					" to (" + x + ", " + y + ")");
		}
//		this.tileMap[y][x] = tileId;
	}
	
	public Map(String mId, int width, int height, TileHashMap tm, StartupNew m) {
		pathToTilesets = m.getPathToTilesets();
		this.tileset = pathToTilesets + "magicant.png";
		m.loadAllTiles(tileset);
		this.state = m;
		this.width = width;
		this.height = height;
		tilesGlobalMap = tm;
		this.mapId = mId;
		for (int i = 0; i < height; i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < width; j++) {
				list.add(0);
			}
			tileMapBG.add(list);
		}
		for (int i = 0; i < height; i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < width; j++) {
				list.add(0);
			}
			tileMapFG.add(list);
		}
		layerMap = tileMapBG;
		pathToMaps = "maps/";
	}

	public String getMapId() {
		// TODO Auto-generated method stub
		return mapId;
	}

	public ArrayList<Entity> getEntities() {
		return entitiesInMap;
	}
	
//	public ArrayList<Entity> getEntities(int x, int y) {
//		ArrayList<Entity> entitiesInRegion = new ArrayList<Entity>();
//		
//	}
	
	public void addToEntities(Entity newEntity) {
		// TODO Auto-generated method stub
		entitiesInMap.add(newEntity);
	}

	public ArrayList<Entity> getEntitiesInView(int xLeft, int yTop) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity e : entitiesInMap) {
			if (e.getX() < xLeft + state.getMainWindow().getScreenWidth() + 1000 && e.getX() > xLeft - 1000
					&& e.getY() > yTop - 1000  && e.getY() < yTop + state.getMainWindow().getScreenHeight() + 1000) {
				list.add(e);
			}
		}
		return list;
		// TODO Auto-generated method stub
		
	}
}
