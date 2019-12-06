package system.data;

import java.util.HashMap;

/*
 * This needs to be in an external file.
 * */
public class LevelupData {
	private static int[] expTable = new int[99];
	 //Level to PSI Index
	static HashMap<Integer, Integer> nintenPSIHashMap = new HashMap<Integer, Integer>(){{
		put(3, 0);
		put(5, 22);
		put(11, 20);
		put(13, 29);
		put(16, 23);
		put(19, 24);
		put(22, 25);
		put(29,1);
		put(30,30);
		put(33,2);
		put(35,21);
		put(38,4);
		}};
	static HashMap<Integer, Integer> anaPSIHashMap = new HashMap<Integer, Integer>(){{
			put (2,5);
			put (2,15);
			put (4,29);
			put (5,34);
			put (6,22);
			put (7,39);
			put (8,6);
			put(9,16);
			put (11,35);
			put (9,30);
			put (12,2);
			put(13,23);
			put(14,24);
			put(15,25);
			put(17,10);//PKFire a
			put(18,21);
//			put(19,);//shield off
			put(21,7);
			put (22,2);
			put (25,36);
			put(27,11);
			put(28,17);
			put(29,8);
			put(31,12);
			put(33,37);
			put (34,3);
			put(35,13);
		}};
	
//	public static int getExpToLevel(int level) {
//		return expTable[level-1];
//	}
	
	public static int getExpToLevel(int targetLevel) {
		return (int) (targetLevel*targetLevel*(targetLevel+1)*0.5);
	}
	
	public static long getPSIShouldBeKnown(String pm, int level, long knownPSI) {
		long newPSI = 0;
		switch(pm) {
			case "ninten" :
				for (Integer key : nintenPSIHashMap.keySet()) {
					if (key <= level) {
						//try to AND the PSI with your string
						newPSI |= 1 << nintenPSIHashMap.get(key);
					}
				}
				break;
			case "ana" : 
				for (Integer key : anaPSIHashMap.keySet()) {
					if (key <= level) {
						//try to AND the PSI with your string
						newPSI |= 1 << anaPSIHashMap.get(key);
					}
				}
				break;
		}
		
		return newPSI;
	}
	
}
