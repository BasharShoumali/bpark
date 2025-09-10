
module BPark_client {
	requires javafx.fxml;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;

	exports client;
	exports clientGUI;
	opens clientGUI to javafx.fxml; // ← This line fixes your error

}