package gamestate;

import java.util.HashMap;

import menu.StartupNew;

public class TrainCutscene extends Cutscene {
	private int startIndex = 33;
	private int endIndex = 24;
	private boolean loaded = false;
	private HashMap<Integer, String> mapIndexes;
	private String mapName;
	
	public TrainCutscene(StartupNew m, int trainStartIndex, int trainEndIndex) {
		super(m, new CutsceneData(m,"maps/train.cs",null));
		startIndex = trainStartIndex;
		endIndex = trainEndIndex;
		switch(startIndex) {
		case 0 : mapName = "desert"; break;
		case 6 : mapName = "merrysville north"; break;
		case 21 : mapName = "reindeer"; break;
		case 24 : mapName = "spookane"; break;
		case 33 : mapName = "snowman"; break;
		}
		if (endIndex < startIndex) {
			setReverse();
			cutsceneData = new CutsceneData(m,"maps/train - reverse.cs",null);
		}
		// TODO Auto-generated constructor stub
	}
	
	public void doCutscene() {
		if (!loaded) {
			cutsceneData.setIndex(startIndex);
			curMovement = cutsceneData.getMovementData();
//			cutsceneData.step(reverseOrder);
//			state.getGameState().getTrain().setXY(curMovement.getX(),curMovement.getY());
			DoorEntity e = new DoorEntity("",state.getGameState().getTrain().getX(),state.getGameState().getTrain().getY(),256,256,state,curMovement.getX(),curMovement.getY(),mapName,"");
			e.addToInteractables(state.getGameState().getTrain());
			state.getGameState().getEntityList().add(e);
			e.act();
			
//			state.getGameState().getCamera().snapToEntity(curMovement.getX(),curMovement.getY());
			loaded = true;
			return;
		}
		
		state.setBGM("paradise.ogg");
		state.setAudioOverride(true);
		if (reverseOrder) {
			if (cutsceneData.getIndex() < endIndex - 1) {
				end();
				return;
			}
		}
		else {
			if (cutsceneData.getIndex() > endIndex + 1 ) {
				end();
				return;
			}
		}
		super.doCutscene();
	}
	
	public void onEndAction() {
		state.getGameState().setFlag("train",false);
		state.setAudioOverride(false);
		state.setBGM(state.getGameState().getMap().getBGM());
	}

}
