package ipd.evolution.game.impl;

import ipd.evolution.game.Game;
import ipd.evolution.game.RepeatedGame;
import ipd.evolution.manager.EvolutionManager;
import ipd.model.game.Action;
import ipd.model.game.Player;
import ipd.utils.ProbabilityUtils;

import org.apache.commons.lang3.tuple.Pair;

public class RepeatedGameImpl implements RepeatedGame {

	public void play(Pair<Player, Player> players) {
		Player player1 = players.getLeft();
		Player player2 = players.getRight();
		player1.setPayoff(0);
		player2.setPayoff(0);
		double discountFactor = 1.0; // delta ** i

		do {
			// first round is always played
			playRound(player1, player2, discountFactor);
			// decreasing discountFactor so that later rounds are becoming less important for total payOff
			discountFactor *= EvolutionManager.DELTA;
			// next round is played with nextRoundProbability
		} while (ProbabilityUtils.simulateProbability(EvolutionManager.DELTA));

		normalizePayoff(player1);
		normalizePayoff(player2);
	}

	/**
	 * For repeated game payoffs are normalized.
	 */
	private void normalizePayoff(Player player) {
		player.setPayoff(player.getPayOff() * (1 - EvolutionManager.DELTA));
	}

	private void playRound(Player player1, Player player2, double discountFactor) {
		Action action1 = player1.getStrategy().getCurrentState().getAction();
		Action action2 = player2.getStrategy().getCurrentState().getAction();

		Game game = new GameImpl(discountFactor);
		game.play(Pair.of(player1, player2), Pair.of(action1, action2));

		player1.getStrategy().goToNextState(action2);
		player2.getStrategy().goToNextState(action1);
	}
}
