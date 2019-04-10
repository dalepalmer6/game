package ShaderTest;
 
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;
 
/**
* The vertex and fragment shaders are setup when the box object is
* constructed. They are applied to the GL state prior to the box
* being drawn, and released from that state after drawing.
* @author Stephen Jones
*/
public class Box {
    private float startTime;
    private Texture texture;
    /*
    * if the shaders are setup ok we can use shaders, otherwise we just
    * use default settings
    */
    private boolean useShader;
     
    /*
    * program shader, to which is attached a vertex and fragment shaders.
    * They are set to 0 as a check because GL will assign unique int
    * values to each
    */
    private int program=0;
 
    public Box(){
    	BufferedImage img;
    	try {
    		img = ImageIO.read(new File("ness.png"));
			this.texture = BufferedImageUtil.getTexture("",img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	texture.bind();
    	GL13.glActiveTexture(texture.getTextureID());
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D,texture.getTextureID());
    	
    	startTime = System.nanoTime();
        int vertShader = 0, fragShader = 0;
         
        try {
            vertShader = createShader("src/shaders/vertex_textured.glsl",ARBVertexShader.GL_VERTEX_SHADER_ARB);
            fragShader = createShader("src/shaders/fragment_textured.glsl",ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        }
        catch(Exception exc) {
            exc.printStackTrace();
            return;
        }
        finally {
            if(vertShader == 0 || fragShader == 0)
                return;
        }
         
        program = ARBShaderObjects.glCreateProgramObjectARB();
         
        if(program == 0)
            return;
         
        /*
        * if the vertex and fragment shaders setup sucessfully,
        * attach them to the shader program, link the sahder program
        * (into the GL context I suppose), and validate
        */
        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);
         
        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(program));
            return;
        }
         
        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(program));
            return;
        }
         
        useShader = true;
    }
     
    /*
    * If the shader was setup succesfully, we use the shader. Otherwise
    * we run normal drawing code.
    */
    public void draw(){
        if(useShader)
            ARBShaderObjects.glUseProgramObjectARB(program);
        
        ARBShaderObjects.glUseProgramObjectARB(program); 
		 int p = GL20.glGetUniformLocation(program, "in_color");
	        GL20.glUniform4f(p, 0.0f,1.0f,0.0f,0.0f);
	        int p2 = GL20.glGetUniformLocation(program, "in_time");
	        GL20.glUniform1f(p2, System.nanoTime()-startTime);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -10.0f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);//white
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_QUADS);
        	GL11.glTexCoord2f(0,0);
	        GL11.glVertex2f(-1.0f, 1.0f);
	        GL11.glTexCoord2f(1,0);
	        GL11.glVertex2f(1.0f, 1.0f);
	        GL11.glTexCoord2f(1,1);
	        GL11.glVertex2f(1.0f, -1.0f);
	        GL11.glTexCoord2f(0,1);
	        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
         
        //release the shader
        if(useShader)
            ARBShaderObjects.glUseProgramObjectARB(0);
 
    }
     
    /*
    * With the exception of syntax, setting up vertex and fragment shaders
    * is the same.
    * @param the name and path to the vertex shader
    */
    private int createShader(String filename, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
             
            if(shader == 0)
                return 0;
             
            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);
             
            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
             
            return shader;
        }
        catch(Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
    }
     
    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
     
    private String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();
         
        FileInputStream in = new FileInputStream(filename);
         
        Exception exception = null;
         
        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
             
            Exception innerExc= null;
            try {
                String line;
                while((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            }
            catch(Exception exc) {
                exception = exc;
            }
            finally {
                try {
                    reader.close();
                }
                catch(Exception exc) {
                    if(innerExc == null)
                        innerExc = exc;
                    else
                        exc.printStackTrace();
                }
            }
             
            if(innerExc != null)
                throw innerExc;
        }
        catch(Exception exc) {
            exception = exc;
        }
        finally {
            try {
                in.close();
            }
            catch(Exception exc) {
                if(exception == null)
                    exception = exc;
                else
                    exc.printStackTrace();
            }
             
            if(exception != null)
                throw exception;
        }
         
        return source.toString();
    }
}