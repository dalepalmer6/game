package gamestate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import menu.StartupNew;

public class CutsceneData {
	private StartupNew state;
	private Entity entity;
	private ArrayList<MovementData> movements;
	private HashMap<Integer,String> strings;
	private int indexOfMovements;
	private String entityName;
	
	public MovementData getNextMovementData() {
		if (indexOfMovements >= movements.size()) {
			return null;
		}
		return movements.get(indexOfMovements++);
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public CutsceneData(StartupNew state, String cutsceneFileName) {
		indexOfMovements = 0;
		this.state = state;
		movements = new ArrayList<MovementData>();
		strings = new HashMap<Integer,String>();
		BufferedReader br;
		String relEntities = "";
		String row = "";
		try {
			int index = 0;
			br = new BufferedReader(new FileReader(cutsceneFileName));
			relEntities = br.readLine();
			while ((row = br.readLine())!=null) {
				String[] controls = row.split(",");
				if (controls[0].equals("move")) {
					int x = Integer.parseInt(controls[1]);
					int y = Integer.parseInt(controls[2]);
					String actionTaken = "";
					String directionX = "";
					String directionY = "";
					if (x == 0 && y == 0) {
						actionTaken = "idle";
					} else {
						actionTaken = "walking";
					}
					if (x < 0) {
						directionX = "left";
					} else {
						directionX = "right";
					}
					if (y < 0) {
						directionY = "up";
					} else {
						directionY = "down";
					}
					movements.add(new MovementData(x,y,actionTaken,directionX,directionY));
					index++;
				} else if (controls[0].equals("text")) {
					strings.put(index,controls[1]);
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		entityName = relEntities;
	}
	
	public void loadEntity() {
		for (Entity e : state.getGameState().getEntityList()) {
			if (e.getName().equals(entityName)) {
				entity = e;
			}
		}
		int x = entity.getX();
		int y = entity.getY();
//		for (MovementData md : movements) {
//			md.setX(x += md.getX());
//			md.setY(y += md.getY());
//		}
	}
	
	public void moveTowardPlayer() {
		//get the vector between entity and the player object and approach it in steps
		Player p = state.getGameState().getPlayer();
		int distX = entity.getX() - p.getX();
		int distY = entity.getY() - p.getY();
		int numStepsX = distX/8;
		int numStepsY = distY/8;

	}

	public String getString() {
		// TODO Auto-generated method stub
		return strings.get(indexOfMovements);
	}
}