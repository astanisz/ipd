package ipd.model.strategy.impl;

import ipd.model.game.Action;
import ipd.model.strategy.State;

public class Defection extends State {

	@Override
	public Action getAction() {
		return Action.DEFECTION;
	}

}