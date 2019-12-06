package canvas.renderer.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

public class TileShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "map_vertex.glsl";
	private static final String FRAGMENT_SHADER = "map_fragment.glsl";
	
	private int loc_transMatrix;
	private int loc_projMatrix;
	private int loc_viewMatrix;
	
	public TileShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	public void bindTexCoords(float[] texCoords) {
		int loc = getUniformLocation("texCoords1");
		loadVector2f(loc, new Vector2f(texCoords[0],texCoords[1]));
		loc = getUniformLocation("texCoords2");
		loadVector2f(loc, new Vector2f(texCoords[2],texCoords[3]));
		loc = getUniformLocation("texCoords3");
		loadVector2f(loc, new Vector2f(texCoords[4],texCoords[5]));
		loc = getUniformLocation("texCoords4");
		loadVector2f(loc, new Vector2f(texCoords[6],texCoords[7]));
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0,"pos");
		bindAttribute(1,"texCoords");
	}
	
	@Override
	protected void getAllUniformLocations() {
		loc_transMatrix = super.getUniformLocation("transMatrix");
		loc_projMatrix = super.getUniformLocation("projMatrix");
//		loc_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(loc_transMatrix,matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f proj) {
		super.loadMatrix(loc_projMatrix,proj);
	}
	
//	public void loadViewMatrix(Camera camera){
//        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
//        super.loadMatrix(location_viewMatrix, viewMatrix);
//    }
	
}
