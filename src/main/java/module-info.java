module itc.celaya.juegosubmarinos {
    requires javafx.controls;
    requires javafx.fxml;


    opens itc.celaya.juegosubmarinos to javafx.fxml;
    exports itc.celaya.juegosubmarinos;
}