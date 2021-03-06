package system.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import gamestate.entities.DoorEntity;
import gamestate.entities.EnemySpawnEntity;
import gamestate.entities.Entity;
import gamestate.entities.FollowingPlayer;
import gamestate.entities.HotSpot;
import gamestate.entities.Player;
import gamestate.entities.PresentEntity;
import system.SystemState;
import system.sprites.SpritesheetCoordinates;
import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;
import tiles.TileInstance;

public class Map {
	private String mapId;
	public ArrayList<ArrayList<Integer>> tileMapBase = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tileMapBG = new ArrayList<ArrayList<Integer>>();
	public ArrayList<ArrayList<Integer>> tileMapFG = new ArrayList<ArrayList<Integer>>();
	
	public ArrayList<ArrayList<TileInstance>> tileInstanceMapBase = new ArrayList<ArrayList<TileInstance>>();
	public ArrayList<ArrayList<TileInstance>> tileInstanceMapBG = new ArrayList<ArrayList<TileInstance>>();
	public ArrayList<ArrayList<TileInstance>> tileInstanceMapFG = new ArrayList<ArrayList<TileInstance>>();
	
	public String currentMapLayer;
	
	private String bgm;
	
	public ArrayList<ArrayList<Integer>> layerMap;
	public ArrayList<ArrayList<TileInstance>> layerMapTileInstance;
	
	private ArrayList<Entity> entitiesInMap = new ArrayList<Entity>();
	private TileHashMap tilesGlobalMap;
	private int width;
	private int height;
	private SystemState state;
	private String tileset;
	private String pathToMaps;
	private String pathToTilesets;
	
	private boolean ignoreTiles;
	private String actualBGM;
	
	public String getMapLayer() {
		return currentMapLayer;
	}
	
	public String getActualBGM() {
		return actualBGM;
	}
	
	public ArrayList<ArrayList<Integer>> getLayerMap() {
		return layerMap;
	}
	
	public ArrayList<ArrayList<TileInstance>> getLayerInstances() {
		return layerMapTileInstance;
	}
	
	public String getBGM() {
		return bgm;
	}
	
	public void setTileset(String s) {
		tileset = s;
	}
	
	public String getTileset() {
		return tileset;
	}
	
	public ArrayList<ArrayList<Integer>> getLayer() {
		return layerMap;
	}
	
