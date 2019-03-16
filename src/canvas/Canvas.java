package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import global.GlobalVars;

public class Canvas extends JPanel implements ActionListener {
	//a timer that will fire an event 60 times per second (60fps)
			private Timer timer = new Timer(1000/60, this);
			
			public Canvas() {
				this.timer.start();
			}
			
			public void actionPerformed(ActionEvent ev) {
				if (ev.getSource() == timer) {
					//redraw the frame 60 times per second.
					repaint();
				}
			}
	
	public Point getMouseCoordinates() {
		return this.getMousePosition();
	}
	
	public void paint(Graphics g) {
		//draw the mouse coordinates on the canvas as well
		super.paint(g);
		g.setColor(Color.black);
		for (Drawable d : GlobalVars.getDrawables()) {
			d.draw(g);
		}
	}
	
	
}
