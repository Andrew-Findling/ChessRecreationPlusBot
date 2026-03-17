package org.example.chessrecreationplusbot;

import java.util.ArrayList;

public class Board {
    public ChessPiece[][] boardArray;
    public Board(ChessPiece[][] board) {
        this.boardArray = board;
    }
    public Board(boolean deepCopy, Board otherBoard) {
        this.boardArray = new ChessPiece[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (otherBoard.boardArray[r][c] != null) {
                    this.boardArray[r][c] = otherBoard.boardArray[r][c].copy(this);
                }
            }
        }
    }
    public void move(int xi, int yi, int xf, int yf){
        boardArray[xf][yf] = boardArray[xi][yi];
        boardArray[xi][yi] = null;
    }
    public boolean canMoveTo(int x, int y, int color){
        if(x>=8 || y >= 8 || y<0 || x<0) return false;
        return boardArray[x][y] == null || boardArray[x][y].getSide() != color;
    }
    public boolean pawnCanMoveTo(int x, int y, Pawn p){
        if(x>=8 || y >= 8 || y<0 || x<0) return false;
        //moving forward
        if(boardArray[x][y] == null && p.x == x){
            return true;
        }
        //taking
        else if(boardArray[x][y] != null && boardArray[x][y].getSide() != p.getSide() && Math.abs(x-p.x)==1){
            return true;
        }
        //en passant
        else if(boardArray[x][y] == null && boardArray[x][y+p.getSide()] != null && boardArray[x][y+p.getSide()].getSide() != p.getSide() && boardArray[x][y+ p.getSide()] instanceof Pawn && ((Pawn) boardArray[x][y+ p.getSide()]).justDoubledMoved){
            return true;
        }
        return false;
    }
    public ChessPiece pieceAt(int x, int y){
        return boardArray[x][y];
    }
    public boolean kingCanMoveHere(int x, int y, int color){
        for(int r = 0; r<8; r++){
            for(int c = 0; c<8; c++){
                if(boardArray[r][c] != null && boardArray[r][c].getSide() != color){
                    ArrayList<Positions> pieceMoves;
                    if (boardArray[r][c] instanceof King) {
                        pieceMoves = ((King) boardArray[r][c]).getAttackingMoves();
                    }
                    else if(boardArray[r][c] instanceof Pawn){
                        pieceMoves = ((Pawn) boardArray[r][c]).getAttackingMoves();
                    }
                    else {
                        pieceMoves = boardArray[r][c].getPossibleMoves();
                    }

                    for(int i = 0; i < pieceMoves.size(); i++){
                        Positions p = pieceMoves.get(i);
                        if(p.x == x && p.y == y){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public boolean isKingInCheck(int currentSide, ChessPiece[][] possibleBoardArray){
        for(int r = 0; r<8; r++){
            for(int c = 0; c<8; c++){
                if(possibleBoardArray[r][c] instanceof King && possibleBoardArray[r][c].getSide() == currentSide){
                    return !kingCanMoveHere(r,c, currentSide);
                }
            }
        }
        return false;
    }
    public void promotePawns(){
        for(int c =  0; c<8; c++){
            if(boardArray[c][7] instanceof Pawn && boardArray[c][7].getSide() == -1){
                boardArray[c][7] = new Queen(c,7,-1,this);
            }
            else if (boardArray[c][0] instanceof Pawn && boardArray[c][0].getSide() == 1){
                boardArray[c][0] = new Queen(c,0,1,this);
            }
        }
    }
    public ChessPiece[][] getBoardArray(){
        return boardArray;
    }
    public ArrayList<Positions> getLegalMoves(ChessPiece piece){
        ArrayList<Positions> possibleMoves = new ArrayList<>(piece.getPossibleMoves());
        ArrayList<Positions> legalMoves = new ArrayList<>();

        int oldX = piece.getX();
        int oldY = piece.getY();

        for (Positions move : possibleMoves) {
            ChessPiece capturedPiece = boardArray[move.x][move.y];

            boardArray[oldX][oldY] = null;
            boardArray[move.x][move.y] = piece;
            piece.setX(move.x);
            piece.setY(move.y);

            if (!isKingInCheck(piece.getSide(), boardArray)) {
                legalMoves.add(move);
            }

            boardArray[move.x][move.y] = capturedPiece;
            boardArray[oldX][oldY] = piece;
            piece.setX(oldX);
            piece.setY(oldY);
        }

        return legalMoves;
    }
}
