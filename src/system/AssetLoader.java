package system;

public class AssetLoader implements Runnable {
	private SystemState state;
	
	public AssetLoader(SystemState state) {
		this.state = state;
	}
	
	@Override
	public void run() {
		state.loadAllAnims();
		state.loadAllPSI();
	}

}
