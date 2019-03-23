package menu.namecharactersmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class PromptForCharacterNameButton extends MenuItem {
	
	public PromptForCharacterNameButton(String text, int x, int y,StartupNew m ) {
		super(text,x,y,m);
	}
	
	public String execute() {
//		JOptionPane name = new JOptionPane();
//		String n = name.showInputDialog("Enter a Name:",text);
//		this.setText(n);
		CharacterNameInputMenu m = new CharacterNameInputMenu(state);
		m.create();
		state.getMenuStack().push(m);
		return null;
	}
	
}
