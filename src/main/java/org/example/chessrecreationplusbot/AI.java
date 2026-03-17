package org.example.chessrecreationplusbot;

import java.util.ArrayList;

import static org.example.chessrecreationplusbot.Main.playerColor;
import static org.example.chessrecreationplusbot.Main.botHasntMoved;

public class AI {
    public AI(){}
    public int side = -1;
    //WORKING ON
    public Move minimax(Board board, int ply){
        //implement instant first move
        if(botHasntMoved){
            botHasntMoved = false;
            if(playerColor.equals("black")){
                return new Move(4, 1, 4, 3);
            }
            else if(playerColor.equals("white") && board.getBoardArray()[4][4] instanceof Pawn) {
                return new Move(4, 1, 4, 3);
            }
            else if(playerColor.equals("white") && board.getBoardArray()[3][4] instanceof Pawn) {
                return new Move(3, 1, 3, 3);
            }
        }
        return calcBestMove(board,-1,ply);
    }
    //needs to improve, but good for now
    public double evalScore(Board board){
        double score = 0;
        int pieceCount = 0;
        ChessPiece[][] boardArray = board.getBoardArray();
        boolean botMated = board.isKingInCheck(side,board.getBoardArray());
        boolean playerMated = board.isKingInCheck(1,board.getBoardArray());
        //checks what squares bot and player hit for later
        boolean [][] botHits = getHitMap(side, boardArray);
        boolean [][] playerHits = getHitMap(1,boardArray);
        //gives bonuses and minuses for kings being in check
        if(board.isKingInCheck(-1,board.getBoardArray())) score-=.3;
        if(board.isKingInCheck(1,board.getBoardArray())) score+=.3;
        for(int r = 0; r<8; r++){
            for(int c = 0; c<8; c++){
                ChessPiece piece = boardArray[r][c];
                //piece counter obv
                if(piece!=null && piece.getSide()==side) pieceCount++;
                if(piece!=null && piece.getSide()==side && (piece instanceof Knight || piece instanceof Bishop)) {
                    //promotes activity
                    if(piece.getY()!=0 && pieceCount>10)score+=.4;
                }
                //yay pawn getting closer to promotion
                else if(piece!=null && piece.getSide() == side && piece instanceof Pawn) score+=(double)piece.getY()/7;
                //yay we castle
                else if(piece!= null && piece.getSide() == side && piece instanceof King){
                    if((r== 6 || r==2) && pieceCount>7) score+=.5;
                }
                //nooo enemy pawn getting close
                else if(piece!=null && piece.getSide() != side && piece instanceof Pawn){
                    score-=(double)(7 - piece.getY())/7;
                }
                //checks if any pieces are hanging
                if(piece!=null && piece.getSide()==side && playerHits[r][c] && !botHits[r][c]){
                    score-=piece.getValue()*.5;
                }
                if(piece!=null&& piece.getSide()!=side && botHits[r][c] && !playerHits[r][c]){
                    score+=piece.getValue()*.5;
                }
                //count piece scores
                if(boardArray[r][c] != null) {
                    if (piece.getSide() == side) {
                        score += piece.getValue();
                    } else score -= piece.getValue();

                    if (piece.getSide() == side && botMated && !board.getLegalMoves(piece).isEmpty()) {
                        botMated = false;
                    }
                    if (piece.getSide() != side && playerMated && !board.getLegalMoves(piece).isEmpty()) {
                        playerMated = false;
                    }
                }
            }
        }
        //first is stalemate
        if(botMated && !board.isKingInCheck(side,board.getBoardArray()) || playerMated && !board.isKingInCheck(1,board.getBoardArray())){return 0;}
        if(botMated) return -999999999;
        if(playerMated) return 999999999;
        return score;
    }
    public Move calcBestMove(Board board, int currentSide, int ply){
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        ArrayList<Move> allLegalMoves = new ArrayList<>(getAllLegalMoves(board, currentSide));
        Move bestMove = allLegalMoves.getFirst();
        double highestScore = Double.NEGATIVE_INFINITY;
        //simulate doing each move than check evalScore
        for(int i = 0; i< allLegalMoves.size(); i++){
            int startX = allLegalMoves.get(i).startX;
            int startY = allLegalMoves.get(i).startY;
            int endX = allLegalMoves.get(i).endX;
            int endY = allLegalMoves.get(i).endY;
            Board boardCopy = new Board(true,board);
            ChessPiece[][] boardCopyArray = boardCopy.getBoardArray();
            clearDoubleMoveFlags(boardCopy);
            //simulate moving the piece
            //did you en Passant
            if(boardCopyArray[startX][startY] instanceof Pawn && startX!=endX && boardCopyArray[endX][endY] == null){
                    boardCopyArray[endX][startY] = null;
            }
            //did you castle left
            if(boardCopyArray[startX][startY] instanceof King && startX-endX == 2){
                boardCopyArray[endX+1][endY] = boardCopyArray[0][endY];
                boardCopyArray[0][endY] = null;
                boardCopyArray[endX+1][endY].setX(endX+1);
                ((Rook) boardCopyArray[endX+1][endY]).hasMoved = true;
            }
            //did you castle right
            if(boardCopyArray[startX][startY] instanceof King && startX-endX == -2){
                boardCopyArray[endX-1][endY] = boardCopyArray[7][endY];
                boardCopyArray[7][endY] = null;
                boardCopyArray[endX-1][endY].setX(endX-1);
                ((Rook) boardCopyArray[endX-1][endY]).hasMoved = true;
            }
                boardCopyArray[endX][endY] = boardCopyArray[startX][startY];
                boardCopyArray[startX][startY] = null;
                boardCopyArray[endX][endY].setX(endX);
                boardCopyArray[endX][endY].setY(endY);
                //pawn unique handling
            if (boardCopyArray[endX][endY] instanceof Pawn pawn) {
                if (Math.abs(endY - startY) == 2) {
                    pawn.justDoubledMoved = true;
                }
                pawn.isFirstMove = false;
            }
            //king and rook unique handling
            if(boardCopyArray[endX][endY] instanceof King) {((King) boardCopyArray[endX][endY]).hasMoved = true;}
            if(boardCopyArray[endX][endY] instanceof Rook) {((Rook) boardCopyArray[endX][endY]).hasMoved = true;}
                //check to see move rating
            double thisScore = minimaxScore(boardCopy, ply - 1, -currentSide, alpha, beta);
            if(thisScore > highestScore){
                highestScore = thisScore;
                alpha = Math.max(alpha, highestScore);
                bestMove = allLegalMoves.get(i);
            }
        }
        return bestMove;
    }
    public ArrayList<Move> getAllLegalMoves(Board board, int currentSide){
        ArrayList<Move> allLegalMoves = new ArrayList<>();
        ChessPiece[][] boardArray = board.getBoardArray();
        for(int r = 0; r<8; r++){
            for(int c = 0; c<8; c++){
                if(boardArray[r][c] != null && boardArray[r][c].getSide() == currentSide) {
                    ArrayList<Positions> piecePossibleMoves = board.getLegalMoves(boardArray[r][c]);
                    for (int i = 0; i<piecePossibleMoves.size();i++) {
                        Positions movePos = piecePossibleMoves.get(i);
                        Move move = new Move(r, c, movePos.x, movePos.y);
                        //checks if the move takes to do faster alpha beta pruning
                        if (boardArray[movePos.x][movePos.y] != null) {
                            allLegalMoves.addFirst(move);
                        }
                        else {
                            allLegalMoves.add(move);
                        }
                    }
                }
            }
        }
        return allLegalMoves;
    }
    public void clearDoubleMoveFlags(Board board) {
        ChessPiece[][] arr = board.getBoardArray();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (arr[r][c] instanceof Pawn) {
                    ((Pawn) arr[r][c]).justDoubledMoved = false;
                }
            }
        }
    }
    public double minimaxScore(Board board, int ply, int currentSide, double alpha, double beta) {
        if (ply == 0) {
            return evalScore(board);
        }

        ArrayList<Move> allLegalMoves = getAllLegalMoves(board, currentSide);

        if (allLegalMoves.isEmpty()) {
            return evalScore(board);
        }

        if (currentSide == side) {
            double bestScore = Double.NEGATIVE_INFINITY;

            for (Move move : allLegalMoves) {
                int startX = move.startX;
                int startY = move.startY;
                int endX = move.endX;
                int endY = move.endY;

                Board boardCopy = new Board(true, board);
                ChessPiece[][] boardCopyArray = boardCopy.getBoardArray();
                clearDoubleMoveFlags(boardCopy);

                if (boardCopyArray[startX][startY] instanceof Pawn && startX != endX && boardCopyArray[endX][endY] == null) {
                    boardCopyArray[endX][startY] = null;
                }

                if (boardCopyArray[startX][startY] instanceof King && startX - endX == 2) {
                    boardCopyArray[endX + 1][endY] = boardCopyArray[0][endY];
                    boardCopyArray[0][endY] = null;
                    boardCopyArray[endX + 1][endY].setX(endX + 1);
                    boardCopyArray[endX + 1][endY].setY(endY);
                    ((Rook) boardCopyArray[endX + 1][endY]).hasMoved = true;
                }

                if (boardCopyArray[startX][startY] instanceof King && startX - endX == -2) {
                    boardCopyArray[endX - 1][endY] = boardCopyArray[7][endY];
                    boardCopyArray[7][endY] = null;
                    boardCopyArray[endX - 1][endY].setX(endX - 1);
                    boardCopyArray[endX - 1][endY].setY(endY);
                    ((Rook) boardCopyArray[endX - 1][endY]).hasMoved = true;
                }

                boardCopyArray[endX][endY] = boardCopyArray[startX][startY];
                boardCopyArray[startX][startY] = null;
                boardCopyArray[endX][endY].setX(endX);
                boardCopyArray[endX][endY].setY(endY);

                if (boardCopyArray[endX][endY] instanceof Pawn pawn) {
                    if (Math.abs(endY - startY) == 2) {
                        pawn.justDoubledMoved = true;
                    }
                    pawn.isFirstMove = false;
                }

                if (boardCopyArray[endX][endY] instanceof King) {
                    ((King) boardCopyArray[endX][endY]).hasMoved = true;
                }

                if (boardCopyArray[endX][endY] instanceof Rook) {
                    ((Rook) boardCopyArray[endX][endY]).hasMoved = true;
                }

                double thisScore = minimaxScore(boardCopy, ply - 1, -currentSide, alpha, beta);

                if (thisScore > bestScore) {
                    bestScore = thisScore;
                    alpha = Math.max(bestScore, alpha);
                }
                if(alpha>=beta) break;
            }

            return bestScore;
        } else {
            double bestScore = Double.POSITIVE_INFINITY;

            for (Move move : allLegalMoves) {
                int startX = move.startX;
                int startY = move.startY;
                int endX = move.endX;
                int endY = move.endY;

                Board boardCopy = new Board(true, board);
                ChessPiece[][] boardCopyArray = boardCopy.getBoardArray();
                clearDoubleMoveFlags(boardCopy);

                if (boardCopyArray[startX][startY] instanceof Pawn && startX != endX && boardCopyArray[endX][endY] == null) {
                    boardCopyArray[endX][startY] = null;
                }

                if (boardCopyArray[startX][startY] instanceof King && startX - endX == 2) {
                    boardCopyArray[endX + 1][endY] = boardCopyArray[0][endY];
                    boardCopyArray[0][endY] = null;
                    boardCopyArray[endX + 1][endY].setX(endX + 1);
                    boardCopyArray[endX + 1][endY].setY(endY);
                    ((Rook) boardCopyArray[endX + 1][endY]).hasMoved = true;
                }

                if (boardCopyArray[startX][startY] instanceof King && startX - endX == -2) {
                    boardCopyArray[endX - 1][endY] = boardCopyArray[7][endY];
                    boardCopyArray[7][endY] = null;
                    boardCopyArray[endX - 1][endY].setX(endX - 1);
                    boardCopyArray[endX - 1][endY].setY(endY);
                    ((Rook) boardCopyArray[endX - 1][endY]).hasMoved = true;
                }

                boardCopyArray[endX][endY] = boardCopyArray[startX][startY];
                boardCopyArray[startX][startY] = null;
                boardCopyArray[endX][endY].setX(endX);
                boardCopyArray[endX][endY].setY(endY);

                if (boardCopyArray[endX][endY] instanceof Pawn pawn) {
                    if (Math.abs(endY - startY) == 2) {
                        pawn.justDoubledMoved = true;
                    }
                    pawn.isFirstMove = false;
                }

                if (boardCopyArray[endX][endY] instanceof King) {
                    ((King) boardCopyArray[endX][endY]).hasMoved = true;
                }

                if (boardCopyArray[endX][endY] instanceof Rook) {
                    ((Rook) boardCopyArray[endX][endY]).hasMoved = true;
                }

                double thisScore = minimaxScore(boardCopy, ply - 1, -currentSide, alpha, beta);

                if (thisScore < bestScore) {
                    bestScore = thisScore;
                    beta = Math.min(bestScore, beta);
                }
                if(alpha>=beta) break;
            }

            return bestScore;
        }
    }
    public boolean[][] getHitMap(int side, ChessPiece[][] boardArray){
        boolean [][] hitMap = new boolean[8][8];
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(boardArray[r][c]!=null && boardArray[r][c].getSide()==side){
                    ChessPiece piece = boardArray[r][c];
                    ArrayList<Positions> pieceHitSquares = piece.getAttackingMoves();
                    for (Positions pos : pieceHitSquares) {
                           if(pos.x<8 && pos.y<8 && pos.x>-1 && pos.y>-1) hitMap[pos.x][pos.y] = true;
                    }
                }
            }
        }
        return hitMap;
    }
}
