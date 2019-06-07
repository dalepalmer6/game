package texteditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import font.DialogTextWindow;
import font.Text;
import mapeditor.Map;
import menu.Menu;
import menu.StartupNew;

public class TextEditorWindow {
	private JFrame frame;
	private JPanel panel;
	private JTextArea inputField;
	private JButton loadTextToEngineButton;
	private JComboBox mapNamesComboBox;
	private Map map;
	private JComboBox mapEntitiesNamesComboBox;
	private JTextArea parsedTextArea;
	private GridBagConstraints c;
	private StartupNew state;
	private JPanel flagCheckboxes;
	private ArrayList<String> flagsOn;
	private JPanel panel2;
	private JPanel overallPanel;
	private JTextField stringNumberInput;
	private JLabel stringLabel;
	private JTextArea stringEditor;
	private JPanel panel3;
	private Component stringAsParsed;
	private String parsedEntityText;
	private String parsedStringText;
	private JTextArea parsedStringView;
	private int entityIndex;
	private boolean needToCreateFlags;
	
	public TextEditorWindow() {
		flagsOn = new ArrayList<String>();
		state = new StartupNew();
		state.init(true);
		//state.textData;
		//state.mapNames;
		frame = new JFrame();
		frame.setSize(1000,1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		mapEntitiesNamesComboBox = new JComboBox();
		
		mapNamesComboBox = new JComboBox(state.mapNames.toArray());
		mapNamesComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadMap();
				reloadEntityTexts();
			}
		});
		
		buildPanel();
	}
	
	public void loadMap() {
		map = state.loadMap((String) mapNamesComboBox.getSelectedItem());
		map.parseMap(1,(String) mapNamesComboBox.getSelectedItem());
	}
	
	public void reloadEntityTexts() {
		mapEntitiesNamesComboBox.removeAllItems();
		for (String s : map.getEntityTexts()) {
			mapEntitiesNamesComboBox.addItem(s);
		}
		panel.repaint();
	}
	
	public void buildPanel() {
		c = new GridBagConstraints();
		panel = new JPanel(new GridBagLayout());
//		panel.setPreferredSize(new Dimension(1000,1000));
		inputField = new JTextArea(10,50);
		inputField.setPreferredSize(new Dimension(10,50));
		inputField.setMaximumSize(new Dimension(10,50));
		inputField.setLineWrap(true);
		inputField.setWrapStyleWord(true);
		inputField.setFont(new Font("Courier New", Font.BOLD, 16));
		
		inputField.getDocument().addDocumentListener(new DocumentListener() {

	        @Override
	        public void removeUpdate(DocumentEvent e) {
//	        	Text textObject = new Text(true,inputField.getText(),0,0,32,32,state.charList);
//				textObject.setParsedString();
//				textObject.setState(state);
//				textObject.parse();
//	        	parsedTextArea.setText(textObject.getParsedText());
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
//	        	Text textObject = new Text(true,inputField.getText(),0,0,32,32,state.charList);
//				textObject.setParsedString();
//				textObject.setState(state);
//				textObject.parse();
//	        	parsedTextArea.setText(textObject.getParsedText());
	        }

	        @Override
	        public void changedUpdate(DocumentEvent arg0) {
//	        	Text textObject = new Text(true,inputField.getText(),0,0,32,32,state.charList);
//				textObject.setParsedString();
//				textObject.setState(state);
//				textObject.parse();
//	        	parsedTextArea.setText(textObject.getParsedText());
	        }
	    });
		
		JScrollPane scroller = new JScrollPane(inputField);
		loadTextToEngineButton = new JButton("Load Text Engine");
//		scroller.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		loadTextToEngineButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				    loadAction();
				  } 
				} );
		parsedTextArea = new JTextArea(10,50);
		parsedTextArea.setPreferredSize(new Dimension(10,50));
		parsedTextArea.setMaximumSize(new Dimension(10,50));
		parsedTextArea.setLineWrap(true);
		parsedTextArea.setWrapStyleWord(true);
		parsedTextArea.setFont(new Font("Serif", Font.ITALIC, 16));
		parsedTextArea.setEditable(false);
		
		mapEntitiesNamesComboBox = new JComboBox();
		mapEntitiesNamesComboBox.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				flagsOn = new ArrayList<String>();
				createFlagCheckboxes();
