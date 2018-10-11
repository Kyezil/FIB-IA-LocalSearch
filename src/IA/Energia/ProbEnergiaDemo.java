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
            PEgen.greedyMaxCapacityInitState(0.95);
            //PEgen.randomInitState(RANDOM_SEED);
            //PEgen.randomMaxCapacityInitState(RANDOM_SEED, 1.0);
        } catch(Exception e) {
            e.printStackTrace();
        }
        ProbEnergiaBoard problem = PEgen.getProblem();
        System.out.println(problem.toString());
        // timer
        long time_0 = System.currentTimeMillis();
        EnergiaHillClimbingSearch(problem);
        long dtime = System.currentTimeMillis() - time_0;
        System.out.println("\nTime elapsed: " + dtime + " ms");
    }

    private static void EnergiaHillClimbingSearch(ProbEnergiaBoard board) {
        System.out.println("\nEnergia HillClimbing  -->");
        try {
            Problem problem = new Problem(board,
                    new ProbEnergiaSuccessorFunction(),
                    new ProbEnergiaGoalTest(),
                    new ProbEnergiaHeuristicMix());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);


            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            System.out.println("Final state =>");
            ProbEnergiaBoard final_board = (ProbEnergiaBoard) search.getGoalState();
            System.out.println(final_board);
            // num of customers served
            int customers_served = 0;
            for (int i = 0; i < final_board.getNCustomers(); ++i) {
                if (final_board.isCustomerAllocated(i)) customers_served += 1;
            }
            System.out.println("Number of customers served = " + customers_served);

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
