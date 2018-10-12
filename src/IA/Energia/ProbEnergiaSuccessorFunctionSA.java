package IA.Energia;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ProbEnergiaSuccessorFunctionSA implements SuccessorFunction {

    @Override
    public List getSuccessors (Object o) {
        ArrayList ret = new ArrayList();
        ProbEnergiaBoard board = (ProbEnergiaBoard) o;
        Random myRandom = new Random(); // probablitat proporcional al nombre de successors que generen

        // operators
        // 1. allocate user x station: nm
        // 2. deallocate user: n
        // 3. reallocate user x station: nm
        // 4. swap: n^2
        int n = board.getNCustomers();
        int m = board.getNStations();

        double total = 2*n*m + n + n*n;
        // Probabilitats acumulades
        double p_deallocate = n / total;
        double p_r_allocate = n*m / total + p_deallocate;

        boolean done = false;

        try {
            do {
                double r = myRandom.nextDouble();
                if (r < p_deallocate) {
                    int i = myRandom.nextInt(n);
                    if (board.canDeallocateCustomer(i) && !board.isGuaranteedCustomer(i)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.deallocateCustomer(i);
                        ret.add(new Successor("", new_board));
                        done = true;
                    }
                } else if (r < p_r_allocate) {
                    int i = myRandom.nextInt(n);
                    int j = myRandom.nextInt(m);
                    if (board.canAllocateCustomer2Station(i, j)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.allocateCustomer2Station(i, j);
                        ret.add(new Successor("", new_board));
                        done = true;
                    } else if (board.canReallocateCustomer(i,j)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.reallocateCustomer(i, j);
                        ret.add(new Successor("", new_board));
                        done = true;
                    }
                } else {
                    int i = myRandom.nextInt(n);
                    int j = myRandom.nextInt(m);
                    if (board.canSwapCustomers(i, j)){
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.swapCustomers(i, j);
                        ret.add(new Successor("", new_board));
                        done = true;
                    }
                }
            } while (!done);
        } catch (Exception e) {
            System.out.println("Problem in generating SA successors");
            e.printStackTrace();
        }
        return ret;
    }
}
