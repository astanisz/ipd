package ipd.evolution.impl;

import ipd.evolution.Mutation;
import ipd.model.game.Action;
import ipd.model.game.Player;
import ipd.model.strategy.State;
import ipd.model.strategy.impl.Cooperation;
import ipd.model.strategy.impl.Defection;
import ipd.utils.ProbabilityUtils;

import java.util.List;
import java.util.Random;

public class MutationFromPaper implements Mutation {

	private static final double mutationProbability = 0.001;
	private static final double halfChance = 0.5;
	private Random rand;

	public MutationFromPaper() {
		rand = new Random();
	}

	public void mutate(List<Player> players) {
		players.stream()
				.filter(player -> ProbabilityUtils.simulateProbability(mutationProbability))
				.forEach(this::mutatePlayer);
	}

	private void mutatePlayer(Player player) {
		if (player.getStrategy().getStates().size() == 1)
			addState(player);
		else {
			if (ProbabilityUtils.simulateProbability(halfChance))
				addState(player);
			else
				deleteState(player);
		}
	}

	// Adding state:
	// 1. Choosing state which will be mutated and connected with newly created state
	// 2. State is created and added to the list of available states
	// 3. Newly created state is connected with mutatedState by randomly chosen transition
	// 4. Setting transitions for newState
	private void addState(Player player) {
		State mutatedState = pickStateAtRandom(player);

		State newState = createStateAtRandom();
		player.getStrategy().addState(newState);

		makeTransition(pickTransitionAtRandom(), mutatedState, newState);

		createTransitions(player, newState);
	}

	// Deleting state:
	// 0. Makes no sense to delete state when there is at most one state :)
	// 1. Choosing state we want to delete
	// 2. Pointing all transitions leading to deletedState to randomly selected state
	// 3. Removing state from list of states
	private void deleteState(Player player) {
		if (player.getStrategy().getStates().size() <= 1)
			return;

		State deletedState = pickStateAtRandom(player);

		for (State state : player.getStrategy().getStates()) {
			State destinationState = null;
			do {
				destinationState = pickStateAtRandom(player);
			} while (destinationState == deletedState);

			state.relinkTransition(deletedState, destinationState);
		}

		player.getStrategy().removeState(deletedState);
	}

	// // Additional private convenience methods....

	// Establishing connection: fromState --> transition --> toState
	private void makeTransition(Action transition, State fromState, State toState) {
		if (transition == Action.COOPERATION) {
			fromState.setNextIfCooperation(toState);
		} else {
			fromState.setNextIfDefection(toState);
		}
	}

	// Randomly initializes transitions for player and state
	// This is used when adding new state
	private void createTransitions(Player player, State state) {
		State goToOnCooperation = pickStateAtRandom(player);
		State goToOnDefection = pickStateAtRandom(player);
		state.setNextIfCooperation(goToOnCooperation);
		state.setNextIfDefection(goToOnDefection);
	}

	// Picks transition - COOPERATION is picked with cooperationTransitionPickedProbability probability
	private Action pickTransitionAtRandom() {
		if (ProbabilityUtils.simulateProbability(halfChance)) {
			return Action.COOPERATION;
		} else {
			return Action.DEFECTION;
		}
	}

	// Returns state - uniformly at random
	private State pickStateAtRandom(Player player) {
		List<State> states = player.getStrategy().getStates();
		return states.get(rand.nextInt(states.size()));
	}

	// Generate state randomly - Cooperation state is generated with cooperationStatePickedProbability probability
	private State createStateAtRandom() {
		if (ProbabilityUtils.simulateProbability(halfChance)) {
			return new Cooperation();
		} else {
			return new Defection();
		}
	}

}