	public void readProperties() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(pathToMaps + mapId + "/properties.csv"));
			br.readLine();
			String row = "";
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				tileset = pathToTilesets + split[0] + ".png";
				bgm = split[1];
			}
			state.loadAllTiles(tileset);
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
		case "BASE":currentMapLayer = layer;
					layerMap = tileMapBase;
					layerMapTileInstance = tileInstanceMapBase;
					break;
		case "BG" : currentMapLayer = layer;
					layerMap = tileMapBG;
					layerMapTileInstance = tileInstanceMapBG;
					break;
		case "FG" : layerMap = tileMapFG;
					currentMapLayer = layer;
					layerMapTileInstance = tileInstanceMapFG;
					break;
		}
	}
	
	public void saveMap() {
		saveMap(false);
	}
	
	public void saveMap(boolean saveTextEditor) {
		try {
			PrintWriter pw;
			if (!saveTextEditor) {
				pw = new PrintWriter(new File(pathToMaps + mapId + "/base.map"));
				pw.write(toString(tileMapBase));
				pw.flush();
				pw.close();
				pw = new PrintWriter(new File(pathToMaps + mapId + "/fg.map"));
				pw.write(toString(tileMapFG));
				pw.flush();
				pw.close();
				pw = new PrintWriter(new File(pathToMaps +mapId + "/bg.map"));
				pw.write(toString(tileMapBG));
				pw.flush();
				pw.close();
			}
			
			pw = new PrintWriter(new File(pathToMaps + mapId +"/entities.csv"));
			String writeEntities = "TEXTURE,X,Y,WIDTH,HEIGHT,TEXT,NAME,APPEARFLAG,DISAPPEARFLAG\n";
			for (Entity e : entitiesInMap) {
				if (!((e instanceof DoorEntity) || (e instanceof EnemySpawnEntity) || (e instanceof PresentEntity))) {
					writeEntities += e.getTextureNoExt() + "," + (e.getX()) + "," + (e.getY()) + "," + e.getWidth() + "," + e.getHeight() + "," + e.getText() + "," + e.getName() + "," + e.getAppearFlag() + "," + e.getDisappearFlag() + "\n"; 
				}
			}
			pw.write(writeEntities);
			pw.flush();
			pw.close();
			pw = new PrintWriter(new File(pathToMaps + mapId +"/doors.csv"));
			String writeDoors = "DESCRIPTION,X,Y,WIDTH,HEIGHT,DESTINATION_MAP,DESTX,DESTY,TEXT,APPEARFLAG,DISAPPEARFLAG\n";
			for (Entity e : entitiesInMap) {
				if ((e instanceof DoorEntity) && !(e instanceof HotSpot)) {
					writeDoors += ((DoorEntity)e).getName() + "," + (e.getX()) + "," + (e.getY()) + "," + e.getWidth() + "," + e.getHeight() + "," + ((DoorEntity)e).getDestMap() + "," + (((DoorEntity)e).getDestX()) + "," + (((DoorEntity)e).getDestY()) + "," + e.getText() + "," + e.getAppearFlag() + "," + e.getDisappearFlag() + "\n"; 
				}
			}
			pw.write(writeDoors);
			pw.flush();
			pw.close();
			pw = new PrintWriter(new File(pathToMaps + mapId + "/hotspots.csv"));
			String writeHotspots = "Hotspot Name,x,y,width,height,cutscene name,APPEARFLAG,DISAPPEARFLAG\n";
			for (Entity e : entitiesInMap) {
				if (e instanceof HotSpot) {
					String csnameAbsolute = ((HotSpot)e).getCutsceneName();
					String csName = csnameAbsolute.replaceFirst(pathToMaps + mapId + "/","");
					writeHotspots += (((HotSpot)e).getName() + "," + e.getX() + "," + e.getY() + "," + e.getWidth() + "," + e.getHeight() + "," + csName + "," + e.getAppearFlag() + "," + e.getDisappearFlag() + "\n");
				}
			}
			pw.write(writeHotspots);
			pw.flush();
			pw.close();
			pw = new PrintWriter(new File(pathToMaps + mapId + "/enemyspawns.data"));
			String writeEnemyData = "index,x,y,width,height\n";
			for (Entity e : entitiesInMap) {
				if (e instanceof EnemySpawnEntity) {
					writeEnemyData += e.toString();
				}
			}
			pw.write(writeEnemyData);
			pw.flush();
			pw.close();
			pw = new PrintWriter(pathToMaps + mapId + "/presents.csv");
			String writePresentData = "x,y,item,name\n";
			for (Entity e : entitiesInMap) {
				if (e instanceof PresentEntity) {
					writePresentData += e.toString() + "\n";
				}
			}
			pw.write(writePresentData);
			pw.flush();
			pw.close();
		} catch(FileNotFoundException e) {
			
		} 
//		SystemState.out.println("Successfully saved.");
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
	
	public String[] getEntityTexts() {
		String[] texts = new String[entitiesInMap.size()];
		for (int i = 0; i < entitiesInMap.size(); i++) {
			texts[i] = entitiesInMap.get(i).getText();
		}
		return texts;
	}
	
	public void parseMap(int scale, String mapId) {
		String pathToCurrentMap = pathToMaps + mapId + "/";
		if (!ignoreTiles) {
			ArrayList<Entity> players = new ArrayList<Entity>();
			for (Entity e : entitiesInMap) {
				if (e instanceof Player) {
					players.add(e);
				} else if (e instanceof FollowingPlayer) {
					players.add(e);
				}
			}
			
			this.entitiesInMap.clear();
			entitiesInMap.addAll(players);
			this.mapId = mapId;
			readProperties();
			
			parseMapBase(new File(pathToCurrentMap + "base.map"));
			parseMapFG(new File(pathToCurrentMap + "fg.map"));
			parseMapBG(new File(pathToCurrentMap + "bg.map"));
//			state.setBGM(bgm,true);
//			state.playBGM();
		}
		
		
		parseEntities(new File(pathToCurrentMap + "entities.csv"),scale);
		parseDoors(new File(pathToCurrentMap + "doors.csv"),scale);
		parseHotspots(new File(pathToCurrentMap + "hotspots.csv"),scale);
		parseEnemySpawns(new File(pathToCurrentMap + "enemyspawns.data"),scale);
		parsePresents(new File(pathToCurrentMap + "presents.csv"),scale);
		
	}
	
	public void parsePresents(File ent,int scale) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(ent));
			String row = "";
			br.readLine();//skip headers
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				double x = Double.parseDouble(split[0]);
				double y = Double.parseDouble(split[1]);
				int itemId = Integer.parseInt(split[2]);
				String name = split[3];
				PresentEntity pe = new PresentEntity(x*scale,y*scale,itemId,name,state);
				if (!ignoreTiles) {
					pe.setSpriteCoords(state.getEntityFromEnum("present").getSpriteCoordinates());
				}
				//				pe = (PresentEntity) pe.createCopy(pe.getX(),pe.getY(),pe.getWidth(),pe.getHeight(),pe.getName());
