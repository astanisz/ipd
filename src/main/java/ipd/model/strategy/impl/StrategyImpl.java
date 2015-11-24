package ipd.model.strategy.impl;

import ipd.model.game.Action;
import ipd.model.strategy.State;
import ipd.model.strategy.Strategy;
import ipd.utils.ProbabilityUtils;

import java.util.LinkedList;
import java.util.List;

public class StrategyImpl implements Strategy {
	private List<State> states = new LinkedList<>();
	private State current;

	public State getCurrentState() {
		return current;
	}

	public void goToNextState(Action action) {
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

	/**
	 * Removes state from automata. If removed state is current state, moves current to next-if-cooperation or to
	 * next-if-defection with equal probability. If next-ifs don't exists (it is the last state in a strategy) - goes to
	 * the next (or previous) state on the states list.
	 */
	public void removeState(State state) {
		if (state.equals(current)) {
			moveCurrent(state);
		}
		states.remove(state);
	}

	@Override
	public void resetHistory() {
		if (states.isEmpty())
			throw new IllegalStateException("Cannot reset strategy history if list of states is empty");
		current = states.get(0);
	}

	@Override
	public Strategy getCopy() {
		Strategy copy = new StrategyImpl();
		states.forEach(s -> copy.addState(s.getCopy()));
		for (int i = 0; i < states.size(); i++) {
			copyLinks(copy.getStates().get(i), states.get(i), copy.getStates());
		}
		return copy;
	}

	private void copyLinks(State stateFromCopy, State originalState, List<State> copyStates) {
		stateFromCopy.setNextIfCooperation(copyStates.get(states.indexOf(originalState.getNextIfCooperation())));
		stateFromCopy.setNextIfDefection(copyStates.get(states.indexOf(originalState.getNextIfDefection())));
	}

	private void moveCurrent(State state) {
		if (ProbabilityUtils.simulateProbability(0.5))
			current = state.getNextIfCooperation();
		else
			current = state.getNextIfDefection();
		if (current == null) {
			if (states.size() > 1) {
				int index = states.indexOf(state);
				// there is always at least 1 state in a strategy
				if (index + 1 <= states.size() - 1)
					current = states.get(index + 1);
				else
					current = states.get(index - 1);
			} else
				current = null;
		}
	}
}
