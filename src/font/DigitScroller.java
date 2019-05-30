package font;

import canvas.MainWindow;
import global.InputController;
import menu.MenuItem;
import menu.StartupNew;

public class DigitScroller extends SelectionTextWindow {
	private int[] digits;
	private boolean done;
	private int numDigits;
	/*
	 * Takes digits as the number of digits you can scroll
	 * */
	public DigitScroller(StartupNew state, int digits) {
		super("horizontal",0,0,4,1,state);
		numDigits = digits;
		this.digits = new int[digits];
		int i = 0;
		for (int digit : this.digits) {
//			setCurrentOpen(i*32,0);
			setSteps(32,0);
			add(new MenuItem(digit+"",0,0,state));
		}
		killWhenComplete = true;
	}
	

	
	public int getDigitCount() {
		return numDigits;
	}
	
	@Override
	public void handleInput(InputController input) {
		if (!drawOnly) {
//			state.setHoldable(false);
			if (input.getSignals().get("UP")) {
//				updateIndex("U");
				digits[selectedX] = digits[selectedX]+1;
				if (digits[selectedX] > 9) {
					digits[selectedX] = 0;
				}
				state.setSFX("cursverti.wav");
				state.playSFX();
			} else if (input.getSignals().get("DOWN")) {
//				updateIndex("D");
				digits[selectedX] = digits[selectedX]-1;
				if (digits[selectedX] < 0) {
					digits[selectedX] = 9;
				}
				state.setSFX("cursverti.wav");
				state.playSFX();
			} else if (input.getSignals().get("RIGHT")) {
				updateIndex("R");
				state.setSFX("curshoriz.wav");
				state.playSFX();
			}else if (input.getSignals().get("LEFT")) {
				updateIndex("L");
				state.setSFX("curshoriz.wav");
				state.playSFX();
			} else if (input.getSignals().get("CONFIRM")) {
				update();
				state.getGameState().setWindowArgument(getValue());
				done = true;
			} 
			selections.get(selectedY).get(selectedX).setText(digits[selectedX] + "");
			for (int i : digits) {
				System.out.print(i);
			}
			System.out.println();
			
//			else if (input.getSignals().get("BACK")) {
//				state.getMenuStack().pop();
//			}
		}
	}
	
	public boolean isDone() {
		return done;
	}
	
	public void drawWindow(MainWindow m) {}
	
	public int getValue() {
		String numString = "";
		for (int digit : digits) {
			numString += digit;
		}
		return Integer.parseInt(numString);
	}
	
}
