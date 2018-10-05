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


    // SETTERS AND GETTERS
    public void setCustomers(Clientes cs) {
        customers = cs;

        customer2station = new int[cs.size()];
        // everyone is unassigned
        for (int i = 0; i < cs.size(); ++i) customer2station[i] = UNALLOCATED;
    }
    public void setStations(Centrales ss) {
        stations = ss;
        stationRemainingProduction = new double[ss.size()];
        for (int i = 0; i < ss.size(); ++i) {
            stationRemainingProduction[i] = ss.get(i).getProduccion();
        }
    }

    public int getNCustomers() { return customers.size(); }
    public int getNStations() { return stations.size(); }

    public Cliente getCustomer(int c_id) { return customers.get(c_id); }
    public Central getStation(int s_id) { return stations.get(s_id); }
    public int getAssignedStation(int c_id) {
        return customer2station[c_id];
    }

    // OPERATORS
    public boolean canAssignCustomer2Station(int c_id, int s_id) {
        return (customer2station[c_id] != s_id) // not already assigned
            && (stationRemainingProduction[s_id] >= consumerConsumptionInStation(c_id, s_id)); // has enough space
    }

    public void assignCustomer2Station(int c_id, int s_id) {
        // deallocate in old
        deallocateCustomer(c_id);
        // allocate in new
        customer2station[c_id] = s_id;
        stationRemainingProduction[s_id] -= consumerConsumptionInStation(c_id, s_id);
    }

    public void deallocateCustomer(int c_id) {
        int s_id = customer2station[c_id];
        if (s_id != UNALLOCATED) {
            stationRemainingProduction[s_id] += consumerConsumptionInStation(c_id, s_id);
            customer2station[c_id] = UNALLOCATED;
        }
    }


    // UTILITIES
    public boolean isGuaranteedCustomer(int c_id) {
        return customers.get(c_id).getContrato() == Cliente.GARANTIZADO;
    }
    // returns the consumption (MW) needed for customer c_id in station s_id
    public double consumerConsumptionInStation(int c_id, int s_id) {
        double factor = 1.0 + VEnergia.getPerdida(distance(c_id, s_id));
        return customers.get(c_id).getConsumo() * factor;
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
                '}';
    }
}
