package IA.Energia;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class ProbEnergiaDemo {
    final static int RANDOM_SEED = 1234;

    public static void main(String[] args) throws Exception {
        // EXPERIMENT 1
        ProbEnergiaBoardGenerator PEgen = new ProbEnergiaBoardGenerator();
        try {
            PEgen.setStations(new int[]{5, 10, 25}, RANDOM_SEED);
            PEgen.setCustomers(1000, new double[]{0.25, 0.3, 0.45}, 0.75, RANDOM_SEED);
            PEgen.setInitialState("random");
        } catch(Exception e) {
            e.printStackTrace();
        }
        ProbEnergiaBoard problem = PEgen.getProblem();
        System.out.println(problem.toString());
    //    EnergiaHillClimbingSearch(problem);
    }

    private static void EnergiaHillClimbingSearch(ProbEnergiaBoard board) {
        System.out.println("\nEnergia HillClimbing  -->");
        try {
            Problem problem = new Problem(board,
                    new ProbEnergiaSuccessorFunction(),
                    new ProbEnergiaGoalTest(),
                    new ProbEnergiaHeuristicBenefit());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}
