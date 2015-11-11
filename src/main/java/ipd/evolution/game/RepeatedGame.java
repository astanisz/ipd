package ipd.evolution.game;

import ipd.model.game.Player;

import org.apache.commons.lang3.tuple.Pair;

public interface RepeatedGame {

	void play(Pair<Player, Player> players);
}
