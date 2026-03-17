
package org.example.chessrecreationplusbot;

import java.util.ArrayList;

public class Rook extends ChessPiece {
    public int x;
    public int y;
    public boolean hasMoved = false;
    //bottom is 1, top is -1
    public int side;
    public Rook(int x, int y, int side, Board board){
        super(board);
        this.x = x;
        this.y = y;
        this.side = side;
    }
    public ArrayList<Positions> getPossibleMoves(){
        ArrayList<Positions> possibleMoves = new ArrayList<>();
        //check right horizontal of rook
        for(int x = this.x+1; x<8; x++){
            if(board.pieceAt(x,this.y) == null) possibleMoves.add(new Positions(x, this.y));
            else if(board.pieceAt(x,this.y).getSide() != this.side) {
                possibleMoves.add(new Positions(x, this.y));
                break;
            }
            else break;
        }
        //check left horizontal of rook
        for(int x = this.x-1; x>=0; x--){
            if(board.pieceAt(x,this.y) == null) possibleMoves.add(new Positions(x, this.y));
            else if(board.pieceAt(x,this.y).getSide() != this.side) {
                possibleMoves.add(new Positions(x, this.y));
                break;
            }
            else break;
        }
        //check bottom vertical of rook
        for(int y = this.y+1; y<8; y++){
            if(board.pieceAt(this.x,y) == null) possibleMoves.add(new Positions(this.x, y));
            else if(board.pieceAt(this.x, y).getSide() != this.side){
                possibleMoves.add(new Positions(this.x, y));
                break;
            }
            else break;
        }
        //check top vertical of rook
        for(int y = this.y-1; y>=0; y--){
            if(board.pieceAt(this.x,y) == null) possibleMoves.add(new Positions(this.x, y));
            else if(board.pieceAt(this.x, y).getSide() != this.side){
                possibleMoves.add(new Positions(this.x, y));
                break;
            }
            else break;
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
        return 5;
    }
    public ChessPiece copy(Board newBoard) {
        Rook copy = new Rook(x, y, side, newBoard);
        copy.hasMoved = this.hasMoved;
        return copy;
    }
    public ArrayList<Positions> getAttackingMoves() {
        return getPossibleMoves();
    }
}