package canvas.renderer.models;

public class RawModel {

	private int vaoId;
	private int vertexCount;
	
	private float[] positions;
	private float[] texCoords;
	private int[] indices;
	
	public float[] getPositions() {
		return positions;
	}
	
	public float[] getTexCoords() {
		return texCoords;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public RawModel(int vaoId, int vertexCount) {
		this.vaoId = vaoId;
		this.vertexCount = vertexCount;
	}
	
	public RawModel(float[] positions, int[] indices, float[] texCoords) {
		this.positions = positions;
		this.indices = indices;
		this.texCoords = texCoords;
	}
	
	public int getVaoId() {
		return vaoId;
	}

	public void setVaoId(int vaoId) {
		this.vaoId = vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	
}
