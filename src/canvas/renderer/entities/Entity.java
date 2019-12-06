package canvas.renderer.entities;

import org.lwjgl.util.vector.Vector3f;

import canvas.renderer.models.TexturedModel;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rx, ry, rz;
	private float scale;
	private float[] positions;
	private int[] indices;
	private float[] texCoords;
	
	public Entity(TexturedModel m, Vector3f pos, float rx, float ry, float rz, float scale) {
		this.model = m;
		this.position = pos;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rx += dx;
		this.ry += dy;
		this.rz += dz;
	}
	
	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRx() {
		return rx;
	}

	public void setRx(float rx) {
		this.rx = rx;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float[] getPositions() {
		return positions;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public float[] getTexCoords() {
		return texCoords;
	}

	public void setBufferData(float[] positions, int[] indices, float[] texCoords) {
		this.positions = positions;
		this.indices = indices;
		this.texCoords = texCoords;
	}

	public void setTexCoords(float[] texCoords2) {
		// TODO Auto-generated method stub
		this.texCoords = texCoords2;
	}
	
	public void setPositions(float[] pos) {
		this.positions = pos;
	}
	
	public void setIndices(int[] ind) {
		this.indices = ind;
	}
	
}
