package IA.Energia;

import aima.search.framework.HeuristicFunction;

public class ProbEnergiaHeuristicMix implements HeuristicFunction {

    private double p;
    private double factorPenal;

    public ProbEnergiaHeuristicMix(double prop, double factorPenalitzacio) {
        p = prop;
        factorPenal = factorPenalitzacio;
    }
    public double getHeuristicValue(Object state) {
        ProbEnergiaBoard board = (ProbEnergiaBoard) state;
        HeuristicFunction ben = new ProbEnergiaHeuristicBenefit();
        HeuristicFunction ent = new ProbEnergiaHeuristicEntropy();
        return p * ben.getHeuristicValue(board)/3e4 + (1-p) * ent.getHeuristicValue(board);
    }
}
