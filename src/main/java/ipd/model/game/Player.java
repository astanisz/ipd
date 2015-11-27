package ipd.model.game;

import ipd.model.strategy.Strategy;

public class Player {

	private double payOff = 0.0;

	private double probability = 0.0;

	private Strategy strategy;

	public double getPayOff() {
		return payOff;
	}

	public void addPayOff(double payOff) {
		this.payOff += payOff;
	}

	public double getProbability() { return probability; }

	public void setProbability(double probability) { this.probability = probability; }

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public void setPayoff(double payOff) {
		this.payOff = payOff;
	}

	public Strategy getStrategyCopy() {
		return strategy.getCopy();
	}
}
