package ipd.model.strategy.impl;

import ipd.model.game.Action;
import ipd.model.strategy.State;

public class Defection extends State {

	@Override
	public Action getAction() {
		return Action.DEFECTION;
	}

	@Override
	public Color getColor() {
		return Color.RED;
	}

	@Override
	public State getCopy() {
		return new Defection();
	}

}
