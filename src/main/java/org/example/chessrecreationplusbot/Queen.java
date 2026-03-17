package org.example.chessrecreationplusbot;

import java.util.ArrayList;

public class Queen extends ChessPiece {
    public int x;
    public int y;
    //bottom is 1, top is -1
    public int side;
    public Queen(int x, int y, int side, Board board){
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


        //check right top diagonal of bishop
        for(int add = 1; add<8; add++){
            if(x+add>7 || x+add<0 || y-add <0 || y-add>7) break;
            if(board.pieceAt(this.x + add,this.y - add) == null) possibleMoves.add(new Positions(this.x + add, this.y - add));
            else if(board.pieceAt(this.x + add,this.y - add).getSide() != this.side) {
                possibleMoves.add(new Positions(this.x + add, this.y - add));
                break;
            }
            else break;
        }
        //check left top diagonal of bishop
        for(int add = 1; add<8; add++){
            if(x-add>7 || x-add<0 || y-add <0 || y-add>7) break;
            if(board.pieceAt(this.x - add,this.y - add) == null) possibleMoves.add(new Positions(this.x - add, this.y - add));
            else if(board.pieceAt(this.x - add,this.y - add).getSide() != this.side) {
                possibleMoves.add(new Positions(this.x - add, this.y - add));
                break;
            }
            else break;
        }
        //check right bottom diagonal of bishop
        for(int add = 1; add<8; add++){
            if(x+add>7 || x+add<0 || y+add <0 || y+add>7) break;
            if(board.pieceAt(this.x + add,this.y + add) == null) possibleMoves.add(new Positions(this.x + add, this.y + add));
            else if(board.pieceAt(this.x + add,this.y + add).getSide() != this.side) {
                possibleMoves.add(new Positions(this.x + add, this.y + add));
                break;
            }
            else break;
        }
        //check left bottom diagonal of bishop
        for(int add = 1; add<8; add++){
            if(x-add>7 || x-add<0 || y+add <0 || y+add>7) break;
            if(board.pieceAt(this.x - add,this.y + add) == null) possibleMoves.add(new Positions(this.x - add, this.y + add));
            else if(board.pieceAt(this.x - add,this.y + add).getSide() != this.side) {
                possibleMoves.add(new Positions(this.x - add, this.y + add));
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
        return 9;
    }
    public ChessPiece copy(Board newBoard) {
        Queen copy = new Queen(x, y, side, newBoard);
        return copy;
    }
    public ArrayList<Positions> getAttackingMoves() {
        return getPossibleMoves();
    }
}