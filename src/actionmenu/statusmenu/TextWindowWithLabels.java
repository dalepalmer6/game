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
		super(true,"Status",256,128,20,9,state);
		labels = new ArrayList<TextLabel>();
	}
	
	public TextWindowWithLabels(StartupNew state, int x, int y, int width, int height) {
		super(true,"",x,y,width,height,state);
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
		int i = 0;
		for (TextLabel label : labels) {
			if (i % 2 == 1 && i > 0) {
				label.draw(m,215,215,227);
			} else {
				label.draw(m);
			}
			i++;
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
