package system;

/*
 * Launcher class
 * */
public class MenuRunner {

	public static void main(String[] args) {
		SystemState state = new SystemState();
		AssetLoader al = new AssetLoader(state);
		new Thread(al).start();
		state.run();
	}
}
