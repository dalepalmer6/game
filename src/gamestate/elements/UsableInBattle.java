package gamestate.elements;

import battlesystem.BattleEntity;

public interface UsableInBattle {
	public int useInBattle(BattleEntity user, BattleEntity target);
}
