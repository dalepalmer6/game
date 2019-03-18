package global;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;

public class InputController {
	private Map<String, Boolean> controllerSignals = new HashMap<String, Boolean>();
	private Map<String, String> keyMappings = new HashMap<String, String>();
	private boolean MOUSE_LEFT_DOWN = false;
	private boolean MOUSE_RIGHT_DOWN = false;
	private boolean CONFIRM = false;
	private boolean BACK = false;
	private boolean UP = false;
	private boolean LEFT = false;
	private boolean DOWN = false;
	private boolean RIGHT = false;
	
	
	public InputController() {
		controllerSignals.put("MOUSE_LEFT_DOWN", MOUSE_LEFT_DOWN);
		controllerSignals.put("MOUSE_RIGHT_DOWN", MOUSE_RIGHT_DOWN);
		controllerSignals.put("CONFIRM", CONFIRM);
		controllerSignals.put("BACK", BACK);
		controllerSignals.put("UP", UP);
		controllerSignals.put("LEFT", LEFT);
		controllerSignals.put("DOWN", DOWN);
		controllerSignals.put("RIGHT", RIGHT);
	}
	
	public void pollMouseLeft() {
		if (mouseLeft() && !MOUSE_LEFT_DOWN) {
			System.out.println("Clicking Mouse Left");
			setSignal("MOUSE_LEFT_DOWN", true);
			MOUSE_LEFT_DOWN = true;
			return;
		} else if (!mouseLeft() && MOUSE_LEFT_DOWN) {
			System.out.println("Unset Mouse Left");
			setSignal("MOUSE_LEFT_DOWN", false);
			MOUSE_LEFT_DOWN = false;
		}
	}
	
	public void pollMouseRight() {
		if (mouseRight() && !MOUSE_RIGHT_DOWN) {
			System.out.println("Clicking Mouse Right");
			setSignal("MOUSE_RIGHT_DOWN", true);
			MOUSE_RIGHT_DOWN = true;
			return;
		} 
		else if (!mouseRight() && MOUSE_RIGHT_DOWN) {
			System.out.println("Unset Mouse Right");
			setSignal("MOUSE_RIGHT_DOWN", false);
			MOUSE_RIGHT_DOWN = false;
			return;
		} 
	}
	
	public void handleInputs() {
		pollMouseLeft();
		pollMouseRight();
		
	}
	
	public boolean mouseRight() {
		if (Mouse.isButtonDown(1)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean mouseLeft() {
		if (Mouse.isButtonDown(0)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setSignal(String key, boolean state) {
		if (!controllerSignals.containsKey(key)) {
			System.err.println("Invalid button, \"" + key + "\" cannot set state.");
			return;
		} else {
			controllerSignals.put(key, state);
		}
	}
	
	public Map<String, Boolean> getSignals() {
		return controllerSignals;
	}
}