//				Entity e = state.getEntityFromEnum(name).createCopy(scale*x,scale*y,scale*width,scale*height);
//				e.setText(text);
//				DoorEntity e = new DoorEntity(desc,x*scale,y*scale,width*scale,height*scale,state,destX*scale,destY*scale,destMap,text);
//				e.setAppearFlag(appFlag);
//				e.setDisappearFlag(disFlag);
				entitiesInMap.add(pe);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseEnemySpawns(File ent,int scale) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(ent));
			br.readLine(); //skip headers
			String row = "";
			while ((row = br.readLine()) != null) {
					String[] mapdata = row.split(",");
					int index = Integer.parseInt(mapdata[0]);
					double x = Double.parseDouble(mapdata[1]);
					double y = Double.parseDouble(mapdata[2]);
					int w = Integer.parseInt(mapdata[3]);
					int h = Integer.parseInt(mapdata[4]);
					EnemySpawnEntity ese = new EnemySpawnEntity(index,x*scale,y*scale,w*scale,h*scale,state,"EnemyGroup" + index);
					entitiesInMap.add(ese);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void parseEnemySpawns(File ent,int scale) {
//		BufferedReader br;
//		try {
//			br = new BufferedReader(new FileReader(ent));
//			String row = "";
//			while ((row = br.readLine()) != null) {
//				if (row.equals("#")) {
//					//read next four lines
//					row = br.readLine();
//					String name = row;
//					row = br.readLine();
//					String[] mapdata = row.split(",");
//					double x = Double.parseDouble(mapdata[0]);
//					double y = Double.parseDouble(mapdata[1]);
//					int w = Integer.parseInt(mapdata[2]);
//					int h = Integer.parseInt(mapdata[3]);
//					row = br.readLine();
//					String[] names = row.split(",");
//					row = br.readLine();
//					String[] percents = row.split(",");
//					float[] percentsFloats = new float[percents.length];
//					for (int i = 0; i < percents.length; i++) {
//						percentsFloats[i] = Float.parseFloat(percents[i]);
//					}
//					EnemySpawnEntity ese = new EnemySpawnEntity(x*scale,y*scale,w*scale,h*scale,state,name);
//					ese.setRates(percentsFloats);
//					ese.setEnemies(names);
//					entitiesInMap.add(ese);
//				}
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void parseDoors(File ent,int scale) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(ent));
			String row = "";
			br.readLine();//skip headers
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				String desc = split[0];
				double x = Double.parseDouble(split[1]);
				double y = Double.parseDouble(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);
				String destMap = split[5];
				int destX = Integer.parseInt(split[6]);
				int destY = Integer.parseInt(split[7]);
				String text = split[8];
				String appFlag = split[9];
				String disFlag = split[10];
				
//				Entity e = state.getEntityFromEnum(name).createCopy(scale*x,scale*y,scale*width,scale*height);
//				e.setText(text);
				DoorEntity e = new DoorEntity(desc,x*scale,y*scale,width*scale,height*scale,state,destX*scale,destY*scale,destMap,text);
				e.setAppearFlag(appFlag);
				e.setDisappearFlag(disFlag);
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
				String texture = split[0];
				double x = Double.parseDouble(split[1]);
				double y = Double.parseDouble(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);
				String text = split[5];
				String appFlag = split[7];
				String disFlag = split[8];
				Entity e = state.getEntityFromEnum(texture).createCopy(scale*x,scale*y,scale*width,scale*height,name);
				e.setText(text);
				e.setAppearFlag(appFlag);
				e.setDisappearFlag(disFlag);
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
	
	private void parseHotspots(File file, int scale) {
		// TODO Auto-generated method stub
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String row = "";
			br.readLine();//skip headers
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				String name = split[0];
				double x = Double.parseDouble(split[1]);
				double y = Double.parseDouble(split[2]);
				int w = Integer.parseInt(split[3]);
				int h = Integer.parseInt(split[4]);
				String csName = split[5];
				String appFlag = split[6];
				String disFlag = split[7];
				String pathToCurrentMap = pathToMaps + mapId + "/";
//				Entity e = state.getEntityFromEnum(name).createCopy(scale*x,scale*y,scale*width,scale*height);
//				e.setText(text);
				HotSpot e = new HotSpot(name,x*scale,y*scale,w*scale,h*scale,state,0,0,"","", pathToCurrentMap + csName);
				e.setAppearFlag(appFlag);
				e.setDisappearFlag(disFlag);
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

	public void parseMapBase(File map)  {
		
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
				tileMapBase = rows;
				this.width = tileMapBase.size();
				this.height = tileMapBase.get(0).size();
			}catch (IOException e) {
				
			}
			ArrayList<ArrayList<TileInstance>> rowstiles = new ArrayList<ArrayList<TileInstance>>();
			ArrayList<TileInstance> rowtiles = new ArrayList<TileInstance>();
			for (int j = 1; j < tileMapBase.size()-1; j++) {
				//for every row
				for (int i = 1; i < tileMapBase.get(0).size()-1; i++) {
					//for every column
					int val = this.tileMapBase.get(j).get(i);
					Tile tile = tilesGlobalMap.getTile(val);
					layerMap = tileMapBase;
					int instance = inspectSurroundings(i,j);
					rowtiles.add(tile.getInstance(instance));
				}
				rowstiles.add(rowtiles);
				rowtiles = new ArrayList<TileInstance>();
			}
			tileInstanceMapBase = rowstiles;
				
			} catch(FileNotFoundException e) {
				
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
		ArrayList<ArrayList<Integer>> newmapBase = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> newmapBG = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> newmapFG = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < tileMapBG.size(); i++) {
			ArrayList<Integer> rowBG = tileMapBG.get(i);
			ArrayList<Integer> rowFG = tileMapFG.get(i);
			ArrayList<Integer> rowBase = tileMapBase.get(i);
			for (int c = 0; c < expand; c++) {
				rowBase.add(0);
				rowBG.add(0);
				rowFG.add(0);
			}
			newmapBase.add(rowBase);
			newmapBG.add(rowBG);
			newmapFG.add(rowFG);
		}
		for (int i = 0; i < expand; i++) {
			ArrayList<Integer> emptyRowBase = new ArrayList<Integer>();
			ArrayList<Integer> emptyRowBG = new ArrayList<Integer>();
			ArrayList<Integer> emptyRowFG = new ArrayList<Integer>();
			for (int c = 0; c < newmapBG.get(0).size(); c++) {
				emptyRowBase.add(0);
				emptyRowBG.add(0);
				emptyRowFG.add(0);
			}
			newmapBase.add(emptyRowBase);
			newmapBG.add(emptyRowBG);
			newmapFG.add(emptyRowFG);
		}
		this.tileMapBase = newmapBase;
		this.tileMapBG = newmapBG;
		this.tileMapFG = newmapFG;
		setChangeMap(currentMapLayer);
		this.width = newmapBG.size();
		this.height = newmapBG.get(0).size();
//		SystemState.out.println("New Size = " + newmapBG.size() + "width, by " + newmapBG.get(0).size() + "height");
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
//			SystemState.err.println("Index out of bounds, cant add tile " + tileId +
//					" to (" + x + ", " + y + ")");
		}
