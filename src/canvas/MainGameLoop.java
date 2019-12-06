package canvas;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import canvas.renderer.Loader;
import canvas.renderer.Renderer;
import canvas.renderer.entities.Entity;
import canvas.renderer.models.RawModel;
import canvas.renderer.models.TexturedModel;
import canvas.renderer.shaders.TileShader;
import canvas.renderer.textures.ModelTexture;
 
public class MainGameLoop {
 
    public static void main(String[] args) throws LWJGLException {
 
        Display.create();
        Loader loader = new Loader();
        
        TileShader shader = new TileShader();
        
        Renderer renderer = new Renderer(shader);
//        TileShader shader = new TileShader();
         
        float[] vertices = {            
                -1,1,0,
                1,1,0,
                1,-1,0,
                -1,-1,0
//                -0.73333335f, 1.0f, 0f,
//                -0.6f, 1.0f, 0f,
//                -0.6f, 0.88148147f, 0f,
//                -0.73333335f, 0.88148147f, 0f
        };
         
        int[] indices = {
                0,1,3,  //Top left triangle (V0,V1,V3)
                3,1,2   //Bottom right triangle (V3,V1,V2)
        };
         
        float[] tex = {
        	0,0,
        	0,1,
        	1,1,
        	1,0
        };
        
        RawModel model = loader.loadToVAO(vertices,indices,tex); 
        ModelTexture mt = new ModelTexture(loader.loadTexture("button.png"));
        
        TexturedModel tm = new TexturedModel(model,mt);
        
        Entity entity = new Entity(tm,new Vector3f(-6.0f,2.90f,-10f),0,0,0,1f);
        
        while(!Display.isCloseRequested()){
            //game logic
            renderer.prepare();
            shader.start();
//            renderer.render(entity,shader);
            shader.stop();
            Display.update();         
        }
 
        shader.cleanUp();
        loader.cleanUp();
        if (Display.isCloseRequested()) {
			Display.destroy();
//			if (!textEditor) {
				System.exit(0);
//			}
			
		}
 
    }
 
}