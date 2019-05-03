package gamestate;

public class LevelupData {
	private static int[] expTable = new int[]
			{
					8,
					16,
					24,
					32,
					40,
					48,
					56,
			};
	public static int getExpToLevel(int level) {
		return expTable[level];
	}
}
