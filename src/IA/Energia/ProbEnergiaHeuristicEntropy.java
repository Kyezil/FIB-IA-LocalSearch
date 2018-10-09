package IA.Energia;

import aima.search.framework.HeuristicFunction;

public class ProbEnergiaHeuristicEntropy implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        ProbEnergiaBoard board = (ProbEnergiaBoard) state;
        // sum of c * log(c) where c is proportion of central used
        double h = 0;
        for (int i = 0; i < board.getNStations(); ++i) {
            double c = board.getStationRemainingProportion(i);
            h -= c * Math.log(c);
        }
        return h;
    }
}
