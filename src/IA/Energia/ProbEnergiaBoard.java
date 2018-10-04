package IA.Energia;

import IA.Energia.Centrales;
import IA.Energia.Clientes;


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

    public void setCustomers(Clientes cs) {
        customers = cs;

        customer2station = new int[cs.size()];
        // everyone is unassigned
        for (int i = 0; i < cs.size(); ++i) deallocateCustomer(i);
    }
    public void setStations(Centrales ss) { stations = ss; }

    public int getNCustomers() { return customers.size(); }
    public int getNStations() { return stations.size(); }

    public void assignStation2Customer(int c_id, int s_id) {
        customer2station[c_id] = s_id;
    }
    public void deallocateCustomer(int c_id) {
        customer2station[c_id] = -1;
    }

    public Cliente getCustomer(int c_id) { return customers.get(c_id); }
    public Central getStation(int s_id) { return stations.get(s_id); }
    public Central getAssignedStation(int c_id) {
        int s_id = customer2station[c_id];
        if (s_id == -1) return null;
        else return stations.get(s_id);
    }
}
