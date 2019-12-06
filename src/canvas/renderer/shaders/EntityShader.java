package canvas.renderer.shaders;

public class EntityShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "entity_vertex.glsl";
	private static final String FRAGMENT_SHADER = "entity_fragment.glsl";
	
	public EntityShader() {
		super(VERTEX_SHADER,FRAGMENT_SHADER);
	}
	
	@Override
	protected void bindAttributes() {
		// TODO Auto-generated method stub

	}

}
