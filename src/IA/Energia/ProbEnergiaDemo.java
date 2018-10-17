package IA.Energia;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.ArrayList;
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
            //PEgen.greedyMaxCapacityInitState(0.95);
            PEgen.randomInitState(RANDOM_SEED);
            //PEgen.randomMaxCapacityInitState(RANDOM_SEED, 1.0);
        } catch(Exception e) {
            e.printStackTrace();
        }
        ProbEnergiaBoard problem = PEgen.getProblem();
        System.out.println(problem.toString());

        System.out.println("Starting experiment 1");
        List times = new ArrayList();
        for (int i = 0; i < 20; ++i) {
            long time_0 = System.currentTimeMillis();
            EnergiaHillClimbingSearch(problem);
            //EnergiaSimulatedAnnealingSearch(problem);
            long dtime = System.currentTimeMillis() - time_0;
            times.add(dtime);
        }
        System.out.println("Times elapsed: " + times + " ms");
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


            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            System.out.println("Final state =>");
            ProbEnergiaBoard final_board = (ProbEnergiaBoard) search.getGoalState();
            displayFinalState(final_board);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void EnergiaSimulatedAnnealingSearch(ProbEnergiaBoard board) {
        System.out.println("\nSimulated Annealing  -->");
        try {
            Problem problem = new Problem(board,
                    new ProbEnergiaSuccessorFunctionSA(),
                    new ProbEnergiaGoalTest(),
                    new ProbEnergiaHeuristicBenefit());
            SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(800000, 200, 5, 0.001);
            //search.traceOn();
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            //printActions(agent.getActions()); // not accessible for simulated annealing
            printInstrumentation(agent.getInstrumentation());

            System.out.println("Final state =>");
            ProbEnergiaBoard final_board = (ProbEnergiaBoard) search.getGoalState();
            displayFinalState(final_board);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayFinalState(ProbEnergiaBoard final_board) {
        System.out.println(final_board);
        // num of customers served
        int customers_served = 0;
        for (int i = 0; i < final_board.getNCustomers(); ++i) {
            if (final_board.isCustomerAllocated(i)) customers_served += 1;
        }
        System.out.println("Number of customers served = " + customers_served);
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
