package ipd.utils;

public class ProbabilityUtils {
	private ProbabilityUtils() {
		// utility class
	}

	public static boolean simulateProbability(double probability) {
		// FIXME: 1 is exclusive
		return Math.random() < probability;
	}
}
