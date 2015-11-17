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

	private static List<Player> players;
	private static RepeatedGameImpl game = new RepeatedGameImpl();
	private static Crossover crossover = new CrossoverImpl();
	private static Mutation mutation = new MutationImpl();

	public static void main(String[] args) {
		setUp();
		for (int i = 0; i < 10; i++) {
			System.out.println("Step: " + i);
			step();
			System.out.println("Payoffs: ");
			players.forEach(p -> System.out.println("Player " + players.indexOf(p) + " " + p.getPayOff()));
		}
	}

	public static void setUp() {
		players = ExemplaryPopulationFactory.create(2);
	}

	public static void step() {
		List<Pair<Player, Player>> matchedPlayers = matchPlayers();
		// play repeated game
		matchedPlayers.forEach(pair -> game.play(pair));
		crossover.cross(players);
		mutation.mutate(players);
	}

	private static List<Pair<Player, Player>> matchPlayers() {
		List<Pair<Player, Player>> matchedPlayers = Lists.newLinkedList();
		for (int i = 0; i < players.size() - 1; i += 2) {
			matchedPlayers.add(Pair.of(players.get(i), players.get(i + 1)));
		}
		return matchedPlayers;
	}
}
