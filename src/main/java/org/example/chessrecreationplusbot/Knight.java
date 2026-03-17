package org.example.chessrecreationplusbot;

import java.util.ArrayList;

public class Knight extends ChessPiece {
    public int x;
    public int y;
    //bottom is 1, top is -1
    public int side;
    public Knight(int x, int y, int side, Board board){
        super(board);
        this.x = x;
        this.y = y;
        this.side = side;
    }
    public ArrayList<Positions> getPossibleMoves(){
        ArrayList<Positions> possibleMoves = new ArrayList<>();
        if(board.canMoveTo(x+1,y-2, this.side)){
            possibleMoves.add(new Positions(x+1,y-2));
        }
        if(board.canMoveTo(x-1,y-2, this.side)){
            possibleMoves.add(new Positions(x-1,y-2));
        }
        if(board.canMoveTo(x+1,y+2, this.side)){
            possibleMoves.add(new Positions(x+1,y+2));
        }
        if(board.canMoveTo(x-1,y+2, this.side)){
            possibleMoves.add(new Positions(x-1,y+2));
        }
        if(board.canMoveTo(x+2,y-1, this.side)){
            possibleMoves.add(new Positions(x+2,y-1));
        }
        if(board.canMoveTo(x-2,y-1, this.side)){
            possibleMoves.add(new Positions(x-2,y-1));
        }
        if(board.canMoveTo(x+2,y+1, this.side)){
            possibleMoves.add(new Positions(x+2,y+1));
        }
        if(board.canMoveTo(x-2,y+1, this.side)){
            possibleMoves.add(new Positions(x-2,y+1));
        }
        return possibleMoves;
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
        return 3;
    }
    public ChessPiece copy(Board newBoard) {
        Knight copy = new Knight(x, y, side, newBoard);
        return copy;
    }
    public ArrayList<Positions> getAttackingMoves(){
        return getPossibleMoves();
    }
}
