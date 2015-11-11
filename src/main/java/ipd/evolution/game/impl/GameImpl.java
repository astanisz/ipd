package ipd.evolution.game.impl;

import ipd.evolution.game.Game;
import ipd.model.game.Action;
import ipd.model.game.Player;

import org.apache.commons.lang3.tuple.Pair;

public class GameImpl implements Game {

	// Values from article
	public static final int R = 2;
	public static final int S = 0;
	public static final int T = 3;
	public static final int P = 1;

	public void play(Pair<Player, Player> players, Pair<Action, Action> actions) {
		Player player1 = players.getLeft();
		Player player2 = players.getRight();

		Action action1 = actions.getLeft();
		Action action2 = actions.getRight();

		setPayoffs(player1, player2, action1, action2);
	}

	private void setPayoffs(Player player1, Player player2, Action action1, Action action2) {
		if (action1.equals(Action.COOPERATION)) {
			if (action2.equals(Action.COOPERATION)) {
				player1.addPayOff(R);
				player2.addPayOff(R);
			} else if (action2.equals(Action.DEFECTION)) {
				player1.addPayOff(T);
				player2.addPayOff(S);
			}
		} else {
			if (action2.equals(Action.COOPERATION)) {
				player1.addPayOff(S);
				player2.addPayOff(T);
			} else if (action2.equals(Action.DEFECTION)) {
				player1.addPayOff(P);
				player2.addPayOff(P);
			}
		}
	}
}
