package entityedit;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import battlesystem.Enemy;
import gamestate.EnemySpawnGroup;
import gamestate.elements.items.Item;
import gamestate.entities.DoorEntity;
import gamestate.entities.EnemySpawnEntity;
import gamestate.entities.Entity;
import gamestate.entities.HotSpot;
import gamestate.entities.PresentEntity;
import menu.mapeditmenu.MapEditMenu;
import menu.mapeditmenu.tools.DoorTool;
import menu.mapeditmenu.tools.SingleEntity;
import system.SystemState;
import system.map.Map;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

public class EntityEditor {

	private JFrame frame;
	private JTextField nameTextField;
//	private JTextField enemy1TextField;
//	private JTextField enemy2TextField;
//	private JTextField enemy3TextField;
//	private JTextField enemy4TextField;
	private JComboBox<EnemySpawnGroup> enemySpawnGroupComboBox;
	private JTextField heightTextField;
	private JTextField widthTextField;
	private JTextField disappearTextField;
	private JTextField appearTextField;
	private JTextField yTextField;
	private JTextField xTextField;
	private JComboBox<String> textureComboBox;
	private JComboBox<String> destMapComboBox;
	private JComboBox<Entity> entityComboBox;
	private ArrayList<Entity> entities;
	private Entity entity;
	private SystemState state;
	private Map map;
	private JComboBox<String>  itemComboBox;
	private JTextField  cutsceneComboBox;
	private JComboBox<String>  enemy1ComboBox;
	private JComboBox<String>  enemy2ComboBox;
	private JComboBox<String>  enemy3ComboBox;
	private JComboBox<String>  enemy4ComboBox;
	private JLabel xyLabel;
	private JTextArea textArea;
	private boolean done;
	private ArrayList<Entity> entitiesInMemory;
	
	public void commitCurrentEntity() {
		String name = nameTextField.getText();
		int width = Integer.parseInt(widthTextField.getText());
		int height = Integer.parseInt(heightTextField.getText());
		String appFlag = appearTextField.getText();
		String disFlag = disappearTextField.getText();
		double x = Double.parseDouble(xTextField.getText());
		double y = Double.parseDouble(yTextField.getText());
		String texture = (String) textureComboBox.getSelectedItem();
		String text = textArea.getText();
		
		String destMap = (String) destMapComboBox.getSelectedItem();
		
		String cutsceneName = cutsceneComboBox.getText();
		
		int item = itemComboBox.getSelectedIndex();
		
//		int enemyIndex1 = enemy1ComboBox.getSelectedIndex();
//		int enemyIndex2 = enemy2ComboBox.getSelectedIndex();
//		int enemyIndex3 = enemy3ComboBox.getSelectedIndex();
//		int enemyIndex4 = enemy4ComboBox.getSelectedIndex();
		
		
		if (entity instanceof DoorEntity && !(entity instanceof HotSpot)) {
			((DoorEntity)entity).setNewParams(x,y,width,height,name,appFlag,disFlag,destMap);
		} else if (entity instanceof HotSpot) {
			((HotSpot)entity).setNewParams(x,y,width,height,name,appFlag,disFlag,cutsceneName);
		} else if (entity instanceof PresentEntity) {
			((PresentEntity)entity).setNewParams(x,y,name,item);
		} else if (entity instanceof EnemySpawnEntity) {
			int index = (((EnemySpawnGroup)enemySpawnGroupComboBox.getSelectedItem()).getIndex());
			((EnemySpawnEntity)entity).setNewParams(index,x,y,width,height);
//			float percent2 = Float.parseFloat(enemy2TextField.getText());
//			float percent3 = Float.parseFloat(enemy3TextField.getText());
//			float percent4 = Float.parseFloat(enemy4TextField.getText());
//			((EnemySpawnEntity)entity).setNewParams(x,y,width,height,name,enemyIndex1,enemyIndex2,enemyIndex3,enemyIndex4,percent1,percent2,percent3,percent4);
		} else {
			//entity
			entity.setNewParams(x,y,width,height,name,texture + ".png",appFlag,disFlag);
			entity.setText(text);
		}
	}
	
	public void setMap(Map m) {
		this.map = m;
	}
	
