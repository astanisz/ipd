package ipd.model.strategy.impl;

import ipd.model.game.Action;
import ipd.model.strategy.State;
import ipd.model.strategy.Strategy;

import java.util.LinkedList;
import java.util.List;

public class StrategyImpl implements Strategy {
	private List<State> states = new LinkedList<>();
	private State current;

	public State getCurrentState() {
		return current;
	}

	public void goToNextState(Action action) {
		// FIXME: current may be null
		if (action.equals(Action.COOPERATION))
			current = current.getNextIfCooperation();
		else
			current = current.getNextIfDefection();
	}

	public List<State> getStates() {
		return states;
	}

	public void addState(State state) {
		if (states.isEmpty())
			current = state;
		this.states.add(state);
	}

	public void removeState(State state) {
		states.remove(state);
	}

	@Override
	public void resetHistory() {
		if (states.isEmpty())
			throw new IllegalStateException("Cannot reset strategy history if list of states is empty");
		current = states.get(0);
	}

}
