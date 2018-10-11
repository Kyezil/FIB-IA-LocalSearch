package IA.Energia;

import java.util.Random;

// Generates a ProbEnergiaBoard from a configuration
class ProbEnergiaBoardGenerator {
    protected ProbEnergiaBoard problem;

    public ProbEnergiaBoardGenerator() throws Exception {
        this.problem = new ProbEnergiaBoard();
    }

    public ProbEnergiaBoard getProblem() { return problem; }


    public void setStations(int[] cent, int seed) throws Exception {
        Centrales ss = new Centrales(cent, seed);
        problem.setStations(ss);
    }
    public void setCustomers(int ns, double[] proc, double propg, int seed) throws Exception {
        Clientes cs = new Clientes(ns, proc, propg, seed);
        problem.setCustomers(cs);
    }

    public void randomInitState(int seed) throws Exception {
        // random greedy assignation
        randomMaxCapacityInitState(seed, 1.0);
    }

    public void randomMaxCapacityInitState(int seed, double max_c) throws Exception {
        // random greedy assignation using at most max_c proportion of station capacity
        // max_c between 0 and 1
        // kinda stupid code, can be improved a lot
        Random rnd = new Random();
        rnd.setSeed(seed);
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        for (int i = 0; i < nc; ++i) {
            if (problem.isGuaranteedCustomer(i)) {
                // assign randomly
                boolean isAssigned = false;
                do { // be careful with infinite loop
                    int s_id = rnd.nextInt(ns);
                    if (problem.canAllocateCustomer2Station(i, s_id, max_c)) {
                        problem.allocateCustomer2Station(i, s_id);
                        isAssigned = true;
                    }
                } while (!isAssigned);
            }
        }
    }

    public void greedyInitState() throws Exception {
        greedyMaxCapacityInitState(1.0);
    }

    public void greedyMaxCapacityInitState(double max_c) throws Exception {
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        int current_central = 0;
        for(int i=0; i < nc; ++i){
            if(problem.isGuaranteedCustomer(i)){
                boolean isAssigned = false;
                do {
                    if(problem.canAllocateCustomer2Station(i, current_central, max_c)){
                        problem.allocateCustomer2Station(i, current_central);
                        isAssigned = true;
                    }
                    else current_central = (current_central + 1) % ns;
                } while (!isAssigned);
            }
        }
    }
}
