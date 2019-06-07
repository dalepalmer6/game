package menu;

import canvas.MainWindow;

public class MenuRunner {

	public static void main(String[] args) {
		StartupNew state = new StartupNew();
		AssetLoader al = new AssetLoader(state);
		new Thread(al).start();
		state.run();
	}
}