//				needToCreateFlags = true;
				
			}
		}) ;
		mapEntitiesNamesComboBox.setPreferredSize( new Dimension(100,25) );
		mapEntitiesNamesComboBox.setMaximumSize(new Dimension(100,25));
		
		flagCheckboxes = new JPanel(new GridBagLayout());
		flagCheckboxes.setPreferredSize(new Dimension(250,250));
		
		JButton commitEntityButton = new JButton("Commit Entity");
		commitEntityButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				map.getEntities().get(entityIndex).setText(inputField.getText());
				reloadEntityTexts();
			}
			
		});
		
		JButton saveMapButton = new JButton("Save Map Data");
		saveMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				map.saveMap(true);
			}
		});
		
		int x = 0;
		int y = 0;
		
		c.gridx = x;
		c.gridy = y;
		panel.add(mapNamesComboBox,c);
		
		c.gridx = ++x;
		c.gridy = y;
		panel.add(mapEntitiesNamesComboBox,c);
		x=0;
		
		c.gridx = x;
		c.gridy = ++y;
		panel.add(scroller,c);
		
		c.gridx = ++x;
		c.gridy = y;
		panel.add(parsedTextArea,c);
		
		c.gridx = ++x;
		c.gridy = y;
		
		//load text panel
		panel3 = new JPanel(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.gridx = 0;
		e.gridy = 0;
		panel3.add(flagCheckboxes,e);
		e.gridx = 0;
		e.gridy = 1;
		panel3.add(loadTextToEngineButton,e);
		e.gridx = 1;
		e.gridy = 0;
		panel3.add(commitEntityButton, e);
		e.gridx = 1;
		e.gridy = 1;
		panel3.add(saveMapButton,e);
		
		
		panel3.validate();
		
		panel2 = new JPanel(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.gridx = 0;
		d.gridy = 0;
		panel2.add(new JLabel("Editor for pointed text"),d);
		overallPanel = new JPanel(new GridBagLayout());
		overallPanel.setPreferredSize(new Dimension(1000,1000));
		//create an input field to input a number
		stringLabel = new JLabel("String no.:");
		stringNumberInput = new JTextField(10);
		stringNumberInput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				//parse as a hex value
				int index = Integer.parseInt(stringNumberInput.getText(), 16);
				String s = state.textData.get(index);
				stringEditor.setText(s);
				Text textObject = new Text(true,stringEditor.getText(),0,0,32,32,state.charList);
				textObject.setParsedString();
				textObject.setState(state);
				textObject.parse();
				parsedStringView.setText(textObject.getParsedText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			
			}
			
			
		});
		stringEditor = new JTextArea(10,50);
		stringEditor.setPreferredSize(new Dimension(10,50));
		stringEditor.setMaximumSize(new Dimension(10,50));
		stringEditor.setLineWrap(true);
		stringEditor.setWrapStyleWord(true);
		stringEditor.setFont(new Font("Courier New", Font.BOLD, 16));
		parsedStringView = new JTextArea(10,50);
		parsedStringView.setPreferredSize(new Dimension(10,50));
		parsedStringView.setMaximumSize(new Dimension(10,50));
		JButton stringCommitButton = new JButton("Commit Current String");
		JButton stringSaveButton = new JButton("Save All Strings");
		
		stringCommitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(stringNumberInput.getText(), 16);
				state.textData.set(index,stringEditor.getText()); 
			}
			
			
		});
		
		stringSaveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveTextData();
			}
			
			
		});
		
		d.gridx = 0;
		d.gridy = 1;
		panel2.add(stringLabel,d);
		d.gridx = 1;
		d.gridy = 1;
		panel2.add(stringNumberInput,d);
		d.gridx = 0;
		d.gridy = 2;
		panel2.add(stringEditor,d);
		d.gridx = 1;
		d.gridy = 2;
		panel2.add(parsedStringView,d);
		d.gridx = 2;
		d.gridy = 2;
		panel2.add(stringCommitButton,d);
		d.gridx = 3;
		d.gridy = 2;
		panel2.add(stringSaveButton,d);
		
		GridBagConstraints overall = new GridBagConstraints();
		
		overall.gridx = 0;
		overall.gridy = 0;
		overallPanel.add(panel,overall);
		
		overall.gridx = 1;
		overallPanel.add(panel3,overall);
		
		overall.gridx = 0;
		overall.gridy = 1;
		overallPanel.add(panel2,overall);
		
		panel.validate();
		panel2.validate();
		overallPanel.validate();
		
		frame.add(overallPanel);
