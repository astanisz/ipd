package ipd.model.strategy;

import ipd.model.game.Action;

import java.util.List;

public interface Strategy {

	List<State> getStates();

	void addState(State state);

	void removeState(State state);

	void resetHistory();

	void goToNextState(Action action);

	State getCurrentState();
}
