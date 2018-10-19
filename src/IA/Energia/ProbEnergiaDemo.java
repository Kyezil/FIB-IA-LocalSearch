package IA.Energia;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;

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
            //PEgen.closestInitState(RANDOM_SEED);
        } catch(Exception e) {
            e.printStackTrace();
        }
        ProbEnergiaBoard problem = PEgen.getProblem();
        System.out.println(problem.toString());

        EnergiaHillClimbingSearch(problem);
        //EnergiaSimulatedAnnealingSearch(problem);
    }

    private static void EnergiaHillClimbingSearch(ProbEnergiaBoard board) {
        System.out.println("\nEnergia HillClimbing  -->");
        try {
            List times = new ArrayList();
            int reps = 20;
            for (int i = 0; i < reps; ++i) {
                System.out.println(i+1 + "/" + reps);
                Problem problem = new Problem(board,
                        new ProbEnergiaSuccessorFunction(),
                        new ProbEnergiaGoalTest(),
                        new ProbEnergiaHeuristicMix(0.45));
                Search search = new HillClimbingSearch();
                // timer
                long time_0 = System.currentTimeMillis();
                SearchAgent agent = new SearchAgent(problem, search);
                long dtime = System.currentTimeMillis() - time_0;
                times.add(dtime);
                if (i == 0) {
                    System.out.println("### ACTIONS ###");
                    //printActions(agent.getActions());
                    System.out.println("### FINAL STATE ###");
                    ProbEnergiaBoard final_board = (ProbEnergiaBoard) search.getGoalState();
                    System.out.println(final_board);
                    System.out.println("### EXPERIMENT INFO ###");
                    System.out.println("benefici: " + final_board.getBenefit());
                    displayCustomersServed(final_board);
                    printActionsCount(agent.getActions());
                    printInstrumentation(agent.getInstrumentation());
                }
            }
            System.out.println("Times elapsed (ms): " + times);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
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
    */

    private static void displayCustomersServed(ProbEnergiaBoard final_board) {
        // num of customers served
        int customers_served = 0;
        int guaranteed_served = 0;
        int total_guaranteed = 0;
        int n = final_board.getNCustomers();
        for (int i = 0; i < n; ++i) {
            if (final_board.isGuaranteedCustomer(i)) {
                total_guaranteed++;
                if (final_board.isCustomerAllocated(i)) {
                    guaranteed_served++;
                    customers_served++;
                }
            } else if (final_board.isCustomerAllocated(i)) {
                customers_served++;
            }
        }
        // Customers served: #served (#guaranteed) / #total

        System.out.println("Customers served: " + customers_served + "/" + n);
        System.out.println("      guaranteed: " + guaranteed_served + "/" + total_guaranteed);
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

    private static void printActionsCount(List actions) {
        Map<String, Integer> count = new HashMap<String, Integer>();

        for (int i = 0; i < actions.size(); ++i) {
            String action = (String) actions.get(i);
            // get first word of string
            String action_name = action.split(" ",2)[0];
            count.merge(action_name, 1, Integer::sum); // set to 1 or add 1
        }

        System.out.println("Actions count: " + count);
    }
}
