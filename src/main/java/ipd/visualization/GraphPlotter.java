package ipd.visualization;

import ipd.model.strategy.State;
import ipd.model.strategy.Strategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.collect.Lists;

public class GraphPlotter {
	private static String graphsPath = "graphs/strategy";

	public static void plot(Strategy strategy, String nr) {

		try {
			Path workingDirectory = Paths.get("").toAbsolutePath();
			Path path = saveStrategyToFile(nr, strategy);
			ProcessBuilder p = new ProcessBuilder("dot", "-Tpng", workingDirectory + "/" + path);
			p.redirectOutput(new File(graphsPath + nr + ".png"));
			p.redirectErrorStream(true);
			Process process = p.start();
			System.out.println("Plotting " + workingDirectory + "/" + path);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Path saveStrategyToFile(String nr, Strategy strategy) throws IOException {
		List<String> lines = Lists.newLinkedList();
		lines.add(" digraph G{");
		List<State> states = strategy.getStates();
		for (State s : states) {
			lines.add(states.indexOf(s) + "[color=" + s.getColor() + "];");
			lines.add(states.indexOf(s) + "->" + states.indexOf(s.getNextIfCooperation()) + "[color=blue];");
			lines.add(states.indexOf(s) + "->" + states.indexOf(s.getNextIfDefection()) + "[color=red];");
		}
		lines.add(" }");
		Path path = Paths.get(graphsPath + nr + ".dot");
		path = Files.write(path, lines);
		return path;
	}
}
