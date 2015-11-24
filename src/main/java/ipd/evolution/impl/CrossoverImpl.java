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
		for (int i = 1; i < parentPopulation.size(); i += 2) {
			if (ProbabilityUtils.simulateProbability(EvolutionManager.ALFA))
				players.set(i, getCopyOf(parentPopulation.get(i - 1)));
		}
	}

	private Player getCopyOf(Player player) {
		Player copy = new Player();
		copy.setStrategy(player.getStrategyCopy());
		return copy;
	}
}
