package ipd.evolution;

import ipd.model.game.Player;

import java.util.List;

public interface Crossover {
	void cross(List<Player> players);
}
