package org.example.chessrecreationplusbot;

import java.util.ArrayList;

public class Pawn extends ChessPiece{

    public int x;
    public int y;
    //bottom is 1, top is -1
    public int side;
    boolean isFirstMove = true;
    boolean justDoubledMoved = false;
    public Pawn(int x, int y, int side, Board board){
        super(board);
        this.x = x;
        this.y = y;
        this.side = side;
    }
    public ArrayList<Positions> getPossibleMoves(){
        ArrayList<Positions> possibleMoves = new ArrayList<>();
        if(board.pawnCanMoveTo(x,y-2* side, this) && board.pieceAt(x,y-side) == null && isFirstMove){
            possibleMoves.add(new Positions(x,y-2* side));
        }
        if(board.pawnCanMoveTo(x,y-side,this)){
            possibleMoves.add(new Positions(x,y - side));
        }
        if(board.pawnCanMoveTo(x + 1,y- side, this)){
            possibleMoves.add(new Positions(x + 1,y-side));
        }
        if(board.pawnCanMoveTo(x-1, y-side, this)){
            possibleMoves.add(new Positions(x-1, y-side));
        }
        return possibleMoves;
    }
    public ArrayList<Positions> getAttackingMoves() {
        ArrayList<Positions> moves = new ArrayList<>();
        moves.add(new Positions(x - 1, y - side));
        moves.add(new Positions(x + 1, y - side));
        return moves;
    }
    public int getSide(){
        return side;
    }
    public int getY(){
        return y;
    }
    public int getX(){
        return x;
    }
    public void setX(int row){
        x = row;
    }
    public void setY(int col){
        y = col;
    }
    public double getValue(){
        return 1;
    }
    public ChessPiece copy(Board newBoard) {
        Pawn copy = new Pawn(x, y, side, newBoard);
        copy.isFirstMove = this.isFirstMove;
        copy.justDoubledMoved = this.justDoubledMoved;
        return copy;
    }
}