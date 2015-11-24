package ipd.model.strategy;

import ipd.model.game.Action;

public abstract class State {
	private State nextIfCooperation;
	private State nextIfDefection;

	public enum Color {
		RED, BLUE
	}

	public abstract Action getAction();

	public abstract State getCopy();

	public abstract Color getColor();

	// All links/transitions leading to fromState are moved to toState
	// currentState is currently visited state
	public void relinkTransition(State fromState, State toState) {
		if (getNextIfCooperation() == fromState) {
			setNextIfCooperation(toState);
		}
		if (getNextIfDefection() == fromState) {
			setNextIfDefection(toState);
		}
	}

	public State getNextIfCooperation() {
		return nextIfCooperation;
	}

	public void setNextIfCooperation(State nextIfCooperation) {
		this.nextIfCooperation = nextIfCooperation;
	}

	public State getNextIfDefection() {
		return nextIfDefection;
	}

	public void setNextIfDefection(State nextIfDefection) {
		this.nextIfDefection = nextIfDefection;
	}

}
