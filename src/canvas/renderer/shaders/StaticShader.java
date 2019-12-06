package canvas.renderer.shaders;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "src/shaders/entity_vertex.glsl";
	private static final String FRAGMENT_SHADER = "src/shaders/entity_fragment.glsl";
	
	public StaticShader() {
		super(VERTEX_SHADER,FRAGMENT_SHADER);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0,"position");
	}

	
	
}
