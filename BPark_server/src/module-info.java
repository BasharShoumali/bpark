module BPark_server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	requires javafx.graphics;

    opens server to javafx.fxml;
    opens serverGUI to javafx.fxml;

    exports server;
    exports serverGUI;
}
