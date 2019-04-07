package global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shaders {
	
	public static int createShader(String filename, int shaderType) throws Exception {
		int shader = 0;
		try {
		shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
		
		if(shader == 0)
		return 0;
		
		ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
		ARBShaderObjects.glCompileShaderARB(shader);
		
		if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
		throw new RuntimeException("Error creating shader:");
		
		return shader;
		}
		catch(Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
}
	
	public static String readFileAsString(String filename) {
		String shaderSource = ""; 
		int shaderID = 0;
         
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(filename));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                shaderSource += line + "\n";
	            }
	            reader.close();
	        } catch (IOException e) {
	            System.err.println("Could not read file.");
	            e.printStackTrace();
	            System.exit(-1);
	        }
	    return shaderSource;
	}

	 public static int loadShader(String filename, int type) {
	        StringBuilder shaderSource = new StringBuilder();
	        int shaderID = 0;
	         
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(filename));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                shaderSource.append(line).append("\n");
	            }
	            reader.close();
	        } catch (IOException e) {
	            System.err.println("Could not read file.");
	            e.printStackTrace();
	            System.exit(-1);
	        }
	         
	        shaderID = GL20.glCreateShader(type);
	        GL20.glShaderSource(shaderID, shaderSource);
	        GL20.glCompileShader(shaderID);
	         
	        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
	            System.err.println("Could not compile shader.");
	            System.exit(-1);
	        }
	         
//	        this.exitOnGLError("loadShader");
	         
	        return shaderID;
	 }
	
}
