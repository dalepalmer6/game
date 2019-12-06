package system.controller;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputController {
	private Map<String, Boolean> controllerSignals = new HashMap<String, Boolean>();
	private Map<String, String> keyMappings = new HashMap<String, String>();
	public boolean MOUSE_LEFT_DOWN = false;
	public boolean MOUSE_RIGHT_DOWN = false;
	public boolean CONFIRM = false;
	public boolean BACK = false;
	public boolean UP = false;
	public boolean LEFT = false;
	public boolean DOWN = false;
	public boolean RIGHT = false;
	public boolean holdable = false;
	private boolean KEY_NUMPAD8 = false;
	private boolean KEY_NUMPAD4= false;
	private boolean KEY_NUMPAD2= false;
	private boolean KEY_NUMPAD6= false;
	private boolean ENTER = false;
	
	
	public InputController() {
		controllerSignals.put("MOUSE_LEFT_DOWN", MOUSE_LEFT_DOWN);
		controllerSignals.put("MOUSE_RIGHT_DOWN", MOUSE_RIGHT_DOWN);
		controllerSignals.put("CONFIRM", CONFIRM);
		controllerSignals.put("BACK",BACK);
		controllerSignals.put("BACK", BACK);
		controllerSignals.put("UP", UP);
		controllerSignals.put("LEFT", LEFT);
		controllerSignals.put("DOWN", DOWN);
		controllerSignals.put("RIGHT", RIGHT);
		controllerSignals.put("KEY_NUMPAD8",KEY_NUMPAD8);
		controllerSignals.put("KEY_NUMPAD6",KEY_NUMPAD6);
		controllerSignals.put("KEY_NUMPAD4",KEY_NUMPAD4);
		controllerSignals.put("KEY_NUMPAD2",KEY_NUMPAD2);
		controllerSignals.put("ENTER",ENTER);
	}
	
	public boolean anyKeyDown() {
		boolean any = false;
		for (String key : controllerSignals.keySet()) {
			any = any || controllerSignals.get(key);
		}
		return any;
	}
	
	public boolean anyKeyDownExcept(String except) {
		boolean any = false;
		for (String key : controllerSignals.keySet()) {
			if (except.equals(key)) {
				continue;
			}
			any = any || controllerSignals.get(key);
		}
		return any;
	}
	
	public void pollKeyEnter() {
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && !ENTER) {
			System.out.println("Keydown: return");
			setSignal("ENTER", true);
			ENTER = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_RETURN) && ENTER) {
			System.out.println("Keyup: return");
			setSignal("ENTER", false);
			ENTER = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			setSignal("ENTER",false);
		}
	}
	
	public void pollKeyConfirm() {
		if (Keyboard.isKeyDown(Keyboard.KEY_X) && !CONFIRM) {
			System.out.println("Keydown: X");
			setSignal("CONFIRM", true);
			CONFIRM = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_X) && CONFIRM) {
			System.out.println("Keyup: X");
			setSignal("CONFIRM", false);
			CONFIRM = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
			setSignal("CONFIRM",false);
		}
	}
	
	public void pollKeyBack() {
		if (Keyboard.isKeyDown(Keyboard.KEY_Z) && !BACK) {
			System.out.println("Keydown: Z");
			setSignal("BACK", true);
			BACK = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_Z) && BACK) {
			System.out.println("Keyup: Z");
			setSignal("BACK", false);
			BACK = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Z) && !holdable) {
			setSignal("BACK",false);
		}
	}
	
	public void pollKeyUp() {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && !UP) {
			System.out.println("Keydown: UP");
			setSignal("UP", true);
			UP = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_UP) && UP) {
			System.out.println("Keyup: UP");
			setSignal("UP", false);
			UP = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && !holdable) {
			setSignal("UP",false);
		}
	}
	
	public void pollKeyDown() {
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !DOWN) {
			System.out.println("Keydown: DOWN");
			setSignal("DOWN", true);
			DOWN = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_DOWN) && DOWN) {
			System.out.println("Keyup: DOWN");
			setSignal("DOWN", false);
			DOWN = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !holdable) {
			setSignal("DOWN",false);
		}
	}
	
	public void pollKeyLeft() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !LEFT) {
			System.out.println("Keydown: LEFT");
			setSignal("LEFT", true);
			LEFT = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_LEFT) && LEFT) {
			System.out.println("Keyup: LEFT");
			setSignal("LEFT", false);
			LEFT = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !holdable) {
			setSignal("LEFT",false);
		}
	}
	
	public void pollKeyRight() {
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !RIGHT) {
			System.out.println("Keydown: RIGHT");
			setSignal("RIGHT", true);
			RIGHT = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && RIGHT) {
			System.out.println("Keyup: RIGHT");
			setSignal("RIGHT", false);
			RIGHT = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !holdable) {
			setSignal("RIGHT",false);
		}
	}
	
	public void pollKeyNum8() {
		//up
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8) && !KEY_NUMPAD8) {
			System.out.println("Keydown: KEY_NUMPAD8");
			setSignal("KEY_NUMPAD8", true);
			KEY_NUMPAD8 = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8) && KEY_NUMPAD8) {
			System.out.println("Keyup: KEY_NUMPAD8");
			setSignal("KEY_NUMPAD8", false);
			KEY_NUMPAD8 = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8) && !holdable) {
			setSignal("KEY_NUMPAD8",false);
		}
	}
	
	public void pollKeyNum4() {
		//left
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4) && !KEY_NUMPAD4) {
			System.out.println("Keydown: KEY_NUMPAD4");
			setSignal("KEY_NUMPAD4", true);
			KEY_NUMPAD4 = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4) && KEY_NUMPAD4) {
			System.out.println("Keyup: KEY_NUMPAD4");
			setSignal("KEY_NUMPAD4", false);
			KEY_NUMPAD4 = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4) && !holdable) {
			setSignal("KEY_NUMPAD4",false);
		}
	}
	
	public void pollKeyNum6() {
		//right
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6) && !KEY_NUMPAD6) {
			System.out.println("Keydown: KEY_NUMPAD6");
			setSignal("KEY_NUMPAD6", true);
			KEY_NUMPAD6 = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6) && KEY_NUMPAD6) {
			System.out.println("Keyup: KEY_NUMPAD6");
			setSignal("KEY_NUMPAD6", false);
			KEY_NUMPAD6 = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6) && !holdable) {
			setSignal("KEY_NUMPAD6",false);
		}
	}
	
	public void pollKeyNum2() {
		//down
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2) && !KEY_NUMPAD2) {
			System.out.println("Keydown: KEY_NUMPAD2");
			setSignal("KEY_NUMPAD2", true);
			KEY_NUMPAD2 = true;
			return;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2) && KEY_NUMPAD2) {
			System.out.println("Keyup: KEY_NUMPAD2");
			setSignal("KEY_NUMPAD2", false);
			KEY_NUMPAD2 = false;
			holdable = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2) && !holdable) {
			setSignal("KEY_NUMPAD2",false);
		}
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
			holdable = false;
		}
		if (mouseLeft() && !holdable) {
			setSignal("MOUSE_LEFT_DOWN",false);
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
			holdable = false;
		} 
		if (mouseLeft() && !holdable) {
			setSignal("MOUSE_RIGHT_DOWN",false);
		}
	}
	
	public void handleInputs() {
		pollMouseLeft();
		pollMouseRight();
		pollKeyLeft();
		pollKeyRight();
		pollKeyDown();
		pollKeyUp();
		pollKeyConfirm();
		pollKeyBack();
		pollKeyNum8();
		pollKeyNum6();
		pollKeyNum4();
		pollKeyNum2();
		pollKeyEnter();
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

	public void setHoldable(boolean b) {
		holdable = b;
	}

	public void reset() {
		controllerSignals.put("MOUSE_LEFT_DOWN", false);
		controllerSignals.put("MOUSE_RIGHT_DOWN", false);
		controllerSignals.put("CONFIRM", false);
		controllerSignals.put("BACK", false);
		controllerSignals.put("UP", false);
		controllerSignals.put("LEFT", false);
		controllerSignals.put("DOWN", false);
		controllerSignals.put("RIGHT", false);
		MOUSE_LEFT_DOWN = false;
		MOUSE_RIGHT_DOWN = false;
		CONFIRM = false;
		  BACK = false;
		  UP = false;
		  LEFT = false;
		  DOWN = false;
		  RIGHT = false;
		  holdable = false;
	}
}
