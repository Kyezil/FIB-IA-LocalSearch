package IA.Energia;




import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public void realGreedyInitState() throws Exception {
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        List<Integer> consumption = new ArrayList<>();
        for(int i = 0; i < nc; ++i){
            consumption.add(i);
        }
        consumption.sort((m1, m2) -> {

            if(problem.getCustomerConsumption(m1) == problem.getCustomerConsumption(m2)){

                return 0;

            }
            if(problem.getCustomerConsumption(m1) < problem.getCustomerConsumption(m2)){

                return 1;

            }
            return -1;

        });
        for(int k=0; k < nc; ++k){
            int i = consumption.get(k);
            boolean isAssigned = false;
            for(int j = 0; !isAssigned && j < ns; ++j){
                if(problem.canAllocateCustomer2Station(i, j)){
                    problem.allocateCustomer2Station(i, j);
                    isAssigned = true;
                }
            }
        }
    }


    public void unguaranteedRandomInitState(int seed, double prob) throws Exception { //prob representa la probabilitat d'assignar un customer
        unguaranteedRandomMaxCapacityInitState(seed, prob, 1.0);
    }

    public void unguaranteedRandomMaxCapacityInitState(int seed, double prob, double max_c) throws Exception {
        // random greedy assignation using at most max_c proportion of station capacity
        // max_c between 0 and 1
        // kinda stupid code, can be improved a lot
        Random rnd = new Random();
        rnd.setSeed(seed);
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        for (int i = 0; i < nc; ++i) {
            if (rnd.nextDouble() <= prob) { //assumeixo que en puc treure aqui doubles i mes endavant ints, sino peta
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

    //igual que greedyMaxCapacityInitState però assignant a tots els clients, no nomes els garantits.   
    public void unguaranteedGreedyMaxCapacityInitState(double max_c) throws Exception {
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        int current_central = 0;
        for(int i=0; i < nc; ++i){
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

    public void closestInitState(int seed) throws Exception {
        Random rnd = new Random();
        rnd.setSeed(seed);
        int nc = problem.getNCustomers();
        int ns = problem.getNStations();
        for(int i=0; i < nc; ++i){
            if(problem.isGuaranteedCustomer(i)) {
                int s_id = searchClosestCentral(i, ns);
                if (problem.canAllocateCustomer2Station(i, s_id, 1.0)) { // NOTA IMPORTANT: No se per que el codi
                    problem.allocateCustomer2Station(i, s_id);           // amb 0.95  en comptes de 1.0 es no acaba
                }
                else {
                    boolean isAssigned = false;
                    do { // be careful with infinite loop
                        s_id = rnd.nextInt(ns);
                        if (problem.canAllocateCustomer2Station(i, s_id, 1.0)) {
                            problem.allocateCustomer2Station(i, s_id);
                            isAssigned = true;
                        }
                    } while (!isAssigned);
                }
            }
        }
    }

    private int searchClosestCentral(int c_id, int num_stations) {
        double d = problem.distance(c_id, 0);
        int ret = 0;
        for(int j = 1; j < num_stations; ++j) {
            if (problem.distance(c_id, j) < d) {
                d = problem.distance(c_id, j);
                ret = j;
            }
        }
        return ret;
    }
}
