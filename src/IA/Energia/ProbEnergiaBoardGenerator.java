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
    // implement heuristic
    public void setInitialState(String method, int seed) throws Exception {
        // add else if as needed
        if (method.equals("random")) randomInitState(seed);
        else throw new Exception("Initial state method " + method + " doesn't exists");
    }

    private void randomInitState(int seed) throws Exception {
        // random greedy assignation
        Random rnd = new Random();
        rnd.setSeed(seed);
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        for (int i = 0; i < nc; ++i) {
            if (problem.isGuaranteedCustomer(i)) {
                // assign randomly
                boolean isAssigned = false;
                do { // be careful with infinite loop
                    int station = rnd.nextInt(ns);
                    if (problem.canAllocateCustomer2Station(i, station)) {
                        problem.allocateCustomer2Station(i, station);
                        isAssigned = true;
                    }
                } while (!isAssigned);
            }
        }
    }
}
