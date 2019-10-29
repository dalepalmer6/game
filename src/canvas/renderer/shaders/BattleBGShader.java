package canvas.renderer.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class BattleBGShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "battlebg_vertex.glsl";
	private static final String FRAGMENT_SHADER = "battlebg_fragment.glsl";
	
	private int loc_transMatrix;
	private int loc_projMatrix;
	private int loc_viewMatrix;
	
	//loc variables
	private int time;
	private int freqx;
	private int freqy;
	private int ampx;
	private int ampy;
	private int spdx;
	private int spdy;
	private int transx;
	private int transy;
	private int tex;
	private int texPal;
	private int frames;
	
	public BattleBGShader() {
		super(VERTEX_SHADER,FRAGMENT_SHADER);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(loc_transMatrix,matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f proj) {
		super.loadMatrix(loc_projMatrix,proj);
	}
	
	@Override
	protected void getAllUniformLocations() {
		time = getUniformLocation("time");
        freqx = getUniformLocation("freqx");
        freqy = getUniformLocation("freqy");
        ampx = getUniformLocation("amplitudex");
        ampy = getUniformLocation("amplitudey");
        spdx = getUniformLocation("speedx");
        spdy = getUniformLocation("speedy");
        transx = getUniformLocation("transx");
        transy = getUniformLocation("transy");
        tex = getUniformLocation("texture_diffuse");
        texPal = getUniformLocation("texture_color_palette");
        frames = getUniformLocation("frames");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(1,"in_Position");
		bindAttribute(0,"in_TextureCoord");
	}
	
	public void bindAll(float time, float fx, float fy, float ax, float ay, float sx, float sy, float tx, float ty,
			int tex, int pal) {
		loadFloat(this.time,time);
		loadFloat(freqx,fx);
		loadFloat(freqy,fy);
		loadFloat(ampx,ax);
		loadFloat(ampy,ay);
		loadFloat(spdx,sx);
		loadFloat(spdy,sy);
		loadFloat(transx,tx);
		loadFloat(transy,ty);
		loadInt(this.tex,tex);
		loadInt(this.texPal,pal);
	}

}
