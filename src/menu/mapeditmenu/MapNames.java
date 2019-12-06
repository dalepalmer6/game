package menu.mapeditmenu;

import java.util.HashMap;

/*
 * Should be stored in an external file.
 * */
public class MapNames {
	static HashMap<String,String> mapConversionChart = new HashMap<String,String>() {
		{
			put("podunk","Mother's Day");
			put("canary","Canary Village");
			put("desert","Advent Desert");
			put("factory","Duncan's Factory");
			put("graveyard","Graveyard");
			put("house - myhome","Your House");
			put("magicant","Magicant");
			put("magicave","Magicant");
			put("merrydeer tunnel","Merrysville / Reindeer");
			put("merrysville","Thanksgiving");
			put("merrysville buildings","Thanksgiving");
			put("merrysville north","Thanksgiving");
			put("podunk drugstore","Mother's Day");
			put("podunk house 1","Mother's Day");
			put("podunk house 2","Mother's Day");
			put("podunk house 3","Mother's Day");
			put("reindeer","Reindeer");
			put("roof","Thanksgiving");
			put("zoo","Choucream Zoo");
			put("youngtown","Easter");
			put("spookane","Halloween");
			put("snowman","Snowman");
		}
	};
	
	public static String getName(String id) {
		return mapConversionChart.get(id);
	}
}
