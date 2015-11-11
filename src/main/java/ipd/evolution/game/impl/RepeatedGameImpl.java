package ipd.evolution.game.impl;

import ipd.evolution.game.Game;
import ipd.evolution.game.RepeatedGame;
import ipd.model.game.Action;
import ipd.model.game.Player;
import ipd.utils.ProbabilityUtils;

import org.apache.commons.lang3.tuple.Pair;

public class RepeatedGameImpl implements RepeatedGame {
	private static final double nextRoundProbability = 0.9;

	public void play(Pair<Player, Player> players) {
		Player player1 = players.getLeft();
		Player player2 = players.getRight();

		do {
			// first round is always played
			playRound(player1, player2);
			// next round is played with nextRoundProbability
		} while (ProbabilityUtils.simulateProbability(nextRoundProbability));

		normalizePayoff(player1);
		normalizePayoff(player2);
	}

	/**
	 * For repeated game payoffs are normalized.
	 */
	private void normalizePayoff(Player player) {
		player.setPayoff(player.getPayOff() * (1 - nextRoundProbability));
	}

	private void playRound(Player player1, Player player2) {
		Action action1 = player1.getStrategy().getCurrentState().getAction();
		Action action2 = player2.getStrategy().getCurrentState().getAction();

		Game game = new GameImpl();
		game.play(Pair.of(player1, player2), Pair.of(action1, action2));

		player1.getStrategy().goToNextState(action2);
		player2.getStrategy().goToNextState(action1);
	}
}
