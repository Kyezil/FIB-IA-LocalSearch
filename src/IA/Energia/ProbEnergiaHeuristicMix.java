package IA.Energia;

import aima.search.framework.HeuristicFunction;

public class ProbEnergiaHeuristicMix implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        ProbEnergiaBoard board = (ProbEnergiaBoard) state;
        HeuristicFunction ben = new ProbEnergiaHeuristicBenefit();
        HeuristicFunction ent = new ProbEnergiaHeuristicEntropy();
        double p = 0.6;
        return p * ben.getHeuristicValue(board)/2e4 + (1-p) * ent.getHeuristicValue(board);
    }
}
