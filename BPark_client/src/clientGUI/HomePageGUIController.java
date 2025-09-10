package clientGUI;

import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomePageGUIController {

	@FXML
	private String serverMessage;

	@FXML
	private Label lblServerMessage;

	@FXML
	void UpdateDetailsBtn(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientGUI/UpdateGUI.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Update Page");
			stage.centerOnScreen();
			stage.show();
			System.out.println("moved from HomePage GUI to Update GUI");// notation line
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading Connect Page: " + e.getMessage());
		}
	}

	@FXML
	  void ExitBtn(ActionEvent event) throws Exception {
		if (ClientUI.bParkClient != null && ClientUI.bParkClient.isConnected()) {
	        ClientUI.bParkClient.requestFromServer("DisConnect");
	        ClientUI.bParkClient.quit(); // Thigs will now notify server

	    } else {
	        System.exit(0);
	    }
	  }
	

	@FXML
	void DisplayDetailsBtn(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientGUI/DisplayGUI.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Display Orders Page");
			stage.centerOnScreen();
			stage.show();
			System.out.println("moved from HomePage GUI to Display Orders GUI");// notation line
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading Connect Page: " + e.getMessage());
		}
	}

	public void inputResponse(String response) {
		this.serverMessage = response;
		Platform.runLater(() -> this.lblServerMessage.setText(this.serverMessage));
	}

	public void ShowConnection() {
		ClientUI.bParkClient.setHomePageController(this);
		ClientUI.bParkClient.requestFromServer("ShowConnection");
	}

}
