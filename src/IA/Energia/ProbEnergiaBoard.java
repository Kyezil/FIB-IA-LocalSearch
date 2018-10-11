package IA.Energia;


import java.util.Arrays;

// Represents a problem of matching customers to power stations
public class ProbEnergiaBoard {
    // List of customers
    static private Clientes customers;
    // List of power stations
    static private Centrales stations;

    // Relation customer~plant
    // customer2plant[i] = j  =>  j /= -1, customer i is connected to station j
    //                            j == -1, customer i is not connected
    private int[] customer2station;
    private static final int UNALLOCATED = -1;
    private double[] stationRemainingProduction;

    // heuristics values
    private double hBenefit;
    private double hEntropy;

    public ProbEnergiaBoard() throws Exception {
        hBenefit = 0;
        hEntropy = 0;
    }

    public ProbEnergiaBoard(ProbEnergiaBoard board) {
        this.customer2station = board.customer2station.clone();
        this.stationRemainingProduction = board.stationRemainingProduction.clone();
        this.hBenefit = board.hBenefit;
        this.hEntropy = board.hEntropy;
    }

    // SETTERS AND GETTERS
    public void setCustomers(Clientes cs) throws Exception {
        customers = cs;

        customer2station = new int[cs.size()];
        // everyone is unassigned
        for (int i = 0; i < cs.size(); ++i) {
            customer2station[i] = UNALLOCATED;
            hBenefit -= getCustomerPenalization(i);
        }
    }

    public void setStations(Centrales ss) throws Exception {
        stations = ss;
        stationRemainingProduction = new double[ss.size()];
        for (int i = 0; i < ss.size(); ++i) {
            stationRemainingProduction[i] = ss.get(i).getProduccion();
            hBenefit -= getStationStopCost(i);
        }
    }

    public int getNCustomers() { return customers.size(); }
    public int getNStations() { return stations.size(); }

    public Cliente getCustomer(int c_id) { return customers.get(c_id); }
    public Central getStation(int s_id) { return stations.get(s_id); }
    public int getAssignedStation(int c_id) {
        return customer2station[c_id];
    }

    public double getBenefit() {
        return hBenefit;
    }

    public double getEntropy(){
        return hEntropy;
    }

    // OPERATORS
    public boolean canAllocateCustomer2Station(int c_id, int s_id) {
        return !isCustomerAllocated(c_id) // not already assigned
            && (stationRemainingProduction[s_id] >= consumerConsumptionInStation(c_id, s_id)); // has enough space
    }

    // can allocate so that the remaining production proportion is above max_prod_prop in [0,1]
    public boolean canAllocateCustomer2Station(int c_id, int s_id, double max_prod_prop) {
        if (customer2station[c_id] != UNALLOCATED) return false;
        double prop_after_assign = (stationRemainingProduction[s_id] - consumerConsumptionInStation(c_id, s_id))
                                    / getStation(s_id).getProduccion();
        return prop_after_assign >= 1-max_prod_prop;
    }

    public void allocateCustomer2Station(int c_id, int s_id) throws Exception {
        if(isStationEmpty(s_id)) hBenefit += getStationStopCost(s_id) - getStationRunCost(s_id);
        hEntropy -= getStationEntropy(s_id);
        hBenefit += getCustomerBenefit(c_id) + getCustomerPenalization(c_id);
        customer2station[c_id] = s_id;
        stationRemainingProduction[s_id] -= consumerConsumptionInStation(c_id, s_id);
        hEntropy += getStationEntropy(s_id);
    }

    public boolean canDeallocateCustomer(int c_id){
        return isCustomerAllocated(c_id) && !isGuaranteedCustomer(c_id);
    }


    public void deallocateCustomer(int c_id) throws Exception {
        int s_id = customer2station[c_id];
        hEntropy -= getStationEntropy(s_id);
        stationRemainingProduction[s_id] += consumerConsumptionInStation(c_id, s_id);
        if(isStationEmpty(s_id)) hBenefit += getStationRunCost(s_id) - getStationStopCost(s_id);
        hBenefit += - getCustomerBenefit(c_id) - getCustomerPenalization(c_id);
        hEntropy += getStationEntropy(s_id);
        customer2station[c_id] = UNALLOCATED;
    }

    public boolean canSwapCustomers(int c_id1, int c_id2){
        if (c_id1 == c_id2) return false;
        if(!isCustomerAllocated(c_id1) || !isCustomerAllocated(c_id2)) return false;
        int s_id1 = customer2station[c_id1];
        int s_id2 = customer2station[c_id2];
        double current_consumption_c1 = consumerConsumptionInStation(c_id1, s_id1);
        double current_consumption_c2 = consumerConsumptionInStation(c_id2, s_id2);
        double new_consumption_c1 = consumerConsumptionInStation(c_id1, s_id2);
        double new_consumption_c2 = consumerConsumptionInStation(c_id2, s_id1);
        double newRemaining_s1 = stationRemainingProduction[s_id1] + current_consumption_c1 - new_consumption_c2;
        double newRemaining_s2 = stationRemainingProduction[s_id2] + current_consumption_c2 - new_consumption_c1;
        return newRemaining_s1 >= 0 && newRemaining_s2 >= 0;
    }

