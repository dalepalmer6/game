package tiles;

/*
 * When flagName is set in the flags, it will update to the new tile index.*/
public class ChangeWithFlagTile extends SingleInstanceTile {
	private String flagName;
	private int newTileId;
	
	public String getFlagName() {
		return flagName;
	}
	
	public int getNewTileId() {
		return newTileId;
	}
	
	public ChangeWithFlagTile(int id, String fname, int nid) {
		super(id);
		flagName = fname;
		newTileId = nid;
	}
}
