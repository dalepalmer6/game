package canvas.renderer.models;

import canvas.renderer.textures.ModelTexture;

public class TexturedModel {

	private RawModel model;
	private ModelTexture modelTexture;
	
	public TexturedModel(RawModel m, ModelTexture mt) {
		this.model = m;
		this.modelTexture = mt;
	}
	
	public RawModel getModel() {
		return model;
	}

	public ModelTexture getModelTexture() {
		return modelTexture;
	}

	public void setModelTexture(ModelTexture modelTexture) {
		this.modelTexture = modelTexture;
	}
	
	
}
