package IA.Energia;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class ProbEnergiaSuccessorFunction implements SuccessorFunction {

    @Override
    public List getSuccessors(Object o) {
        ArrayList ret = new ArrayList();
        ProbEnergiaBoard board = (ProbEnergiaBoard) o;
        // apply operators
        // 1. allocate user x station
        // 2. deallocate user
        // 3. reallocate user x station
        try {
            for (int i = 0; i < board.getNCustomers(); ++i) {
                if (board.canDeallocateCustomer(i)) {
                    ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                    new_board.deallocateCustomer(i);
                    ret.add(new Successor("deallocate customer " + i, new_board));
                }
                for (int j = 0; j < board.getNStations(); ++j) {
                    if (board.canAllocateCustomer2Station(i, j)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.allocateCustomer2Station(i, j);
                        ret.add(new Successor("allocate customer " + i + " to station " + j, new_board));
                    }
                    if (board.canReallocateCustomer(i,j)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.reallocateCustomer(i, j);
                        ret.add(new Successor("reallocate customer " + i + " to station " + j, new_board));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Problem in generating successors");
            e.printStackTrace();
        }
        return ret;
    }
}
