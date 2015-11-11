package ipd.evolution.manager;

import ipd.evolution.Crossover;
import ipd.evolution.Mutation;
import ipd.evolution.game.impl.RepeatedGameImpl;
import ipd.evolution.impl.CrossoverImpl;
import ipd.evolution.impl.MutationImpl;
import ipd.model.game.Player;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

public class EvolutionManager {

	private List<Player> players;
	private RepeatedGameImpl game = new RepeatedGameImpl();
	private Crossover crossover = new CrossoverImpl();
	private Mutation mutation = new MutationImpl();

	public void Step() {
		List<Pair<Player, Player>> matchedPlayers = matchPlayers();
		// play repeated game
		matchedPlayers.forEach(pair -> game.play(pair));
		crossover.cross(players);
		mutation.mutate(players);
	}

	public void setUp() {
		// TODO:
	}

	private List<Pair<Player, Player>> matchPlayers() {
		List<Pair<Player, Player>> matchedPlayers = Lists.newLinkedList();
		for (int i = 0; i < players.size() - 1; i += 2) {
			matchedPlayers.add(Pair.of(players.get(i), players.get(i + 1)));
		}
		return matchedPlayers;
	}
}
