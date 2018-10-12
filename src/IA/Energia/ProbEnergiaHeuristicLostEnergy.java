package IA.Energia;

import aima.search.framework.HeuristicFunction;

public class ProbEnergiaHeuristicLostEnergy implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        ProbEnergiaBoard board = (ProbEnergiaBoard) state;
        return board.getLostEnergy();
    }
}
