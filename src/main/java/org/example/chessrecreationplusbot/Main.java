package org.example.chessrecreationplusbot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main extends Application {
    // *IMPORTANT* adjusting the ply downwards makes the bot much faster byt also worse, adjusting it upwards makes it way slower but stronger
    int ply = 6;
    static boolean botHasntMoved = true;
    int currentTurn = 1;
    boolean inCheck = false;
    ChessPiece[][] boardArray = new  ChessPiece[8][8];
    Board board = new Board(boardArray);
    ChessPiece selectedPiece;
    StackPane[][] squares = new StackPane[8][8];
    Image lightPawn = new Image(Objects.requireNonNull(getClass().getResource("/LightPawn.png")).toExternalForm());
    Image darkPawn = new Image(Objects.requireNonNull(getClass().getResource("/DarkPawn.png")).toExternalForm());
    Image darkKnight = new Image(Objects.requireNonNull(getClass().getResource("/DarkKnight.png")).toExternalForm());
    Image lightKnight = new Image(Objects.requireNonNull(getClass().getResource("/LightKnight.png")).toExternalForm());
    Image darkBishop = new Image(Objects.requireNonNull(getClass().getResource("/DarkBishop.png")).toExternalForm());
    Image lightBishop = new Image(Objects.requireNonNull(getClass().getResource("/LightBishop.png")).toExternalForm());
    Image darkRook = new Image(Objects.requireNonNull(getClass().getResource("/DarkRook.png")).toExternalForm());
    Image lightRook = new Image(Objects.requireNonNull(getClass().getResource("/LightRook.png")).toExternalForm());
    Image lightQueen = new Image(Objects.requireNonNull(getClass().getResource("/LightQueen.png")).toExternalForm());
    Image darkQueen = new Image(Objects.requireNonNull(getClass().getResource("/DarkQueen.png")).toExternalForm());
    Image lightKing = new Image(Objects.requireNonNull(getClass().getResource("/LightKing.png")).toExternalForm());
    Image darkKing = new Image(Objects.requireNonNull(getClass().getResource("/DarkKing.png")).toExternalForm());
    Scanner scan = new Scanner(System.in);
    public static String playerColor;
    Label gameMessage = new Label();

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Which color are you playing? (white or black)");
        playerColor = scan.nextLine();
        GridPane chessBoard = new GridPane();
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                StackPane square = new StackPane();
                int x = c;
                int y = r;

                square.setOnMouseClicked(event -> {
                    handleSquareClick(x, y);
                });

                squares[x][y] = square;

                squares[c][r] = square;
                Rectangle tile  = new Rectangle(80,80);
                if((r+c)%2 == 0) tile.setFill(Color.BEIGE);
                else tile.setFill(Color.rgb(181, 136, 99));
                square.getChildren().add(tile);
                chessBoard.add(square, c, r);
            }
        }
        setPieces();
        gameMessage.setStyle(
                "-fx-font-size: 70px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: black; "
        );
        BorderPane root = new BorderPane();
        StackPane boardContainer = new StackPane();
        root.setCenter(boardContainer);
        boardContainer.getChildren().add(chessBoard);
        gameMessage.setText("");
        boardContainer.getChildren().add(gameMessage);
        StackPane.setAlignment(gameMessage, Pos.CENTER);
        Scene scene = new Scene(root, 640, 640);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Chess");

        if (playerColor.equals("black")) {
            currentTurn = -1;
            triggerBotMove();
        }
        redrawBoard();
        stage.show();

    }
    public void handleSquareClick(int x, int y) {
        //for when the bot is ready
        ChessPiece clickedSpot = boardArray[x][y];
        //clicked something for first time
        if(selectedPiece == null){
            if(clickedSpot != null && clickedSpot.getSide()==currentTurn){
                selectedPiece = clickedSpot;
                colorSquares(selectedPiece);
            }
            return;
        }
        if(clickedSpot != null && clickedSpot.getSide()==currentTurn){
                selectedPiece = clickedSpot;
                redrawBoard();
                colorSquares(selectedPiece);
            return;
        }
        //clicked empty square or enemy with piece selected
            ArrayList<Positions> possibleMoves = board.getLegalMoves(selectedPiece);
            for(int i = 0; i < possibleMoves.size(); i++) {
                Positions move = possibleMoves.get(i);
                if(move.x == x && move.y == y){
                    //resets justDoubleMoved
                    resetPawns();
                    //handles unique pawn movement
                    if(selectedPiece instanceof Pawn) {
                        ((Pawn) selectedPiece).isFirstMove = false;
                        if(Math.abs(selectedPiece.getY() - y) == 2) {
                            ((Pawn) selectedPiece).justDoubledMoved = true;
                        }
                    }
                    //keep track of king and rook movement for castling
                    if(selectedPiece instanceof Rook) {((Rook)selectedPiece).hasMoved = true;}
                    if(selectedPiece instanceof King) {((King)selectedPiece).hasMoved = true;}
                    // en passant takes
                    if(selectedPiece instanceof Pawn && boardArray[x][y] == null && boardArray[x][y+selectedPiece.getSide()] instanceof Pawn) {
                        boardArray[x][y+selectedPiece.getSide()] = null;
                    }
                    //move the rook when castling
                    if(selectedPiece instanceof King && selectedPiece.getX() - x == 2){
                        boardArray[x+1][y] = boardArray[0][y];
                        boardArray[x+1][y].setX(x+1);
                        boardArray[0][y] = null;
                    }
                    if(selectedPiece instanceof King && selectedPiece.getX() - x == -2){
                        boardArray[x-1][y] = boardArray[7][y];
                        boardArray[x-1][y].setX(x-1);
                        boardArray[7][y] = null;
                    }
                    //move piece
                    boardArray[selectedPiece.getX()][selectedPiece.getY()] = null;
                    boardArray[x][y] = selectedPiece;
                    selectedPiece.setX(x);
                    selectedPiece.setY(y);
                    selectedPiece = null;
                    inCheck = board.isKingInCheck(currentTurn, boardArray);
                    board.promotePawns();
                    currentTurn *= -1;
                    redrawBoard();

                    if (currentTurn == -1) {
                        triggerBotMove();
                    }

                    return;
                }
            }
    }

    public void triggerBotMove() {
        new Thread(() -> {
            AI ai = new AI();
            Move botMove = ai.minimax(board, ply);
            Platform.runLater(() -> {
                if (botMove != null && currentTurn == -1) {
                    applyMove(botMove);
                    inCheck = board.isKingInCheck(currentTurn, boardArray);
                    board.promotePawns();
                    currentTurn *= -1;
                }
                redrawBoard();
            });
        }).start();
    }
    public void applyMove(Move move) {
        resetPawns();
        ChessPiece movingPiece = boardArray[move.startX][move.startY];
        if (movingPiece == null) {
            return;
        }
        if (movingPiece instanceof Pawn) {
            ((Pawn) movingPiece).isFirstMove = false;
            if (Math.abs(movingPiece.getY() - move.endY) == 2) {
                ((Pawn) movingPiece).justDoubledMoved = true;
            }
        }
        if (movingPiece instanceof Rook) {
            ((Rook) movingPiece).hasMoved = true;
        }
        if (movingPiece instanceof King) {
            ((King) movingPiece).hasMoved = true;
        }
        if (movingPiece instanceof Pawn &&
                boardArray[move.endX][move.endY] == null &&
                move.endY + movingPiece.getSide() >= 0 &&
                move.endY + movingPiece.getSide() < 8 &&
                boardArray[move.endX][move.endY + movingPiece.getSide()] instanceof Pawn) {
            boardArray[move.endX][move.endY + movingPiece.getSide()] = null;
        }
        if (movingPiece instanceof King && movingPiece.getX() - move.endX == 2) {
            boardArray[move.endX + 1][move.endY] = boardArray[0][move.endY];
            boardArray[move.endX + 1][move.endY].setX(move.endX + 1);
            boardArray[0][move.endY] = null;
        }
        if (movingPiece instanceof King && movingPiece.getX() - move.endX == -2) {
            boardArray[move.endX - 1][move.endY] = boardArray[7][move.endY];
            boardArray[move.endX - 1][move.endY].setX(move.endX - 1);
            boardArray[7][move.endY] = null;
        }
        boardArray[movingPiece.getX()][movingPiece.getY()] = null;
        boardArray[move.endX][move.endY] = movingPiece;
        movingPiece.setX(move.endX);
        movingPiece.setY(move.endY);
    }
    public void redrawBoard(){
        isGameOver();
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                StackPane square = squares[x][y];
                square.getChildren().clear();

                Rectangle tile = new Rectangle(80,80);

                    if ((x + y) % 2 == 0) tile.setFill(Color.BEIGE);
                    else tile.setFill(Color.rgb(181, 136, 99));


                square.getChildren().add(tile);

                ChessPiece piece = boardArray[x][y];
                if(piece != null){
                    Image pieceImage = null;
                    if(playerColor.equals("white")) {
                        if (piece instanceof Pawn) pieceImage = (piece.getSide() == 1) ? lightPawn : darkPawn;
                        else if (piece instanceof Knight)
                            pieceImage = (piece.getSide() == 1) ? lightKnight : darkKnight;
                        else if (piece instanceof Bishop)
                            pieceImage = (piece.getSide() == 1) ? lightBishop : darkBishop;
                        else if (piece instanceof Rook) pieceImage = (piece.getSide() == 1) ? lightRook : darkRook;
                        else if (piece instanceof Queen) pieceImage = (piece.getSide() == 1) ? lightQueen : darkQueen;
                        else if (piece instanceof King) pieceImage = (piece.getSide() == 1) ? lightKing : darkKing;
                    } else{
                        if (piece instanceof Pawn) pieceImage = (piece.getSide() == -1) ? lightPawn : darkPawn;
                        else if (piece instanceof Knight)
                            pieceImage = (piece.getSide() == -1) ? lightKnight : darkKnight;
                        else if (piece instanceof Bishop)
                            pieceImage = (piece.getSide() == -1) ? lightBishop : darkBishop;
                        else if (piece instanceof Rook) pieceImage = (piece.getSide() == -1) ? lightRook : darkRook;
                        else if (piece instanceof Queen) pieceImage = (piece.getSide() == -1) ? lightQueen : darkQueen;
                        else if (piece instanceof King) pieceImage = (piece.getSide() == -1) ? lightKing : darkKing;
                    }
                    if(pieceImage != null){
                        ImageView pieceView = new ImageView(pieceImage);
                        pieceView.setFitWidth(80);
                        pieceView.setFitHeight(80);
                        square.getChildren().add(pieceView);
                    }
                }
            }
        }
    }
    public void setPieces(){
        //x and y are flipped so if there are issues its because of this
        if(playerColor.equals("white")) {
            initPiece(new Rook(0, 7, 1, board), "LightRook");
            initPiece(new Rook(7, 7, 1, board), "LightRook");
            initPiece(new Knight(1, 7, 1, board), "LightKnight");
            initPiece(new Knight(6, 7, 1, board), "LightKnight");
            initPiece(new Bishop(2, 7, 1, board), "LightBishop");
            initPiece(new Bishop(5, 7, 1, board), "LightBishop");
            initPiece(new Queen(3, 7, 1, board), "LightQueen");
            initPiece(new King(4, 7, 1, board), "LightKing");

            initPiece(new Rook(0, 0, -1, board), "DarkRook");
            initPiece(new Rook(7, 0, -1, board), "DarkRook");
            initPiece(new Knight(1, 0, -1, board), "DarkKnight");
            initPiece(new Knight(6, 0, -1, board), "DarkKnight");
            initPiece(new Bishop(2, 0, -1, board), "DarkBishop");
            initPiece(new Bishop(5, 0, -1, board), "DarkBishop");
            initPiece(new Queen(3, 0, -1, board), "DarkQueen");
            initPiece(new King(4, 0, -1, board), "DarkKing");
            for(int c = 0; c < 8; c++){
                initPiece(new Pawn(c,6,1,board), "LightPawn");
                initPiece(new Pawn(c,1,-1,board), "DarkPawn");
            }
        } else{
                initPiece(new Rook(0, 7, 1, board), "DarkRook");
                initPiece(new Rook(7, 7, 1, board), "DarkRook");
                initPiece(new Knight(1, 7, 1, board), "DarkKnight");
                initPiece(new Knight(6, 7, 1, board), "DarkKnight");
                initPiece(new Bishop(2, 7, 1, board), "DarkBishop");
                initPiece(new Bishop(5, 7, 1, board), "DarkBishop");
                initPiece(new Queen(4, 7, 1, board), "DarkQueen");
                initPiece(new King(3, 7, 1, board), "DarkKing");

                initPiece(new Rook(0, 0, -1, board), "LightRook");
                initPiece(new Rook(7, 0, -1, board), "LightRook");
                initPiece(new Knight(1, 0, -1, board), "LightKnight");
                initPiece(new Knight(6, 0, -1, board), "LightKnight");
                initPiece(new Bishop(2, 0, -1, board), "LightBishop");
                initPiece(new Bishop(5, 0, -1, board), "LightBishop");
                initPiece(new Queen(4, 0, -1, board), "LightQueen");
                initPiece(new King(3, 0, -1, board), "LightKing");
            for(int c = 0; c < 8; c++){
                initPiece(new Pawn(c,6,1,board), "DarkPawn");
                initPiece(new Pawn(c,1,-1,board), "LightPawn");
            }
        }
    }
    public void initPiece(ChessPiece piece, String pieceName){
        boardArray[piece.getX()][piece.getY()] = piece;
        Image pieceImage = switch (pieceName) {
            case "LightPawn" ->  lightPawn;
            case "DarkPawn" ->  darkPawn;
            case "LightKnight" ->  lightKnight;
            case "DarkKnight" ->  darkKnight;
            case "DarkBishop" ->  darkBishop;
            case "LightBishop" ->  lightBishop;
            case "DarkQueen" ->  darkQueen;
            case "DarkKing" ->  darkKing;
            case "DarkRook" ->  darkRook;
            case "LightRook" ->  lightRook;
            case "LightKing" ->  lightKing;
            case "LightQueen" ->  lightQueen;
            default -> null;
        };

        ImageView pieceView = new ImageView(pieceImage);
        pieceView.setFitWidth(80);
        pieceView.setFitHeight(80);
        squares[piece.getX()][piece.getY()].getChildren().add(pieceView);
    }
    public void colorSquares(ChessPiece piece){
        for (Positions move : board.getLegalMoves(piece)) {
            Circle dot = new Circle(8);
            dot.setFill(Color.color(0, 0, 0, 0.3)); // semi transparent
            squares[move.x][move.y].getChildren().add(dot);
        }
    }
    public void resetPawns(){
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardArray[x][y] instanceof Pawn) {
                    ((Pawn) boardArray[x][y]).justDoubledMoved = false;
                }
            }
        }
    }

    public void isGameOver(){
        if(board.isKingInCheck(currentTurn, boardArray)){
            for(int r = 0 ; r < 8; r++){
                for(int c = 0 ; c < 8; c++){
                    if(boardArray[r][c]!=null && boardArray[r][c].getSide() == currentTurn && !board.getLegalMoves(boardArray[r][c]).isEmpty()){
                        return;
                    }
                }
            }
            gameMessage.setText("Game Over");
        }
    }
}
