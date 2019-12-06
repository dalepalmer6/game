package system;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import battlesystem.Enemy;
import battlesystem.EnemyAction;
import battlesystem.menu.BattleMenu;
import battlesystem.options.EnemyOptionPanel;
import canvas.renderer.models.RawModel;
import canvas.renderer.shaders.BattleBGShader;
import gamestate.EnemySpawnGroup;
import gamestate.EntityStats;
import gamestate.GameState;
import gamestate.MotherGameState;
import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import gamestate.elements.psi.PSIAttackUsableInAndOutOfBattle;
import gamestate.elements.psi.PSIAttackUsableInBattle;
import gamestate.elements.psi.PSIAttackUsableOutOfBattle;
import gamestate.entities.DoorEntity;
import gamestate.entities.Entity;
import gamestate.partymembers.PartyMember;
import gamestate.psi.PSIClassification;
import gamestate.psi.PSIClassificationList;
import gamestate.psi.PSIFamily;
import menu.animation.Animation;
import menu.text.MotherTextEngine;
import menu.text.TextEngine;

public class MotherSystemState extends SystemState {
	public MotherGameState gameState;
	public ArrayList<PartyMember> party = new ArrayList<PartyMember>();
	public ArrayList<String> mapNames = new ArrayList<String>();
	public ArrayList<Item> items;
	public ArrayList<PSIAttack> psi;
	public PSIClassificationList psiClassList;
	public HashMap<Integer,Enemy> enemies;
	public HashMap<Integer,EnemySpawnGroup> enemySpawnGroups;
	public boolean inBattle;
	public BattleMenu battleMenu;
	private Item itemToBuy;
	private String teleportDest;
	private int teleportDestX;
	private int teleportDestY;
	private double savedXPos;
	private double savedYPos;
	private String savedMapName;
	private boolean doTeleportRoutine;
	private boolean doShakeScreen;
	private double shakeFactor;
	private double shakeTimer;
	private boolean doorCollided;
	private boolean textEditor;
	public HashMap<Integer,EnemyAction> enemyActions;
	private EnemyOptionPanel eop;
	private int indexOfParty;
	private int cameraShake;
	private boolean setStartFlags;
	private boolean doneIntro;
	public boolean justTextData;
	private boolean shouldDrawBattleBG;
	
	public void setStartFlags() {
		setStartFlags = true;
	}
	
	public boolean getDoorCollided() {
		return doorCollided;
	}
	
	public void setDoorCollided(boolean  b) {
		doorCollided = b;
	}
	
	public void addPartyMember(PartyMember n) {
		this.party.add(n);
	}
	
	@Override
	public MotherGameState getGameState() {
		return gameState;
	}
	
	public Entity getEntityFromEnum(String name) {
		return allEntities.get(name);
	}
	
	public String[] loadMapNames() {
		File file = new File("maps/");
		String[] mapNames = file.list();
		for (String name : mapNames) {
			this.mapNames.add(name);
		}
		return mapNames;
	}
	
	public ArrayList<String> getMapNames() {
		return mapNames;
	}
	
	public system.map.Map loadMap(String mapname) { 
		return new system.map.Map(mapname,34,34, null, this,true);
	}
	
