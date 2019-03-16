package mapeditor;

public class Tile {
	public int id;
	public String imageName;
	
	public Tile(int id, String imageName) {
		this.id = id;
		this.imageName = imageName;
	}
	
	public String getImageName() {
		return this.imageName;
	}
	
	public int getId() {
		return this.id;
	}
}
