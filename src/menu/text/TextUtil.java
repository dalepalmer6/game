package menu.text;

/*
 * Set of static methods that will allow certain modifications of text
 * */
public class TextUtil {
	
	/*
	 * If s ends in an s, it uses ', otherwise 's
	 * */
	public static String getPluralForm(String s) {
		String plural;
		if (s.substring(s.length()-1,s.length()).equals("s")) {
			plural = "'";
		} else {
			plural = "'s";
		}
		return plural;
	}
	
	/*
	 * If the noun starts with a vowel and the predicate is "a" return "an"
	 * */
	public static String getModifiedPredicate(String predicate, String noun) {
		String newPred = null;
		
		return newPred;
	}

}
