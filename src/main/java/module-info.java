module org.example.chessrecreationplusbot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.chessrecreationplusbot to javafx.fxml;
    exports org.example.chessrecreationplusbot;
}