    public void swapCustomers(int c_id1, int c_id2){
        int s_id1 = customer2station[c_id1];
        int s_id2 = customer2station[c_id2];
        hEntropy -= getStationEntropy(s_id1);
        hEntropy -= getStationEntropy(s_id2);
        double current_consumption_c1 = consumerConsumptionInStation(c_id1, s_id1);
        double current_consumption_c2 = consumerConsumptionInStation(c_id2, s_id2);
        double new_consumption_c1 = consumerConsumptionInStation(c_id1, s_id2);
        double new_consumption_c2 = consumerConsumptionInStation(c_id2, s_id1);
        stationRemainingProduction[s_id1] += current_consumption_c1 - new_consumption_c2;
        stationRemainingProduction[s_id2] += current_consumption_c2 - new_consumption_c1;
        customer2station[c_id1] = s_id2;
        customer2station[c_id2] = s_id1;
        hEntropy += getStationEntropy(s_id1);
        hEntropy += getStationEntropy(s_id2);
        // Aqui es faria el calcul heuristic
    }

    public boolean canReallocateCustomer(int c_id, int s_id){
        if(!isCustomerAllocated(c_id)) return false;
        double new_consumption = consumerConsumptionInStation(c_id, s_id);
        return stationRemainingProduction[s_id] - new_consumption >= 0; // si algo peta, check this
    }

    public void reallocateCustomer(int c_id, int s_id) throws Exception {
        int current_s_id = customer2station[c_id];
        hEntropy -= getStationEntropy(s_id);
        hEntropy -= getStationEntropy(current_s_id);
        double current_consumption = consumerConsumptionInStation(c_id, current_s_id);
        double new_consumption = consumerConsumptionInStation(c_id, s_id);
        // Mirem si movem a una central que era buida
        if(isStationEmpty(s_id)) hBenefit += getStationStopCost(s_id) - getStationRunCost(s_id);
        stationRemainingProduction[s_id] -= new_consumption;
        // Mirem si l'actual passa a ser buida
        stationRemainingProduction[current_s_id] += current_consumption;
        if(isStationEmpty(current_s_id)) hBenefit += getStationRunCost(current_s_id) - getStationStopCost(current_s_id);
        customer2station[c_id] = s_id;
        hEntropy += getStationEntropy(s_id);
        hEntropy += getStationEntropy(current_s_id);
    }

    // UTILITIES
    public double getStationEntropy(int s_id){
        return - getStationRemainingProportion(s_id) * Math.log(getStationRemainingProportion(s_id));
    }

    public boolean isCustomerAllocated(int c_id){
        return customer2station[c_id] != UNALLOCATED;
    }

    public boolean isGuaranteedCustomer(int c_id) {
        return customers.get(c_id).getContrato() == Cliente.GARANTIZADO;
    }

    public boolean isStationEmpty(int s_id) {
        return stationRemainingProduction[s_id] == stations.get(s_id).getProduccion();
    }

    // returns the consumption (MW) needed for customer c_id in station s_id
    public double consumerConsumptionInStation(int c_id, int s_id) {
        double factor = 1.0 + VEnergia.getPerdida(distance(c_id, s_id));
        return customers.get(c_id).getConsumo() * factor;
    }

    public double getStationRunCost(int s_id) throws Exception {
        Central central = getStation(s_id);
        double produccion = central.getProduccion();
        int tipo = central.getTipo();
        double costeProduccionMW = VEnergia.getCosteProduccionMW(tipo);
        double costeMarcha = VEnergia.getCosteMarcha(tipo);
        return produccion*costeProduccionMW + costeMarcha;
    }

    public double getCustomerPenalization(int c_id) throws Exception {
        Cliente client = getCustomer(c_id);
        double consumo = client.getConsumo();
        int tipo = client.getTipo();
        return consumo * VEnergia.getTarifaClientePenalizacion(tipo);
    }

    public double getCustomerBenefit(int c_id) throws Exception {
        Cliente client = getCustomer(c_id);
        double consumo = client.getConsumo();
        int tipo = client.getTipo();
        if(isGuaranteedCustomer(c_id)) return consumo * VEnergia.getTarifaClienteGarantizada(tipo);
        else return consumo * VEnergia.getTarifaClienteNoGarantizada(tipo);
    }

    public double getStationStopCost(int s_id) throws Exception {
        Central central = getStation(s_id);
        int tipo = central.getTipo();
        return VEnergia.getCosteParada(tipo);
    }

    public double getStationRemainingProportion(int s_id) {
        return stationRemainingProduction[s_id] / getStation(s_id).getProduccion();
    }

    // distance between customer c_id and station s_id
    public double distance(int c_id, int s_id) {
        Cliente c = customers.get(c_id);
        Central s = stations.get(s_id);
        return distance(c.getCoordX(), c.getCoordY(), s.getCoordX(), s.getCoordY());
    }
    // euclidean distance between points (x1,y1) and (x2,y2)
    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public String toString() {
        return "ProbEnergiaBoard{" +
                "\n\tcustomer2station=" + Arrays.toString(customer2station) +
                "\n\tstationRemainingProduction=" + Arrays.toString(stationRemainingProduction) +
                "\n\thBenefit=" + hBenefit +
                '}';
    }
}