	public void reload() {
//		destMapComboBox.clear();
		entities = map.getEntities();
		if (entities.size() == 0) {
			PresentEntity pe = new PresentEntity(0,0,0,"firstEntity",state);
			entities.add(pe);
			pe.setSpriteCoords(state.getEntityFromEnum("present").getSpriteCoordinates());
		}
//		entityComboBox.removeAllItems();
		for (Entity e : entities) {
			if (!entitiesInMemory.contains(e)) {
				entitiesInMemory.add(e);
				entityComboBox.addItem(e);
			}
			
		}
		
		if (entity != null) {
			entityComboBox.setSelectedItem(entity);
		}
		
		if (!done) {
			for (String n : state.mapNames) {
				destMapComboBox.addItem(n);
			}
			for (Item n : state.items) {
				itemComboBox.addItem(n.getName());
			}
			for (String e : state.allEntities.keySet()) {
				textureComboBox.addItem(e);
			}
			for (int esg : state.enemySpawnGroups.keySet()) {
				enemySpawnGroupComboBox.addItem(state.enemySpawnGroups.get(esg));
			}
//			for (int n : state.enemies.keySet()) {
//				Enemy en = state.enemies.get(n);
//				enemy1ComboBox.addItem(en.getName());
//				enemy2ComboBox.addItem(en.getName());
//				enemy3ComboBox.addItem(en.getName());
//				enemy4ComboBox.addItem(en.getName());
//			}
			done = true;
		}
		
		updateAllFields();
	}
	
	public void updateAllFields() {
		if (entity == null) {
			entity = (Entity) entityComboBox.getSelectedItem();
		}
		updateAllFieldsWithEntity(entity);
	}
	
	public void updateAllFieldsWithEntity(Entity entity) {
		nameTextField.setText(entity.getName());
		widthTextField.setText(entity.getWidth() + "");
		heightTextField.setText(entity.getHeight() + "");
		disappearTextField.setText(entity.getDisappearFlag());
		appearTextField.setText(entity.getAppearFlag());
		xTextField.setText(entity.getX() + "");
		yTextField.setText(entity.getY() + "");
		textureComboBox.setSelectedItem(entity.getTexture().substring(0,entity.getTexture().indexOf(".png")));
		textArea.setText(entity.getText());
		if (entity instanceof DoorEntity && !(entity instanceof HotSpot)) {
			destMapComboBox.setSelectedIndex(state.mapNames.indexOf(((DoorEntity) entity).getDestMap()));
			xyLabel.setText("( " + ((DoorEntity) entity).getDestX() + "," +((DoorEntity) entity).getDestY() +  " )");
		}
		if (entity instanceof HotSpot) {
			cutsceneComboBox.setText(((HotSpot) entity).getCutsceneName());
		}
		if (entity instanceof PresentEntity) {
			itemComboBox.setSelectedIndex(((PresentEntity) entity).getItemId());
		}
		if (entity instanceof EnemySpawnEntity) {
//			int value = 0;
			enemySpawnGroupComboBox.setSelectedItem(state.enemySpawnGroups.get(((EnemySpawnEntity) entity).getIndex()));
//			enemy1ComboBox.setSelectedIndex(((EnemySpawnEntity) entity).getEnemies().get(1).getId());
//			enemy2ComboBox.setSelectedIndex(((EnemySpawnEntity) entity).getEnemies().get(1).getId());
//			enemy3ComboBox.setSelectedIndex(((EnemySpawnEntity) entity).getEnemies().get(1).getId());
//			enemy4ComboBox.setSelectedIndex(((EnemySpawnEntity) entity).getEnemies().get(1).getId());
//			enemy1TextField.setText(((EnemySpawnEntity) entity).getRates()[0] + "");
//			enemy2TextField.setText(((EnemySpawnEntity) entity).getRates()[1] + "");
//			enemy3TextField.setText(((EnemySpawnEntity) entity).getRates()[2] + "");
//			enemy4TextField.setText(((EnemySpawnEntity) entity).getRates()[3] + "");
		}
		frame.revalidate();
	}
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EntityEditor window = new EntityEditor();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public EntityEditor() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public EntityEditor(SystemState state) {
		this.state = state;
		initialize();
		setMap(((MapEditMenu)state.getMenuStack().peek()).getMapPreview().getMap());
		entitiesInMemory = new ArrayList<Entity>();
		reload();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(1000,500);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel Entities = new JLabel("Name");
		Entities.setBounds(12, 50, 56, 16);
		panel.add(Entities);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(110, 47, 147, 22);
		panel.add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Texture");
		lblNewLabel.setBounds(12, 78, 56, 16);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("x");
		lblNewLabel_1.setBounds(12, 107, 56, 16);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("y");
		lblNewLabel_2.setBounds(12, 136, 56, 16);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Appear Flag");
		lblNewLabel_3.setBounds(12, 165, 116, 16);
		panel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Disappear Flag");
		lblNewLabel_4.setBounds(12, 194, 116, 16);
		panel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Width");
		lblNewLabel_5.setBounds(12, 223, 56, 16);
		panel.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Height");
		lblNewLabel_6.setBounds(12, 252, 56, 16);
		panel.add(lblNewLabel_6);
		
		entityComboBox = new JComboBox();
		entityComboBox.setBounds(12, 15, 154, 22);
		panel.add(entityComboBox);
		entityComboBox.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (done) {
		    		entity = null;
		    		updateAllFields();
		    	}
		    }
		});
		
		JLabel lblNewLabel_7 = new JLabel("Destination Map");
		lblNewLabel_7.setBounds(328, 310, 116, 16);
		panel.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Destination (x,y)");
		lblNewLabel_8.setBounds(328, 339, 116, 16);
		panel.add(lblNewLabel_8);
		
		JLabel lblEnemyList = new JLabel("Enemy List");
		lblEnemyList.setBounds(328, 50, 116, 16);
		panel.add(lblEnemyList);
		
		enemySpawnGroupComboBox = new JComboBox<EnemySpawnGroup>();
		enemySpawnGroupComboBox.setBounds(456, 47, 200, 22);
		panel.add(enemySpawnGroupComboBox);
		
