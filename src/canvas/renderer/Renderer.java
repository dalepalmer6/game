package canvas.renderer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import canvas.renderer.entities.Entity;
import canvas.renderer.models.RawModel;
import canvas.renderer.models.TexturedModel;
import canvas.renderer.shaders.ShaderProgram;
import canvas.renderer.shaders.TileShader;
import canvas.renderer.textures.ModelTexture;
import tools.Maths;

public class Renderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = -1;
	private static final float FAR_PLANE = 1;
	
	private Matrix4f projectionMatrix;

	private TileShader shader;
	
	public Renderer(TileShader shader) {
		this.shader = shader;
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	 public void prepare(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
//	        GL11.glClearColor(0, 0, 0, 1);
	    }
	
	 public void render(Map<TexturedModel, List<Entity>> entities) {
		 for (TexturedModel tm :entities.keySet()) {
			 List<Entity> batch = entities.get(tm);
			 bindTexture(tm.getModelTexture().getTextureId());
			 bindTexturedModel(tm);
			 for (Entity entity : batch) {
//			 Entity entity = batch.get(0);
				shader.bindTexCoords(entity.getTexCoords()); //pass in coords to the shader
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			 }
			 unbindTexturedModel();
			 unbindTexture();
		 }
	 }
	 
	 public void bindTexture(int texId) {
		 GL13.glActiveTexture(GL13.GL_TEXTURE0);
		 GL11.glBindTexture(GL11.GL_TEXTURE_2D,texId);
	 }
	 
	 public void unbindTexture() {
		 GL13.glActiveTexture(GL13.GL_TEXTURE0);
		 GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
	 }
	 
	 
	public void bindTexturedModel(TexturedModel texmodel) {
		RawModel model = texmodel.getModel();
		GL30.glBindVertexArray(model.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	 
	 private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	 }
	 
	 private void prepareInstance(Entity entity) {
		 Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
		           entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
		    shader.loadTransformationMatrix(transformationMatrix);
	 }
	 
	 public Matrix4f getProjMatrix() {
		 return projectionMatrix;
	 }
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}
	
}
