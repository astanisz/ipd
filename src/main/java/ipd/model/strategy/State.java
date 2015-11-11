package ipd.model.strategy;

import ipd.model.game.Action;

public abstract class State {
	private State nextIfCooperation;
	private State nextIfDefection;

	public abstract Action getAction();

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
