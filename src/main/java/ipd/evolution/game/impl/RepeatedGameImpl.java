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

		do {
			// first round is always played
			playRound(player1, player2);
			// next round is played with nextRoundProbability
		} while (ProbabilityUtils.simulateProbability(EvolutionManager.DELTA));

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
