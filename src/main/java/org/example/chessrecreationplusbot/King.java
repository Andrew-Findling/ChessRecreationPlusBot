package org.example.chessrecreationplusbot;

import java.util.ArrayList;

public class King extends ChessPiece {
    public int x;
    public int y;
    public boolean hasMoved = false;
    //bottom is 1, top is -1
    public int side;
    public boolean inCheck = false;
    public King(int x, int y, int side, Board board){
        super(board);
        this.x = x;
        this.y = y;
        this.side = side;
    }
    public ArrayList<Positions> getPossibleMoves(){
        ArrayList<Positions> possibleMoves = new ArrayList<>();
        if(board.canMoveTo(x+1,y, this.side) && board.kingCanMoveHere(x+1,y, this.side)){
            possibleMoves.add(new Positions(x+1,y));
        }
        if(board.kingCanMoveHere(x-1,y, this.side) && board.canMoveTo(x-1,y, this.side)){
            possibleMoves.add(new Positions(x-1,y));
        }
        if(board.kingCanMoveHere(x+1,y+1, this.side) && board.canMoveTo(x+1,y+1, this.side)){
            possibleMoves.add(new Positions(x+1,y+1));
        }
        if(board.kingCanMoveHere(x-1,y-1, this.side) && board.canMoveTo(x-1,y-1, this.side)){
            possibleMoves.add(new Positions(x-1,y-1));
        }
        if(board.kingCanMoveHere(x+1,y-1, this.side) && board.canMoveTo(x+1,y-1, this.side)){
            possibleMoves.add(new Positions(x+1,y-1));
        }
        if(board.kingCanMoveHere(x-1,y+1, this.side) && board.canMoveTo(x-1,y+1, this.side)){
            possibleMoves.add(new Positions(x-1,y+1));
        }
        if(board.kingCanMoveHere(x,y-1, this.side) && board.canMoveTo(x,y-1, this.side)){
            possibleMoves.add(new Positions(x,y-1));
        }
        if(board.kingCanMoveHere(x,y+1, this.side) && board.canMoveTo(x,y+1, this.side)){
            possibleMoves.add(new Positions(x,y+1));
        }
        if(Main.playerColor.equals("white")) {
            if (!board.isKingInCheck(this.side,board.getBoardArray()) && x + 2 <= 7 && board.getBoardArray()[x + 1][y] == null && board.getBoardArray()[x + 2][y] == null && !hasMoved && board.kingCanMoveHere(x + 1, y, this.side) && board.kingCanMoveHere(x + 2, y, this.side) && board.getBoardArray()[7][y] instanceof Rook && !((Rook) board.getBoardArray()[7][y]).hasMoved) {
                possibleMoves.add(new Positions(x + 2, y));
            }
            if (!board.isKingInCheck(this.side,board.getBoardArray()) && x - 3 >= 0 && board.getBoardArray()[x - 3][y] == null && board.getBoardArray()[x - 1][y] == null && board.getBoardArray()[x - 2][y] == null && !hasMoved && board.kingCanMoveHere(x - 1, y, this.side) && board.kingCanMoveHere(x - 2, y, this.side) && board.getBoardArray()[0][y] instanceof Rook && !((Rook) board.getBoardArray()[0][y]).hasMoved) {
                possibleMoves.add(new Positions(x - 2, y));
            }
        } else{
            if (!board.isKingInCheck(this.side,board.getBoardArray()) && !hasMoved && board.getBoardArray()[x - 1][y] == null && board.getBoardArray()[x - 2][y] == null && board.kingCanMoveHere(x - 1, y, this.side) && board.kingCanMoveHere(x - 2, y, this.side) && board.getBoardArray()[0][y] instanceof Rook && !((Rook) board.getBoardArray()[0][y]).hasMoved) {
                possibleMoves.add(new Positions(x - 2, y));
            }
            if (!board.isKingInCheck(this.side,board.getBoardArray()) && !hasMoved && board.getBoardArray()[x + 3][y] == null && board.getBoardArray()[x + 1][y] == null && board.getBoardArray()[x + 2][y] == null && board.kingCanMoveHere(x + 1, y, this.side)  && board.kingCanMoveHere(x + 2, y, this.side) && board.getBoardArray()[7][y] instanceof Rook && !((Rook) board.getBoardArray()[7][y]).hasMoved) {
                possibleMoves.add(new Positions(x + 2, y));
            }
        }
        return possibleMoves;
    }
    public ArrayList<Positions> getAttackingMoves(){
        ArrayList<Positions> possibleMoves = new ArrayList<>();
        if(board.canMoveTo(x+1,y, this.side)){
            possibleMoves.add(new Positions(x+1,y));
        }
        if(board.canMoveTo(x-1,y, this.side)){
            possibleMoves.add(new Positions(x-1,y));
        }
        if(board.canMoveTo(x+1,y+1, this.side)){
            possibleMoves.add(new Positions(x+1,y+1));
        }
        if(board.canMoveTo(x-1,y-1, this.side)){
            possibleMoves.add(new Positions(x-1,y-1));
        }
        if(board.canMoveTo(x+1,y-1, this.side)){
            possibleMoves.add(new Positions(x+1,y-1));
        }
        if(board.canMoveTo(x-1,y+1, this.side)){
            possibleMoves.add(new Positions(x-1,y+1));
        }
        if(board.canMoveTo(x,y-1, this.side)){
            possibleMoves.add(new Positions(x,y-1));
        }
        if(board.canMoveTo(x,y+1, this.side)){
            possibleMoves.add(new Positions(x,y+1));
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
        return 0;
    }
    public ChessPiece copy(Board newBoard) {
        King copy = new King(x, y, side, newBoard);
        copy.hasMoved = this.hasMoved;
        return copy;
    }
}