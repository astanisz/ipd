package ipd.evolution.manager;

import ipd.model.game.Player;
import ipd.model.strategy.State;
import ipd.model.strategy.Strategy;
import ipd.model.strategy.impl.Cooperation;
import ipd.model.strategy.impl.Defection;
import ipd.model.strategy.impl.StrategyImpl;

import java.util.List;

import com.google.common.collect.Lists;

public class ExeplaryPopulationFactory {

	public static List<Player> create(int size) {
		List<Player> players = Lists.newLinkedList();
		for (int i = 0; i < size; i++) {
			Player p = new Player();
			Strategy strategy = createStrategy(i);
			p.setStrategy(strategy);
			players.add(p);
		}

		return players;
	}

	private static Strategy createStrategy(int i) {
		Strategy strategy = new StrategyImpl();
		State state = null;
		if (i % 2 == 0)
			state = new Defection();
		else
			state = new Cooperation();
		state.setNextIfCooperation(state);
		state.setNextIfDefection(state);
		strategy.addState(state);
		return strategy;
	}

}
