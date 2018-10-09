package IA.Energia;

import aima.search.framework.HeuristicFunction;

public class ProbEnergiaHeuristicBenefit implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        return -((ProbEnergiaBoard) state).getBenefit();
    }
}
