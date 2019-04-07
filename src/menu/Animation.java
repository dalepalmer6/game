package menu;

import java.io.File;
import java.io.IOException;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import canvas.MainWindow;
import gamestate.AnimationCoordinates;
import gamestate.Pose;
import gamestate.TileMetadata;

public class Animation extends Menu{
	private String texture;
	private Texture textureSlick;
	private AnimationCoordinates coordinates;
	private double tickCount = 0;
	private double ticksPerFrame = 0.2;
	private AnimationItem animItem;
	
	public String getTexture() {
		return state.getPathToAnims() + texture;
	}
	
	public Animation(StartupNew m, String texture) {
		super(m);
		this.texture = texture;
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
			org.w3c.dom.Document doc = documentBuilder.parse(new File(removePNGExtension(getTexture()) + ".sprites"));
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
		state.getMenuStack().push(this);
		state.setCurrentAnimation(texture);
		try {
			state.createAtlas();//rather than this, simply use opengl to bind the texture
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		coordinates.setPose(0);
		parseXMLFile();
		animItem = new AnimationItem(texture,state,0,0,state.getMainWindow().getScreenWidth(),state.getMainWindow().getScreenHeight());
		addMenuItem(animItem);
	}
	
	public void update() {
		System.out.println(tickCount);
		tickCount += ticksPerFrame;
		int i = (int) tickCount % coordinates.getPose(0).getNumStates();
		
		if (tickCount >= coordinates.getPose(0).getNumStates()-1) {
			state.needToPop = true;
		}
		
		TileMetadata tm = coordinates.getPose(0).getStateByNum(i);
		animItem.setTileMetadataToDraw(tm);
	}
	
	public void draw(MainWindow m) {
		
	}

}
