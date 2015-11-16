package ipd.evolution.impl;

import ipd.evolution.Crossover;
import ipd.model.game.Player;
import ipd.utils.ProbabilityUtils;

import java.util.List;

import com.google.common.collect.Lists;

public class CrossoverImpl implements Crossover {
	private static final double copyProbability = 0.3;

	public void cross(List<Player> players) {
		List<Player> parentPopulation = Lists.newLinkedList(players);
		for (int i = 1; i < parentPopulation.size(); i += 2) {
			if (ProbabilityUtils.simulateProbability(copyProbability))
				players.set(i, parentPopulation.get(i - 1));
		}
	}
}
