package IA.Energia;

// Generates a ProbEnergiaBoard from a configuration
class ProbEnergiaBoardGenerator {
    protected ProbEnergiaBoard problem;

    public ProbEnergiaBoard getProblem() { return problem; }

    public void setStations(int[] cent, int seed) throws Exception {
        Centrales ss = new Centrales(cent, seed);
    }
    public void setCustomers(int ns, double[] proc, double propg, int seed) throws Exception {
        Clientes cs = new Clientes(ns, proc, propg, seed);
        problem.setCustomers(cs);
    }
    // implement heuristic
    public void setInitialState(String method) throws Exception {
        // add else if as needed
        if (method.equals("")) defaultInitialState();
        else throw new Exception("Initial state method " + method + " doesn't exists");
    }

    private void defaultInitialState() {
        // for illustrative purposes only
    }
}