//		this.tileMap[y][x] = tileId;
	}
	
	public Map(String mId, int width, int height, TileHashMap tm, SystemState m) {
		this(mId,width,height,tm,m,false);
	}
	
	public Map(String mId, int width, int height, TileHashMap tm, SystemState m,boolean ignoreTiles) {
		pathToTilesets = m.getPathToTilesets();
		this.ignoreTiles = ignoreTiles;
		this.tileset = pathToTilesets + "magicant.png";
//		ignoreTiles = true;
		if (!ignoreTiles) {
			m.loadAllTiles(tileset);
		}
		
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
			tileMapBase.add(list);
		}
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
	
	public void addToEntities(Entity newEntity) {
		// TODO Auto-generated method stub
		entitiesInMap.add(newEntity);
	}

	public ArrayList<Entity> getEntitiesInView(int xLeft, int yTop) {
		ArrayList<Entity> list = new ArrayList<Entity>();
		for (Entity e : entitiesInMap) {
			if (e.getX() < xLeft + state.getMainWindow().getScreenWidth() + 512 && e.getX() > xLeft - 512
					&& e.getY() > yTop - 512  && e.getY() < yTop + state.getMainWindow().getScreenHeight() + 5) {
				list.add(e);
			}
		}
		return list;
		// TODO Auto-generated method stub
		
	}

	public void fillInGrass() {
		// TODO Auto-generated method stub
		int[] grassTiles = {0,22,23,28,29};
		for (int i = 0; i < tileMapBG.size(); i++) {
			ArrayList<Integer> row = tileMapBG.get(i);
			for (int j = 0; j < row.size(); j++) {
				if (Math.random() < 0.5) {
					continue;
				}
				if (row.get(j) == 0) {
					int gid = (int) (5*Math.random());
					row.set(j,grassTiles[gid]);
				}
			}
		}
//		for (int i = 0; i < tileMapBase.size(); i++) {
//			ArrayList<Integer> row = tileMapBase.get(i);
//			for (int j = 0; j < row.size(); j++) {
//				if (row.get(j) > 158 || row.get(j) == 2 || row.get(j) == 8) {
//					tileMapBG.get(i).set(j,row.get(j));
//					tileMapBase.get(i).set(j,0);
//				}
//			}
//		}
	}

	public void setIgnoreTiles(boolean b) {
		// TODO Auto-generated method stub
		ignoreTiles = b;
	}

	public void setBGM(String path) {
		// TODO Auto-generated method stub
		this.actualBGM = bgm;
		this.bgm = path;
	}
	
	public void restoreBGM() {
		this.bgm = actualBGM;
	}
}
