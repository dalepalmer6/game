package actionmenu.statusmenu;

import java.util.ArrayList;

import actionmenu.equipmenu.TextLabel;
import canvas.Controllable;
import canvas.MainWindow;
import font.TextWindow;
import global.InputController;
import menu.StartupNew;

public class TextWindowWithLabels extends TextWindow implements Controllable {
	private ArrayList<TextLabel> labels;
	public TextWindowWithLabels(StartupNew state) {
		super(true,"Test",0,0,15,8,state);
		labels = new ArrayList<TextLabel>();
	}
	
	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		if (input.getSignals().get("CONFIRM")) {
			//show the psi menu for the current member (index, pm, anything)
		} else if (input.getSignals().get("BACK")) {
			state.getMenuStack().pop();
		}
	}
	
	public void createLabel(String s, int x, int y) {
		TextLabel tl = new TextLabel(s,this.x + x,this.y + y,state);
		labels.add(tl);
	}
	
	public void draw(MainWindow m) {
		// TODO Auto-generated method stub
		drawWindow(m);
		drawLabels(m);
	}
	
	public void drawLabels(MainWindow m) {
		for (TextLabel label : labels) {
			label.draw(m);
		}
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	}
}
