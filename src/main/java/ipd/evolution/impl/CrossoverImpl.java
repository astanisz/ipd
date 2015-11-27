package ipd.evolution.impl;

import ipd.evolution.Crossover;
import ipd.evolution.manager.EvolutionManager;
import ipd.model.game.Player;
import ipd.utils.ProbabilityUtils;

import java.util.List;

import com.google.common.collect.Lists;

public class CrossoverImpl implements Crossover {

	public void cross(List<Player> players) {
		List<Player> parentPopulation = Lists.newLinkedList(players);

		// Computing sum of payoffs in parent population
		double payoffsSum = 0.0;
		for (Player player: parentPopulation) {
			payoffsSum += player.getPayOff();
		}

		// For every parent computing the probability that this parent is chosen
		for (Player player: parentPopulation) {
			player.setProbability(player.getPayOff() / payoffsSum);
		}

		// Performing crossover
		for (int i = 1; i < parentPopulation.size(); i += 2) {
			Player parent = pickParentProportionalToPayoff(parentPopulation);
			players.set(i - 1, getCopyOf(parent));
			if (ProbabilityUtils.simulateProbability(EvolutionManager.ALFA)) {
				players.set(i, getCopyOf(parent));
			} else {
				Player alternativeParent = pickParentProportionalToPayoff(parentPopulation);
				players.set(i, getCopyOf(alternativeParent));
			}
		}
	}

	private Player getCopyOf(Player player) {
		Player copy = new Player();
		copy.setStrategy(player.getStrategyCopy());
		return copy;
	}

	// Obtaining parent proportionally to payoff which means
	// that parents with higher payoffs are more likely to be used
	private Player pickParentProportionalToPayoff(List<Player> players) {
		double p = Math.random();
		double cumulativeProbability = 0.0;
		for (Player player : players) {
			cumulativeProbability += player.getProbability();
			if (p <= cumulativeProbability) {
				return player;
			}
		}
		return null;
	}
}
