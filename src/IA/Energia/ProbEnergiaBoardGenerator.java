package IA.Energia;

import java.util.Random;

// Generates a ProbEnergiaBoard from a configuration
class ProbEnergiaBoardGenerator {
    protected ProbEnergiaBoard problem;

    public ProbEnergiaBoardGenerator() {
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
    public void setInitialState(String method) throws Exception {
        // add else if as needed
        problem.initHeuristicValues();
        if (method.equals("random")) randomInitState();
        else throw new Exception("Initial state method " + method + " doesn't exists");
    }

    private void randomInitState() {
        // random greedy assignation
        Random rnd = new Random();
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        for (int i = 0; i < nc; ++i) {
            if (problem.isGuaranteedCustomer(i)) {
                // assign randomly
                boolean isAssigned = false;
                do { // be careful with infinite loop
                    int station = rnd.nextInt(ns);
                    if (problem.canAssignCustomer2Station(i, station)) {
                        problem.assignCustomer2Station(i, station);
                        isAssigned = true;
                    }
                } while (!isAssigned);
            }
        }
    }
}
