package IA.Energia;

import aima.search.framework.HeuristicFunction;

public class ProbEnergiaHeuristicGuaranteed implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        ProbEnergiaBoard board = (ProbEnergiaBoard) state;
        return board.getNGuaranteedCustomersAllocated();
    }
}