//		enemy1ComboBox = new JComboBox();
//		enemy1ComboBox.setBounds(456, 47, 85, 22);
//		panel.add(enemy1ComboBox);
//		
//		enemy2ComboBox = new JComboBox();
//		enemy2ComboBox.setBounds(456, 78, 85, 22);
//		panel.add(enemy2ComboBox);
//		
//		enemy3ComboBox = new JComboBox();
//		enemy3ComboBox.setBounds(456, 104, 85, 22);
//		panel.add(enemy3ComboBox);
//		
//		enemy4ComboBox = new JComboBox();
//		enemy4ComboBox.setBounds(456, 133, 85, 22);
//		panel.add(enemy4ComboBox);
		
		JLabel lblItem = new JLabel("Item");
		lblItem.setBounds(328, 194, 56, 16);
		panel.add(lblItem);
		
		itemComboBox = new JComboBox();
		itemComboBox.setBounds(456, 191, 147, 22);
		panel.add(itemComboBox);
		
		cutsceneComboBox = new JTextField();
		cutsceneComboBox.setBounds(456, 249, 147, 22);
		panel.add(cutsceneComboBox);
		
		JLabel lblNewLabel_10 = new JLabel("Cutscene");
		lblNewLabel_10.setBounds(328, 252, 56, 16);
		panel.add(lblNewLabel_10);
		
		
//		enemySpawnGroupComboBox.setColumns(10);
		
