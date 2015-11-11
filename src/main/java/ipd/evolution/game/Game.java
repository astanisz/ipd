package ipd.evolution.game;

import ipd.model.game.Action;
import ipd.model.game.Player;

import org.apache.commons.lang3.tuple.Pair;

public interface Game {

	void play(Pair<Player, Player> players, Pair<Action, Action> actions);

}
