package canvas.renderer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import canvas.renderer.models.RawModel;

public class Loader {

	List<Integer> vaos = new ArrayList<Integer>();
	List<Integer> vbos = new ArrayList<Integer>();
	List<Integer> textures = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, int[] indices, float[] texCoords) {
		int vaoId = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,texCoords);
		unbindVAO();
		return new RawModel(vaoId, indices.length);
	}
	
	public int loadTexture(String filePath) {
		Texture t = null;
		try {
			t = TextureLoader.getTexture("PNG", new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int id = t.getTextureID();
		textures.add(id);
		return id;
	}
	
	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int tex : textures) {
			GL11.glDeleteTextures(tex);
		}
	}
	
	public int createVAO() {
		int id = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(id);
		vaos.add(id);
		return id;
	}
	
	public void bindIndicesBuffer(int[] indices) {
		int vboId = GL15.glGenBuffers();
		vbos.add(vboId);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
	}
	
	public void storeDataInAttributeList(int attribNo, int size, float[] data) {
		int vboId = GL15.glGenBuffers();
		vbos.add(vboId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboId);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribNo,size,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
	}
	
	public void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	public FloatBuffer storeDataInFloatBuffer(float[] b) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(b.length);
		fb.put(b);
		fb.flip();
		return fb;
	}
	
	public IntBuffer storeDataInIntBuffer(int[] b) {
		IntBuffer ib = BufferUtils.createIntBuffer(b.length);
		ib.put(b);
		ib.flip();
		return ib;
	}
}
