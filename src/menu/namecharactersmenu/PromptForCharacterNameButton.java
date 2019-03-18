package menu.namecharactersmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import menu.MenuItem;
import menu.StartupNew;

public class PromptForCharacterNameButton extends MenuItem {
	
	public PromptForCharacterNameButton(String text, int x, int y,StartupNew m ) {
		super(text,x,y,m);
	}
	
	public void execute() {
		//create a new JFrame with a Textfield to input the name
		JOptionPane name = new JOptionPane();
		String n = name.showInputDialog("Enter a Name:");
		this.setText(n);
	}
	
}
