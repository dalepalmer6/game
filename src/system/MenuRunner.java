package system;

/*
 * Launcher class
 * */
public class MenuRunner {

	public static void main(String[] args) {
		MotherSystemState state = new MotherSystemState();
		AssetLoader al = new AssetLoader(state);
		new Thread(al).start();
		state.run();
	}
}
