package font;

import java.util.HashMap;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import global.InputController;

public class Text implements Drawable{
	private String textString;
	private String stringDuringLastCall = "";
	private String parsedString;
	protected int drawUntil = 1;
	protected boolean drawAll;
	private String name;
	private int x;
	private int y;
	private int width;
	private int height;
	private HashMap<Integer, String> controlCodes = new HashMap<Integer,String>();
	private CharList charList;
	private boolean textChanged;
	
	public void setTextChanged(boolean f) {
		this.textChanged = f;
	}
	
	public void incrementDrawUntil() {
		if (parsedString != null && !drawAll) {
			if (drawUntil < parsedString.length()) {
				drawUntil++;
			}
		}
	}
	public Text(String s, int x, int y,int width, int height, CharList cd) {
		this(false,s,x,y,width,height,cd);
	}
	
	public Text(boolean shouldDrawAll, String s, int x, int y,int width, int height, CharList cd) {
		this.textString = s;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.charList = cd;
		this.drawAll = shouldDrawAll;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setText(String s) {
		this.textString = s;
		setTextChanged(true);
	}
	
	public String getText() {
		return this.textString;
	}
	
	public boolean parse() {
		int currentWidth = 0;
		int currentHeight = 0;
		int indexOfLastSpace = 0;
		int indexOfNewSpace = 0;
		int wordWidth = 0;
		char[] stringArray = parsedString.toCharArray();
		for (int i = 0; i < stringArray.length; i++) {
			if (controlCodes.containsKey(i)) {
				continue;
			}
			String controlCode = "";
			if (stringArray[i] == '[') {
				wordWidth = 0;
				currentWidth = 0;
				for (int j = i+1; j < stringArray.length; j++) {
					if (stringArray[j] != ']') {
						controlCode += stringArray[j];
					} else {
						parsedString = parsedString.replaceFirst("\\[" + controlCode + "\\]", "");
						controlCodes.put(i, controlCode);
						if (parse()) {
							return true;
						};
						stringArray = parsedString.toCharArray();
						break;
					}
				}
			} 
			else if (stringArray[i] == '@') {
				controlCode = "INDENT";
			} else if (stringArray[i] == ' ') {
				indexOfLastSpace = indexOfNewSpace;
				indexOfNewSpace = i;
				currentWidth += wordWidth;
				wordWidth = 0;
			}
			else if (currentWidth + wordWidth > width) {
				if (indexOfNewSpace != 0) {
					controlCodes.put(indexOfNewSpace,"NEWLINE");
				}
				
				currentWidth = 0;
				wordWidth = 0;
				indexOfLastSpace = indexOfNewSpace;
			} else {
				wordWidth += charList.getCharObjects().get(parsedString.toCharArray()[i]).getDw() + 2;
			}
		}
		return true;
	}
	
	@Override
	public void draw(MainWindow m) {
		initDrawText(m);
		// TODO Auto-generated method stub
		if (parsedString == null || textChanged) {
			this.parsedString = textString;
			controlCodes.clear();
			parse();
			textChanged = false;
			
			if (drawAll) {
				drawUntil = parsedString.length();
			}
			
			for (int key : controlCodes.keySet()) {
				if (key >= parsedString.length()) {
					controlCodes.remove(key);
					break;
				}
			}
		}
		int curX = x;
		int curY = y;
		int scale = 2;
		char[] chars = parsedString.toCharArray();
//		for (int i = 0; i < parsedString.length(); i++) {
		for (int i = 0; i < drawUntil; i++) {
			char c = chars[i];
			if (i == 0 && c != '@') {
				curX += charList.getCharObjects().get(c).getDw()*scale;
			}
			if (controlCodes.containsKey(i)) {
				String control = controlCodes.get(i);
				if (control.equalsIgnoreCase("NEWLINE")) {
					curX = x + (int) charList.getCharObjects().get('@').getDw() + 2;
					curY +=32;
				}
			}
			m.renderTile(curX,curY,
					(int) charList.getCharObjects().get(c).getDw()*scale,
					(int) charList.getCharObjects().get(c).getDh()*scale,
					charList.getCharObjects().get(c).getDx(),
					charList.getCharObjects().get(c).getDy(),
					charList.getCharObjects().get(c).getDw(),
					charList.getCharObjects().get(c).getDh());
			curX += 1*scale +  charList.getCharObjects().get(c).getDw()*scale;
		}
	}
	
	public static void initDrawText(MainWindow m) {
		m.setTexture("img/font.png");
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	
	
	
}