//		frame.add(panel2);
		
		frame.pack();
		
		frame.show();
	}
	
	protected void saveTextData() {
		// TODO Auto-generated method stub
		int i = 0;
		String output = "";
		for (String s : state.textData) {
			output += String.format("%03X", i++) + ": " + s + "\n";
		}
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File("data/text.txt"));
			pw.write(output);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public boolean createFlagCheckboxes() {
		flagsOn = new ArrayList<String>();
		String text = (String) mapEntitiesNamesComboBox.getSelectedItem();
		entityIndex = mapEntitiesNamesComboBox.getSelectedIndex();
		if (text == null) {
			return false;
		}
		inputField.setText(text);
		Text textObject = new Text(true,text,0,0,32,32,state.charList);
		textObject.setParsedString();
		textObject.setState(state);
		textObject.parse();
		ArrayList<String> flags = textObject.getFlags();
		flagCheckboxes.removeAll();
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		flagCheckboxes.add(new JLabel("Text Flags"),c);
		
		
		int i = 1;
		for (String s : flags) {
			JCheckBox jcb = new JCheckBox(s);
			jcb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						flagsOn.remove(((JCheckBox) e.getItemSelectable()).getText());
						return;
					}
					flagsOn.add(((JCheckBox) e.getItemSelectable()).getText());
					setFlags();
				}});
			c.gridy = i++;
			flagCheckboxes.add(jcb,c);
		}
		System.out.println(flags);
		frame.revalidate();
		return true;
	}
	
	public void setFlags() {
		state.getGameState().clearFlags();
		state.getMenuStack().clear();
		for (String s : flagsOn) {
			state.getGameState().setFlag(s);
		}
	}
	
	public void loadAction() { 
		System.out.println("Creating the LWJGL context.");
		setFlags();
		Menu m = new Menu(state);
		m.addMenuItem(new DialogTextWindow(inputField.getText(),"Test Entity",state));
		state.getMenuStack().push(m);
		state.createContext();
		state.loadAllImages("");
		state.loadImageData();
		state.gameLoop(true);
		
	}
	
	public static void main(String[] args) {
		TextEditorWindow tw = new TextEditorWindow();
		while (true) {
//			String text = (String) tw.mapEntitiesNamesComboBox.getSelectedItem();
//			tw.inputField.setText(text);
//			Text textObject = new Text(true,text,0,0,32,32,tw.state.charList);
//			textObject.setParsedString();
//			textObject.setState(tw.state);
//			textObject.parse();
			
//			if (tw.needToCreateFlags) {
//				tw.createFlagCheckboxes();
//				tw.setFlags();
				Text text = new Text(tw.inputField.getText(),0,0,0,0,tw.state.charList);
				text.setState(tw.state);
				text.setParsedString();
				text.parse();
				tw.parsedTextArea.setText(text.getParsedText());
				tw.panel.revalidate();
				tw.panel2.revalidate();
				tw.overallPanel.revalidate();
				tw.panel.repaint();
//			}
			
		}
	}
}
