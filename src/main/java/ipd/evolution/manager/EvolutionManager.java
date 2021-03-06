package ipd.evolution.manager;

import ipd.evolution.Crossover;
import ipd.evolution.Mutation;
import ipd.evolution.game.impl.RepeatedGameImpl;
import ipd.evolution.impl.CrossoverImpl;
import ipd.evolution.impl.MutationFromPaper;
import ipd.model.game.Action;
import ipd.model.game.Player;
import ipd.model.strategy.State;
import ipd.visualization.GraphPlotter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

public class EvolutionManager {

	private static List<Player> players;
	private static RepeatedGameImpl game = new RepeatedGameImpl();
	private static Crossover crossover = new CrossoverImpl();
	private static Mutation mutation = new MutationFromPaper();
	public static double ALFA = 0.2;
	public static double DELTA = 0.3;
	private static Player theBestPlayer;

	public static void main(String[] args) {

		if (args.length < 3) {
			System.out.println("Too few arguments. Parameters: alfa_probability delta_probability iterations");
			return;
		}

		setUp(args);

		int iterations = Integer.parseInt(args[2]);
		// List<String> lines = Lists.newLinkedList();
		for (int i = 0; i < iterations; i++) {
			step();
			getTheBestPlayer();
			// lines.add(printAveragePayOffSum(i));
		}
		printPercentageOfCooperations();
		// saveAveragePayoff(lines);
		saveTheBestStrategy();

	}

	private static void saveAveragePayoff(List<String> lines) {
		Path path = Paths.get("fitness.txt");
		try {
			Files.write(path, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setUp(String[] args) {
		ALFA = Double.parseDouble(args[0]);
		DELTA = Double.parseDouble(args[1]);
		players = ExemplaryPopulationFactory.create(1000);
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

	private static void printPercentageOfCooperations() {
		long cooperationSum = 0;
		long statesNumber = 0;
		for (Player p : players) {
			List<State> states = p.getStrategy().getStates();
			statesNumber += states.size();
			cooperationSum += states.stream().filter(s -> s.getAction().equals(Action.COOPERATION)).count();
		}
		System.out.println(cooperationSum * 100.0 / statesNumber);
	}

	private static String printAveragePayOffSum(int i) {

		double sum = players.stream().mapToDouble(p -> p.getPayOff()).sum();
		return i + " " + sum / 200;
	}

	private static void saveTheBestStrategy() {

		GraphPlotter.plot(theBestPlayer.getStrategy(), getCurrentDate());
	}

	private static void getTheBestPlayer() {
		theBestPlayer = players.get(0);
		for (Player p : players)
			if (p.getPayOff() > theBestPlayer.getPayOff())
				theBestPlayer = p;
	}

	private static String getCurrentDate() {
		String date = new Date().toString();
		date = date.replaceAll(":", "-");
		date = date.trim();
		return date;
	}

}
