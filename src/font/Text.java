package font;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import global.InputController;

public class Text implements Drawable{
	private String textString;
	private String parsedString;
	protected int drawStart = 0;
	protected int drawUntil = 1;
	protected boolean drawAll;
	private int x;
	private int y;
	private int width;
	private int height;
	private HashMap<Integer, String> controlCodes = new HashMap<Integer,String>();
	private CharList charList;
	private boolean textChanged;
	private boolean freeze;
	private int saveStart;
	private boolean done;
	
	public void setTextChanged(boolean f) {
		this.textChanged = f;
	}
	
	public void incrementDrawUntil() {
		if (parsedString != null && !drawAll && !freeze) {
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
	
	public String getParsedText() {
		return this.parsedString;
	}
	
	public void parse() {
		parseCodes();
		reparseForWidths();
		reparseForHeights();
	}
	
	public void parseCodes() {
		char[] stringArray = parsedString.toCharArray();
		String controlCode = "";
		int indexOfCode = 0;
		boolean parsingControlCode = false;
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray[i] == '[') {
				//start a new control code
				controlCode = "";
				indexOfCode = i;
				parsingControlCode = true;
				continue;
			}
			
			if (stringArray[i] != ']' && parsingControlCode) {
				controlCode += stringArray[i];
			} 
			if (stringArray[i] == ']' && parsingControlCode) {
				String controlCodesList = controlCode;
				if (controlCodes.containsKey(indexOfCode)) {
					controlCodesList = controlCodes.get(indexOfCode) + "," + controlCode;
				}
				controlCodes.put(indexOfCode,controlCodesList);
				parsedString = parsedString.replaceFirst("\\[" + controlCode + "\\]", "");
				i -= (controlCode.length()+2);
				stringArray = parsedString.toCharArray();
				parsingControlCode = false;
				continue;
			}
			
		}
	}
	
	public void setFreeze(boolean c) {
		freeze = c;
		drawStart = saveStart;
	}
	
	public int getDrawStart() {
		return drawStart;
	}
	
	public void reparseForWidths() {
		//implement text that can scroll, and be prompted next
		int currentWidth = 0;
		int currentHeight = 0;
		int currentWordWidth = 0;
		int indexOfLastSpace = 0;
		boolean addToWidths = true;
		char[] stringArray = parsedString.toCharArray();
		for (int i = 0; i < stringArray.length; i++) {
			addToWidths = true;
			if (controlCodes.containsKey(i)){
				currentWordWidth = 0;
				currentWidth = 0;
				indexOfLastSpace = i;
				continue;
			}
			if (stringArray[i] == ' ') {
				currentWidth += currentWordWidth;
				currentWordWidth = 0;
				indexOfLastSpace = i;
			}  
			if (currentWidth + currentWordWidth > this.width) {
				String controlCode = "NEWLINE";
				if (controlCodes.containsKey(indexOfLastSpace)) {
					controlCode = "NEWLINE," + controlCodes.get(indexOfLastSpace);
				}
				controlCodes.put(indexOfLastSpace,controlCode);
				
				parsedString = parsedString.substring(0,indexOfLastSpace) + parsedString.substring(indexOfLastSpace+1); 
				stringArray = parsedString.toCharArray();
				//fix the control codes that are greater than i
				HashMap<Integer, String> newCtrlCodes = new HashMap<Integer,String>();
				for (int key : controlCodes.keySet()) {
					if (key > indexOfLastSpace) {
						String val = controlCodes.get(key);
						newCtrlCodes.put(key-1,val);
					}
					else if (key == indexOfLastSpace) {
						String val = controlCodes.get(key);
						newCtrlCodes.put(key,val);
					} else {
						String val = controlCodes.get(key);
						newCtrlCodes.put(key,val);
					}
				}
				controlCodes = newCtrlCodes;
				
				currentWidth = currentWordWidth;
				currentWordWidth = 0;
				addToWidths = false;
			}
			if (addToWidths) {
				currentWordWidth += charList.getCharObjects().get(parsedString.toCharArray()[i]).getDw() + 1;
			}
		}
	}
	
	public void reparseForHeights() {
		int curY = y;
		char[] stringArray = parsedString.toCharArray();
		//count the number of NEWLINES in the controlCodes,
		Object[] controlCodesKeys =  controlCodes.keySet().toArray();
		Arrays.sort(controlCodesKeys);
		for (Object key : controlCodesKeys) {
			String[] controlCodesAtKey = controlCodes.get(key).split(",");
			String controlCodesReplace = "";
			for (int i = 0; i < controlCodesAtKey.length; i++) {
				String cc = controlCodesAtKey[i];
				if (cc.equals("NEWLINE")) {
					curY+=32;
					if (curY + 32 > y + (this.height + 16) * 2) {
						cc = "PROMPTINPUT";
						curY = y;
					}
				}
				controlCodesReplace = controlCodesReplace + "," +cc;
			}
			controlCodes.put((Integer) key,controlCodesReplace);
		}
	}
	
	public boolean getDrawState() {
		return done;
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
		for (int i = drawStart; i < drawUntil; i++) {
			char c = chars[i];
			if (i == 0 && c != '@') {
//				curX += charList.getCharObjects().get(c).getDw()*scale;
			}
			if (controlCodes.containsKey(i)) {
				String[] controls = controlCodes.get(i).split(",");
				String newListOfControlCodes = "";
				for (String control : controls) {
					if (control.equalsIgnoreCase("NEWLINE")) {
//						curX = x + (int) charList.getCharObjects().get('@').getDw() + 2;
						curX = x;
						curY +=32;
						newListOfControlCodes += ",NEWLINE";
					}
					if (control.equalsIgnoreCase("PROMPTINPUT")) {
						saveStart = i;
						drawUntil = i;
						freeze = true;
						return;
					}
					controlCodes.put(i,newListOfControlCodes);
				}
			}
			m.renderTile(curX,curY,
					(int) charList.getCharObjects().get(c).getDw()*scale,
					(int) charList.getCharObjects().get(c).getDh()*scale,
					charList.getCharObjects().get(c).getDx(),
					charList.getCharObjects().get(c).getDy(),
					charList.getCharObjects().get(c).getDw(),
					charList.getCharObjects().get(c).getDh());
			curX += scale + charList.getCharObjects().get(c).getDw()*scale;
			if (i >= parsedString.length()-1) {
				done = true;
			}
		}
	}
	
	public static void initDrawText(MainWindow m) {
		m.setTexture("img/font.png");
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}

	
	
	
}
