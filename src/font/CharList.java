package font;

import java.util.ArrayList;
import java.util.HashMap;

public class CharList {
	char[] chars = {'!','\"','`','$','%','`','\'','(',')','*','+',',','-','.','/','0','1','2','3','4','5','6','7','8','9',':',';','\"','=','\"','?','@','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W'
			,'X','Y','Z','`','`','`','`','`','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w'
			,'x','y','z'};
	
	int[] widths = {1,3,2,5,9,7,2,3,3,3,4,2,2,1,4, 4,2,4,4,5,4,4,4,4,4, 1,2,3,5,3,4,3,   6,5,5,5,4,4,5,5,1,4,5,4,7,5,5,5,5,5,5,5,5,6,7,5,5,4,0,0,0,0,0,5,4,4,4,4,3,4,4,1,2,4,1,7,4,4,4,4,3,4,3,4,5,7,4,4,4};
	int[] heights= {9,9,9,16,9,9,9,16,16,9,9,16,9,9,9,9,9,9,9,9,9,9,9,9,9,9,16,9,9,9,9,9,   9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,0,0,0,0,0,9,9,9,9,9,9,16,9,9,16,9,9,9,9,9,16,16,9,9,9,9,9,9,9,18,9};
	HashMap<Character, CharacterData> charObjects = new HashMap<Character,CharacterData>();
	HashMap<String, CharacterData> specialCharObjects = new HashMap<String,CharacterData>();
	
	public HashMap<Character, CharacterData> getCharObjects() {
		return charObjects;
	}
	
	public CharList() {
		int dx = 24;
		int dy = 28;
		int deltaX = 16;
		int deltaY = 24;
		
		specialCharObjects.put("alpha",new CharacterData(' ',360,52,5,9));
		specialCharObjects.put("beta",new CharacterData(' ',376,52,4,9));
		specialCharObjects.put("gamma",new CharacterData(' ',392,52,6,9));
		specialCharObjects.put("sigma",new CharacterData(' ',408,52,4,9));
		specialCharObjects.put("omega",new CharacterData(' ',424,52,5,9));
//		specialCharObjects.put("pi")
		
		for (int i = 0; i < chars.length; i++) {
			if (dx > 600) {
				dx = 24;
				dy += deltaY;
			}
			
			charObjects.put(chars[i],new CharacterData(chars[i], dx, dy, widths[i],heights[i]));
			dx += deltaX;
		}
		charObjects.put(' ', new CharacterData(' ', 528,76,2,9));
	}

	public CharacterData getSpecChar(String name) {
		// TODO Auto-generated method stub
		return specialCharObjects.get(name);
		
	}
	
}