//		enemy2TextField = new JTextField();
//		enemy2TextField.setBounds(553, 75, 56, 22);
//		panel.add(enemy2TextField);
//		enemy2TextField.setColumns(10);
//		
//		enemy3TextField = new JTextField();
//		enemy3TextField.setBounds(553, 104, 56, 22);
//		panel.add(enemy3TextField);
//		enemy3TextField.setColumns(10);
//		
//		enemy4TextField = new JTextField();
//		enemy4TextField.setBounds(553, 133, 56, 22);
//		panel.add(enemy4TextField);
//		enemy4TextField.setColumns(10);
		
		JButton setDestinationButton = new JButton("Set Destination");
		setDestinationButton.setBounds(559, 335, 160, 25);
		panel.add(setDestinationButton);
		setDestinationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				((MapEditMenu) state.getMenuStack().peek()).setTool(new DoorTool((DoorEntity)entity,state));
				((DoorTool)((MapEditMenu) state.getMenuStack().peek()).getMapTool()).setNewMapName((String)destMapComboBox.getSelectedItem());
			}
			
		});
		
		JButton saveEntityButton = new JButton("Save Entity");
		saveEntityButton.setBounds(798, 14, 154, 25);
		panel.add(saveEntityButton);
		
		saveEntityButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				commitCurrentEntity();
			}
			
		});
		
		JButton saveAllEntitiesButton = new JButton("Save All Entities");
		saveAllEntitiesButton.setBounds(798, 46, 154, 25);
		panel.add(saveAllEntitiesButton);
		saveAllEntitiesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				map.saveMap();
			}
			
		});
		
		JLabel lblText = new JLabel("Text");
		lblText.setBounds(12, 281, 56, 16);
		panel.add(lblText);
		
		JButton loadStringEditorButton = new JButton("Load String Editor");
		loadStringEditorButton.setBounds(110, 277, 147, 25);
		panel.add(loadStringEditorButton);
		loadStringEditorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		heightTextField = new JTextField();
		heightTextField.setBounds(110, 249, 116, 22);
		panel.add(heightTextField);
		heightTextField.setColumns(10);
		
		widthTextField = new JTextField();
		widthTextField.setBounds(110, 220, 116, 22);
		panel.add(widthTextField);
		widthTextField.setColumns(10);
		
		disappearTextField = new JTextField();
		disappearTextField.setBounds(110, 194, 116, 22);
		panel.add(disappearTextField);
		disappearTextField.setColumns(10);
		
		appearTextField = new JTextField();
		appearTextField.setBounds(110, 162, 116, 22);
		panel.add(appearTextField);
		appearTextField.setColumns(10);
		
		yTextField = new JTextField();
		yTextField.setBounds(110, 133, 116, 22);
		panel.add(yTextField);
		yTextField.setColumns(10);
		
		xTextField = new JTextField();
		xTextField.setBounds(110, 104, 116, 22);
		panel.add(xTextField);
		xTextField.setColumns(10);
		
		xyLabel = new JLabel("( x, y )");
		xyLabel.setBounds(456, 339, 56, 16);
		panel.add(xyLabel);
		
		destMapComboBox = new JComboBox();
		destMapComboBox.setBounds(456, 307, 147, 22);
		panel.add(destMapComboBox);
		
		JButton newEntityButton = new JButton("New Entity");
		newEntityButton.setBounds(178, 14, 97, 25);
		panel.add(newEntityButton);
		newEntityButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				// TODO Auto-generated method stub
				Entity e = state.allEntities.get("ninten").createCopy(0,0,24,32,"NewEntityName");
				e.setText("New Entity");
				((MapEditMenu)state.getMenuStack().peek()).getMapPreview().setAddedEntity(e);
				reload();
				updateAllFields();
			}
			
		});
		
		JButton newDoorButton = new JButton("New Door");
		newDoorButton.setBounds(287, 14, 97, 25);
		panel.add(newDoorButton);
		newDoorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				// TODO Auto-generated method stub
				DoorEntity e = new DoorEntity("Description",0,0,32,32,state,0,0,"podunk","Text");
				((MapEditMenu)state.getMenuStack().peek()).getMapPreview().setAddedEntity(e);
				reload();
				updateAllFields();
			}
			
		});
		
		JButton newHotSpotButton = new JButton("New HotSpot");
		newHotSpotButton.setBounds(394, 14, 118, 25);
		panel.add(newHotSpotButton);
		newHotSpotButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				// TODO Auto-generated method stub
				HotSpot e = new HotSpot("Description",0,0,32,32,state,0,0,"podunk","Text","Cutscene Name");
				((MapEditMenu)state.getMenuStack().peek()).getMapPreview().setAddedEntity(e);
				updateAllFields();
			}
			
		});
		
		JButton newSpawnButton = new JButton("New Spawn");
		newSpawnButton.setBounds(512, 14, 97, 25);
		panel.add(newSpawnButton);
		newSpawnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				// TODO Auto-generated method stub
				Entity e = new EnemySpawnEntity(0,0,0,32,32,state,"");
				e.setText("New Enemy Group // this is never shown anyways");
				((MapEditMenu)state.getMenuStack().peek()).getMapPreview().setAddedEntity(e);
				reload();
				updateAllFields();
			}
			
		});
		
		JButton newPresButton = new JButton("New Pres");
		newPresButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				PresentEntity pe = new PresentEntity(0,0,0,"New Present",state);
				pe.setSpriteCoords(state.getEntityFromEnum("present").getSpriteCoordinates());
				((MapEditMenu)state.getMenuStack().peek()).getMapPreview().setAddedEntity(pe);
				reload();
				updateAllFields();
			}
			
		});
		newPresButton.setBounds(621, 14, 97, 25);
		panel.add(newPresButton);
		
		textureComboBox = new JComboBox();
		textureComboBox.setBounds(110, 75, 116, 22);
		panel.add(textureComboBox);
		
		textArea = new JTextArea();
		textArea.setBounds(12, 307, 304, 120);
		panel.add(textArea);
		
		JButton btnSet = new JButton("Set");
		btnSet.setBounds(238, 103, 56, 54);
		panel.add(btnSet);
		btnSet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				((MapEditMenu) state.getMenuStack().peek()).setTool(new SingleEntity(entity,state));
			}
			
		});
	}
}
