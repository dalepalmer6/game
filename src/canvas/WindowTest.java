package canvas;

public class WindowTest {
	public static void main(String[] args) {
		MainWindow m = new MainWindow();
		m.start();
		while (true) {
			m.render(10, 10, 200, 200);
		}
		
	}
}
