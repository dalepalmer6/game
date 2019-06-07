package menu;

public class AssetLoader implements Runnable {
	private StartupNew state;
	
	public AssetLoader(StartupNew state) {
		this.state = state;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Thread is Starting its Job");
		state.loadAllAnims();
		state.loadAllPSI();
		System.out.println("Thread is Done");
	}

}