	public void loadAllEnemyGroups() {
		int x = 0;
		enemySpawnGroups = new HashMap<Integer,EnemySpawnGroup>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("data/enemygroups.data"));
			String rowIds = "";
			String rowPercents = "";
			while ((rowIds  = br.readLine()) != null) {
				String[] enemyIds = rowIds.split(",");
				rowPercents = br.readLine();
				String[] spawnPercs = rowPercents.split(",");
				int[] ids = new int[enemyIds.length];
				float[] percs = new float[spawnPercs.length];
				for (int i = 0; i < enemyIds.length; i++) {
					ids[i] = Integer.parseInt(enemyIds[i]);
				}
				for (int i = 0; i < spawnPercs.length; i++) {
					percs[i] = Float.parseFloat(spawnPercs[i]);
				}
				EnemySpawnGroup esg = new EnemySpawnGroup(x,ids,percs,this);
				enemySpawnGroups.put(x,esg);
				x++;
			}
			
		} catch(IOException e) {
			
		}
	}
	
	public void loadAllPSI() {
		psi = new ArrayList<PSIAttack>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/psi.csv"));
			br.readLine();//skip the headers
			String row= "";
			int id = 0;
			String name;
			String desc;
			int target;
			boolean inBattleUsable;
			boolean outBattleUsable;
			int action = 0;
			String anim;
			String classification;
			String family;
			String stage;
			int minDmg;
			int maxDmg;
			int ppUse;
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				id = Integer.parseInt(split[0]);
				name = split[1];
				desc = split[2];
				target = Integer.parseInt(split[3]);
				inBattleUsable = Boolean.parseBoolean(split[4]);
				outBattleUsable = Boolean.parseBoolean(split[5]);
				action = Integer.parseInt(split[6]);
				anim = split[7];
				classification = split[8];
				family = split[9];
				stage = split[10];
				minDmg = Integer.parseInt(split[11]);
				maxDmg = Integer.parseInt(split[12]);
				ppUse = Integer.parseInt(split[13]);
				PSIAttack psiAttack = null;
				if (inBattleUsable && outBattleUsable) {
					psiAttack = new PSIAttackUsableInAndOutOfBattle(id,name,desc,target,action,anim,classification,family,stage,ppUse);
					psi.add(psiAttack);
				} else if (inBattleUsable){
					psiAttack = new PSIAttackUsableInBattle(id,name,desc,target,action,anim,classification,family,stage,ppUse);
					psi.add(psiAttack);
				} else if (outBattleUsable) {
					psiAttack = new PSIAttackUsableOutOfBattle(id,name,desc,target,action,anim,classification,family,stage,ppUse);
					psi.add(psiAttack);
				}
				psiAttack.setMinMaxDmg(minDmg,maxDmg);
				if (anim.equals("undef")) {
					
				} else {
					Animation animate = new Animation(this,anim,0,0,mainWindow.getScreenWidth(),mainWindow.getScreenHeight());
//					animate.createAnimation();
					psiAttack.setAnim(animate);
				}
			}
			//create the classifications ordering now.
			psiClassList = new PSIClassificationList();
			ArrayList<PSIClassification> psiClasses = new ArrayList<PSIClassification>();
			ArrayList<PSIFamily> psiFamilies = new ArrayList<PSIFamily>();
			ArrayList<String> encounteredFamilies = new ArrayList<String>();
			ArrayList<String> encounteredClasses = new ArrayList<String>();
			for (PSIAttack pi : psi) {
				//pass 1, get all unique families from PSIAttacks, adding the stages to the families
				//pass 2, get all unique classifications from families, adding the families to the classifications
				String fam = pi.getFamily();
				if (!encounteredFamilies.contains(fam)) {
					encounteredFamilies.add(fam);
					//for each element that pi != p2, check if p2's family is = fam. if so, add to teh family
					PSIFamily createdFam = new PSIFamily(fam);
					for (PSIAttack pi2 : psi) {
						if (pi2.getFamily().equalsIgnoreCase(fam)) {
							createdFam.addStage(pi2);
						}
					}
					psiFamilies.add(createdFam);
				}
			}
			for (PSIFamily pf : psiFamilies) {
				String classif = pf.getStage(0).getClassification();
				if (!encounteredClasses.contains(classif)) {
					encounteredClasses.add(classif);
					PSIClassification psiClass = new PSIClassification(classif);
					for (PSIFamily pf2 : psiFamilies) {
						String class2 = pf2.getStage(0).getClassification();
						if (classif.equalsIgnoreCase(class2)) {
							psiClass.addFamily(pf2);
						}
					}
					psiClassList.addClassification(psiClass);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadAllItems() {
		items = new ArrayList<Item>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/items.csv"));
			br.readLine();//skip the headers
			String row= "";
			int id = 0;
			String name;
			String desc;
			int target;
			int equippable;
			boolean inBattleUsable;
			boolean outBattleUsable;
			int action = 0;
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				id = Integer.parseInt(split[0]);
				name = split[1];
				desc = split[2];
				target = Integer.parseInt(split[3]);
				equippable = Integer.parseInt(split[4],16);
				inBattleUsable = Boolean.parseBoolean(split[5]);
				outBattleUsable = Boolean.parseBoolean(split[6]);
				action = Integer.parseInt(split[7]);
				int off = Integer.parseInt(split[8]);
				int def = Integer.parseInt(split[9]);
				int spd = Integer.parseInt(split[10]);
				int luck = Integer.parseInt(split[11]);
				int hp = Integer.parseInt(split[12]);
				int pp = Integer.parseInt(split[13]);
				long resists = Long.parseLong(split[14]);
				String participle = split[15];
				int value = Integer.parseInt(split[16]);
				int useVariable = Integer.parseInt(split[17]);
				if ((equippable & 15) != 0) {
					EquipmentItem newItem = new EquipmentItem(id,name,desc,target,action,equippable,off,def,spd,luck,hp,pp,resists,participle,value);
					newItem.setUsage(false,false);
					items.add(newItem);
				} else {
					Item newItem = new Item(id,name,desc,target,action,equippable,participle,value,useVariable);
					newItem.setUsage(inBattleUsable,outBattleUsable);
					items.add(newItem);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadAllEnemies() {
		String pathToEntity = "data/enemies.csv";
		File file = new File(pathToEntity);
		enemies = new HashMap<Integer,Enemy>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			//skip headers
			String row = br.readLine();
			int i = 0;
			while ((row=br.readLine()) != null) {
				String[] data = row.split(",");
				String name=data[1];
				int hp=Integer.parseInt(data[2]);
				int pp=Integer.parseInt(data[3]);
				int off=Integer.parseInt(data[4]);
				int def=Integer.parseInt(data[5]);
				int vit=Integer.parseInt(data[6]);
				int iq=Integer.parseInt(data[7]);
				int speed=Integer.parseInt(data[8]);
				int guts=Integer.parseInt(data[9]);
				int luck=Integer.parseInt(data[10]);
				int xp = Integer.parseInt(data[11]);
				int money = Integer.parseInt(data[12]);
				String texture = data[13];
//				int width = Integer.parseInt(data[13]);
//				int height = Integer.parseInt(data[14]);
				String entityName = data[14];
				String[] potentialEnemyActions = data[17].split("_");
				String bgm = data[18];
				String battleBG = data[19];
				String predicate = data[20];
				String resist = data[21];
				int numberAllies = Integer.parseInt(data[22]);
				EnemyAction[] enemyActions = new EnemyAction[potentialEnemyActions.length];
				for (int x = 0; x < potentialEnemyActions.length; x++) {
					enemyActions[x] = this.enemyActions.get(Integer.parseInt(potentialEnemyActions[x]));
				}
//				public EntityStats(int lvl,int chp, int cpp, int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck, int vit,int curxp) {
				EntityStats stats = new EntityStats(0,hp,pp,hp,pp,off,def,iq,speed,guts,luck,vit,xp);
				Enemy e = new Enemy(i,texture,name,stats,xp,money,entityName,this);
				e.setActions(enemyActions);
				e.setBGM(bgm);
				e.setBattleBG(battleBG);
				e.setResistances(Integer.parseInt(resist));
				e.setMaxAllies(numberAllies);
				if (predicate.equals(" ")) {
					e.setPredicate("");
				} else {
					e.setPredicate(predicate);
				}
				
				enemies.put(i++,e);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void init(boolean justTextData) {
		loadAllImages("enemies");
		super.init(justTextData);
		loadAllItems();
		loadAllEnemyActions();
		loadAllEnemies();
		loadAllEnemyGroups();
		loadAllStrings();	
	}
	
	private void loadAllEnemyActions() {
		String pathToEntity = "data/enemyactions.csv";
		File file = new File(pathToEntity);
		enemyActions = new HashMap<Integer,EnemyAction>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			//skip headers
			String row = br.readLine();
			int i = 0;
			while ((row=br.readLine()) != null) {
				String[] data = row.split(",");
				String text = data[1];
				int action = Integer.parseInt(data[2]);
				int useVar = Integer.parseInt(data[3]);
				int target = Integer.parseInt(data[4]);
				EnemyAction ea = new EnemyAction(text,action,useVar,target);
				enemyActions.put(i++,ea);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = getMainWindow().getScreenHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
//	public void update() {
//		if (restoreAudioWhenDone && !bgm.isPlaying()) {
//			restoreAudioWhenDone = false;
//			setOldBGM();
//		}
//		if (textEditor && menuStack.isEmpty()) {
//			Display.destroy();
//		}
//		if (currentCutscene != null) {
//			if (currentCutscene.needToRemove()) {
//				currentCutscene = null;
//			} else {
//				currentCutscene.doCutscene();
//			}
//			
//		}
//		if (fadeOutIsDone) {
//			Menu savedMenu = menuStack.pop();
//			if (menuStack.peek() != null) {
//				menuStack.peek().doDoneFadeOutAction();
//			} 
////			else {
////				savedMenu.doDoneFadeOutAction();
////				menuStack.push(savedMenu);
////			}
//			
//			fadeOutIsDone = false;
//		}
//		if (shouldFadeIn) {
//			menuStack.pop();
//			if (needToAddMenu!=null) {
//				menuStack.push(needToAddMenu);
//				needToAddMenu = null;
//			}
//			
//			shouldFadeIn = false;
//			AnimationMenu ffb = new AnimationMenu(this);
//			ffb.createAnimMenu(new AnimationFadeFromBlack(this));
//			getMenuStack().push(ffb);
//		}
//		if (bgm != null) {
//			if (bgm.isPlaying() && bgm.getPosition() >= bgmEnd && !playOnce) {
//				bgm.setPosition(bgmStart);
//			}
//			if (!bgm.isPlaying() && savedAudio) {
//				setOldBGM();
//			}
//		}
//		if (drawAllMenus) {
//			for (Menu c : menuStack.getMenus()) {
//				//only update the top one 
//				ArrayList<DrawableObject> list = new ArrayList<DrawableObject>();
//				if (c != null) {
//					list.addAll(c.getDrawableObjects());
//					list.addAll(c.getMenuItems());
//					addToDrawables(list);
//				}
//			}
//		} else {
//			Menu c = getMenuStack().peek();
//			if (c != null) {
//				ArrayList<DrawableObject> list = new ArrayList<DrawableObject>();
//				list.addAll(c.getDrawableObjects());
//				list.addAll(c.getMenuItems());
//				addToDrawables(list);
////				c.updateAll(input);
//			}
//		}
//		
//		Menu c = getMenuStack().peek();
//		if (c != null) {
//			c.updateAll(input);
//			if (doShakeScreen) {
////				factor;
//				if (shakeTimer >= 60) {
//					doShakeScreen = false;
//				}
//				shakeTimer++;
//				for (MenuItem i : c.getMenuItems()) {
//					double applyShake = 50*shakeFactor * (1/(4*shakeTimer)) * Math.sin(shakeTimer*Math.PI/4) ;
//					i.setShakingY(applyShake);
//				}
//			}
//		}
//		
//		if (!(menuStack.peek() instanceof BattleMenu) && inBattle && !(menuStack.peek() instanceof AnimationMenu)) {
//			battleMenu.updateAll(input);
//		}
//		
//		if (inBattle && eop != null) {
//			if (!(menuStack.peek() instanceof SelectTargetMenu)) {
//				eop.setSelected(-2);
//			}
////			if (menuStack.peek() instanceof SelectTargetMenu) {
//				eop.updateAnim();
////			}
//		}
//		
////		if (battleMenu != null && menuStack.peek() != battleMenu) {
////			battleMenu.updateAll(input);
////		}
//		
//		
////		input.setHoldable(false);
//		Point mouse = getMainWindow().getMouseCoordinates();
//		for (Drawable d : getDrawables()) {
//			if (d instanceof Hoverable) {
//				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
//					((Hoverable) d).hoveredAction();
////					if (d instanceof LeftClickableItem) {
//						input.setHoldable(false);
//						if (d instanceof MapPreview) {
//							input.setHoldable(true);
//						}
//						((LeftClickableItem)d).checkInputs(input);
//						
////						input.setHoldable(false);
////					}
//				} else {
//					((Hoverable) d).unhoveredAction();
//				}
//			}
//		}
//		
//		if (gameState != null) {
//			gameState.updateTimer();
//			
//			input.setHoldable(true);
//			gameState.updatePartyMembers();
//			if (gameState.getFlag("buyingItem") && itemToBuy != null) {
//				menuStack.pop();
//				if (gameState.getFundsOnHand() < itemToBuy.getValue()) {
//					SimpleDialogMenu.createDialogBox(this,"Well[WAIT60] this is embarassing.[WAIT30][NEWLINE]You don't appear to have the funds to buy this.");
//				} else {
//					SimpleDialogMenu.createDialogBox(this,"Alright, it's all yours![ADDITEM_" + itemToBuy.getId() +"] ");
//					gameState.spendFunds(itemToBuy.getValue());
//				}
//				gameState.setFlag("buyingItem",false);
//				itemToBuy = null;
//			}
//			if (gameState.getFlag("saveGame")) {
//				gameState.setFlag("saveGame",false);
//				menuStack.push(new SelectSaveFileMenu(this));
//			}
//			if (doTeleportRoutine)  {
//				doTeleportRoutine = false;
//				gameState.setFlag("teleporting");
//			}
//		}
//		
//		if (!textEditor && gameState != null && (menuStack.isEmpty() || menuStack.peek() instanceof AnimationMenuFadeFromBlack)) {
////			input.setHoldable(true);
//			gameState.update(input);
//		}
//		
//		if (!menuStack.isEmpty()) {
//			if (menuStack.peek().getCanUpdateGameState()) {
//				gameState.update(input);
////				gameState.updateEntities(input,true);
//			}
//		}
//		
//		
//		for (DrawableObject d : drawables) {
//			if (d instanceof SelectionTextWindow || d instanceof InvisibleMenuItem) {
//				input.setHoldable(false);
//			}
//		}
//		
//		if (gameState != null && menuStack.peek() instanceof AnimationMenu) {
////			if (menuStack.peek().isSwirl()) {
//				gameState.updateEntities(input,false);
////			}
//		}
//		
//		//change this from AnimationMenu to when a boolean is set
//		if (menuStack.peek() instanceof AnimationMenu && gameState == null) {
//			if (((AnimationMenu)menuStack.peek()).isComplete()) {
//				menuStack.pop();
//				menuStack.pop();
//				needToPop = false;
//				GameState gs = new GameState(this);
//				this.setGameState(gs);
//				if (!createNewFile) {
//					gs.loadFromSaveFile(saveFileName);
//					gameState.loadMapData();
//				}else {
////					gs.loadFromSaveFile("test");
//					setAudioOverride(true);
//					gameState.setDoIntro(true);
//				}
//			}
//		}
//	}
	
	@Override
	public void drawGameState() {
		if (shouldDrawBattleBG) {
			shouldDrawBattleBG = false;
			BattleBGShader battleBGShader = new BattleBGShader();
			battleBGShader.start();

			canvas.renderer.entities.Entity entity = mainWindow.getBattleBGModel();

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,entity.getModel().getModelTexture().getTextureId());

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,mainWindow.getPalette().getTextureID());

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);


			mainWindow.useShader(battleBGShader);
			RawModel model = entity.getModel().getModel();
			GL30.glBindVertexArray(model.getVaoId());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);

			GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);

			battleBGShader.stop();

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureAtlas.getTexture().getTextureID());
		} else {
			super.drawGameState();
		}
	}
	
	public void setDrawBattleBG(boolean b) {
		shouldDrawBattleBG = b;
	}
	
	public void run(){
		now = System.nanoTime();
		init();
		boolean running = true;
		gameLoop(running);
//		cleanup();
	}

	public void setGameState(GameState gs) {
		// TODO Auto-generated method stub
		this.gameState = (MotherGameState) gs;
	}
	
	public GameState createGameState() {
		return new MotherGameState(this);
	}

	public void setItemToBuy(Item item) {
		// TODO Auto-generated method stub
		itemToBuy = item;
	}

	public void setTeleportVariables(String newMapName, int newX, int newY, boolean b) {
		// TODO Auto-generated method stub
		teleportDest = newMapName;
		teleportDestX = newX;
		teleportDestY = newY;
		gameState.setTeleportVariables(newMapName,newX,newY);
		doTeleportRoutine = b;
	}

	public void setShakeVariables(int damage, boolean b) {
		// TODO Auto-generated method stub
		shakeTimer = 0;
		doShakeScreen = b;
		shakeFactor = damage;
	}

	public void saveCoordinates() {
		// TODO Auto-generated method stub
		savedXPos = gameState.getPlayer().getX();
		savedYPos = gameState.getPlayer().getY();
		savedMapName = gameState.getMap().getMapId();
	}
	
	public DoorEntity createWarpDoor() {
		//public DoorEntity(String desc,int x, int y, int width, int height, SystemState m, int destX, int destY, String system.map,String text) {
		return new DoorEntity("",gameState.getPlayer().getX(),gameState.getPlayer().getY(),256,256,this,(int)savedXPos,(int)savedYPos,savedMapName,"");
	}

	public DoorEntity createMagicantWarp() {
		// TODO Auto-generated method stub
		return new DoorEntity("",gameState.getPlayer().getX(),gameState.getPlayer().getY(),256,256,this,1698*4,3389*4,"magicant","");
	}

	public void setEOP(EnemyOptionPanel eop) {
		// TODO Auto-generated method stub
		this.eop = eop;
	}

	public void setIndexOfParty(int partyIndex) {
		// TODO Auto-generated method stub
		indexOfParty = partyIndex;
	}
	
	public int getPartyIndex() {
		int i = indexOfParty;
		indexOfParty = -1;
		return i;
	}

	public void setDoneIntro() {
		// TODO Auto-generated method stub
		doneIntro = true;
	}
	
	public boolean getDoneIntro() {
		return doneIntro;
	}
	
	@Override
	public TextEngine createTextEngine(boolean drawAll, String text, int x, int y, int w, int h) {
		return new MotherTextEngine(drawAll, text ,x , y ,w, h, charList);
	}
}