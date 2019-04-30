package menu;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import battlesystem.BattleMenu;
import canvas.MainWindow;
import gamestate.AnimationCoordinates;
import gamestate.Pose;
import gamestate.TileMetadata;

public class Animation extends MenuItem {
	private String texture;
	private Texture textureSlick;
	protected AnimationCoordinates coordinates;
	protected double tickCount = 0;
	protected double ticksPerFrame = 0.34;
	protected TileMetadata tm;
	
	public String getTexture() {
		return texture;
	}
	
	public Animation(StartupNew m) {
		super("",0,0,m.getMainWindow().getScreenWidth(),m.getMainWindow().getScreenHeight(),m);
		state = m;
	}
	
	public Animation(StartupNew m, String texture, int x, int y, int w, int h) {
		super("",x,y,w,h,m);
		this.texture = texture;
		try {
			textureSlick = BufferedImageUtil.getTexture("", ImageIO.read(new File(state.getPathToAnims() + this.texture + ".png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		coordinates = new AnimationCoordinates(texture);
	}
	
	public String removePNGExtension(String fname) {
		return fname.substring(0,fname.length()-4);
	}
	
	public void parseXMLFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = documentBuilder.parse(new File(state.getPathToAnims() + texture + ".sprites"));
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("spr");
			for(int i = 0 ; i < nList.getLength(); i++){
			    Element subTextureElement = (Element)nList.item(i);
			    String name = subTextureElement.getAttribute("name");
			    String  x= subTextureElement.getAttribute("x");
			    String y= subTextureElement.getAttribute("y");
			    String width = subTextureElement.getAttribute("w");
			    String height= subTextureElement.getAttribute("h");
			    coordinates.addStateToPose(0,Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(width),Integer.parseInt(height));
			    // DO SOMETHING WITH THIS DATA HERE
			    // GET THE PICTURE FROM SHEET WITH THESE.
			}
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void createAnimation() {
//		state.setCurrentAnimation(texture);
//		try {
//			state.createAtlas();//rather than this, simply use opengl to bind the texture
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		coordinates.setPose(0);
		parseXMLFile();
//		tm = coordinates.getPose(0).getStateByNum(0);
//		animItem = new AnimationItem(texture,state,0,0,state.getMainWindow().getScreenWidth(),state.getMainWindow().getScreenHeight());
	}
	
	public TileMetadata getStateByNum(int i) {
		return coordinates.getPose(0).getStateByNum(i);
	} 
	
	public void updateAnim() {
		System.out.println(tickCount);
		tickCount += ticksPerFrame;
		int i = (int) tickCount % coordinates.getPose(0).getNumStates();
		
		if (tickCount >= coordinates.getPose(0).getNumStates()-1) {
			state.getMenuStack().peek().setToRemove(this);
			((BattleMenu)state.getMenuStack().peek()).setGetResultText();
			((BattleMenu)state.getMenuStack().peek()).getCurrentActiveBattleAction().setComplete();
			tickCount = 0;
//			((BattleMenu)state.getMenuStack().peek()).getCurrentAction().setComplete();
		}
		
		TileMetadata tm = coordinates.getPose(0).getStateByNum(i);
		this.tm = tm;
	}
	
	
	public void draw(MainWindow m) {
		m.setTexture(state.getPathToAnims() + texture + ".png");
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureSlick.getTextureID());
		m.renderAnimation(textureSlick,x,y,width,height,tm.getX(),tm.getY(),tm.getWidth(),tm.getHeight(),false);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.getTextureAtlas().getTexture().getTextureID());
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	public void bindAnimToTwo() {
		// TODO Auto-generated method stub
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureSlick.getTextureID());
	}


}
