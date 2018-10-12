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
        // 4. swap
        try {
            for (int i = 0; i < board.getNCustomers(); ++i) {
                if (board.canDeallocateCustomer(i) && !board.isGuaranteedCustomer(i)) { //afegeixo el check perquè ha desaparegut del canDeallocateCustomer
                    ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                    new_board.deallocateCustomer(i);
                    ret.add(new Successor("deallocate customer " + i +
                            "\n\t benefit = " + new_board.getBenefit() , new_board));
                }
                for (int j = 0; j < board.getNStations(); ++j) {
                    if (board.canAllocateCustomer2Station(i, j)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.allocateCustomer2Station(i, j);
                        ret.add(new Successor("allocate customer " + i + " to station " + j +
                                "\n\t benefit = " + new_board.getBenefit() , new_board));
                    }
                    if (board.canReallocateCustomer(i,j)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.reallocateCustomer(i, j);
                        ret.add(new Successor("reallocate customer " + i + " to station " + j +
                                "\n\t benefit = " + new_board.getBenefit() , new_board));
                    }
                }
                /*
                for (int j = 0; j < board.getNCustomers(); ++j) {
                    if (board.canSwapCustomers(i,j)) {
                        ProbEnergiaBoard new_board = new ProbEnergiaBoard(board);
                        new_board.swapCustomers(i, j);
                        ret.add(new Successor("swap customer " + i + " and " + j +
                                "\n\t benefit = " + new_board.getBenefit() , new_board));
                    }
                }*/
            }
        } catch (Exception e) {
            System.out.println("Problem in generating successors");
            e.printStackTrace();
        }
        return ret;
    }
}
