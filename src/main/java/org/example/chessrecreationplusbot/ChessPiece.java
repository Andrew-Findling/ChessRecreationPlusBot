package org.example.chessrecreationplusbot;

import java.util.ArrayList;

public abstract class ChessPiece {
    Board board;
    public ChessPiece(Board board){
        this.board = board;
    }
    public abstract ArrayList<Positions> getPossibleMoves();
    //white is 1, black is -1
    public abstract int getSide();
    public abstract int getY();
    public abstract int getX();
    public abstract void setX(int row);
    public abstract void setY(int col);
    public abstract double getValue();
    public abstract ChessPiece copy(Board board);
    public abstract ArrayList<Positions> getAttackingMoves();

}
