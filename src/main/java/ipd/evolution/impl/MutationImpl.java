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

// Implementation based on: https://www.youtube.com/watch?v=mPUrB8aFFuI
public class MutationImpl implements Mutation {

	private static final double mutationProbability = 0.2;
	private static final double cooperationTransitionPickedProbability = 0.5;
	private static final double cooperationStatePickedProbability = 0.5;

	private static final double addStateMutationProbability = 0.6;
	private static final double changeTransitionMutationProbability = 0.7;
	private static final double changeLabelMutationProbability = 0.5;
	private static final double deleteStateMutationProbability = 0.3;

	private Random rand;

	public MutationImpl() {
		rand = new Random();
	}

	public void mutate(List<Player> players) {
		players.stream().filter(player -> ProbabilityUtils.simulateProbability(mutationProbability)).forEach(this::mutatePlayer);
	}

	private void mutatePlayer(Player player) {
		if (ProbabilityUtils.simulateProbability(addStateMutationProbability)) addState(player);
		if (ProbabilityUtils.simulateProbability(changeTransitionMutationProbability)) changeTransition(player);
		if (ProbabilityUtils.simulateProbability(changeLabelMutationProbability)) changeLabel(player);
		if (ProbabilityUtils.simulateProbability(deleteStateMutationProbability)) deleteState(player);
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

	// Changing transition:
	// 1. Choosing state to be changed
	// 2. Choosing new destination state for the transition (it could be possibly the same one ?)
	// 3. Changing selected transition from mutatedState to destinationState
	private void changeTransition(Player player) {
		State mutatedState = pickStateAtRandom(player);

		State destinationState = pickStateAtRandom(player);

		makeTransition(pickTransitionAtRandom(), mutatedState, destinationState);
	}

	// Changing label:
	// 1. Choosing state for which we need to change the label
	// 2. Creating new state with opposite label (action)
	// 3. Pointing all transitions leading to mutatedState to newState
	// 4. Removing mutatedState
	// 5. Adding newState to states
	private void changeLabel(Player player) {
		// NOTE: This mutatedState will be removed
		State mutatedState = pickStateAtRandom(player);

		State newState = mutatedState.getAction() == Action.COOPERATION ? new Defection() : new Cooperation();
		newState.setNextIfCooperation(mutatedState.getNextIfCooperation());
		newState.setNextIfDefection(mutatedState.getNextIfDefection());

		for (State state: player.getStrategy().getStates()) {
			relinkTransition(state, mutatedState, newState);
		}

		player.getStrategy().removeState(mutatedState);

		player.getStrategy().addState(newState);
	}

	// Deleting state:
	// 0. Makes no sense to delete state when there is at most one state :)
	// 1. Choosing state we want to delete
	// 2. Pointing all transitions leading to deletedState to state from which their originated !
	// 3. Removing state from list of states
	private void deleteState(Player player) {
		if (player.getStrategy().getStates().size() <= 1) return;

		State deletedState = pickStateAtRandom(player);

		for (State state: player.getStrategy().getStates()) {
			relinkTransition(state, deletedState, state);
		}

		player.getStrategy().removeState(deletedState);
	}

	//// Additional private convenience methods....

	// All links/transitions leading to fromState are moved to toState
	// currentState is currently visited state
	private void relinkTransition(State currentState, State fromState, State toState) {
			if (currentState.getNextIfCooperation() == fromState) {
				currentState.setNextIfCooperation(toState);
			}
			if (currentState.getNextIfDefection() == fromState) {
				currentState.setNextIfDefection(toState);
			}
	}

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
		if (ProbabilityUtils.simulateProbability(cooperationTransitionPickedProbability)) {
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
		if (ProbabilityUtils.simulateProbability(cooperationStatePickedProbability)) {
			return new Cooperation();
		} else {
			return new Defection();
		}
	}
}
