package canvas.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import canvas.renderer.entities.Entity;
import canvas.renderer.models.TexturedModel;
import canvas.renderer.shaders.TileShader;
import canvas.renderer.textures.ModelTexture;

public class MasterRenderer {

	private TileShader shader = new TileShader();
	private Renderer renderer = new Renderer(shader);
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public void render() {
		renderer.prepare();
		shader.start();
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel,newBatch);
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
}
