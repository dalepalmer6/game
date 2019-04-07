package menu;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import canvas.MainWindow;
import gamestate.TileMetadata;

public class AnimationItem extends MenuItem {
	private TileMetadata tm;
	private String texture;
	public AnimationItem(String texture, StartupNew m, int x, int y, int width, int height) {
		super("",x,y,width,height,m);
		this.texture = texture;
	}

	public void draw(MainWindow m) {
		m.setTexture(((Animation)state.getMenuStack().peek()).getTexture());
//		Texture textureSlick;
//		try {
//			textureSlick = BufferedImageUtil.getTexture("", state.getAnimation(state.getPathToAnims() + texture));
//			textureSlick.bind();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		m.renderTile(x,y,width,height,tm.getX(),tm.getY(),tm.getWidth(),tm.getHeight());
		state.getTextureAtlas().getTexture().bind();
	}

	public void setTileMetadataToDraw(TileMetadata tm) {
		// TODO Auto-generated method stub
		this.tm = tm;
	}
	
}
