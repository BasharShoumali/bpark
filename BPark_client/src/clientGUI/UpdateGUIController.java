package clientGUI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UpdateGUIController {

	ConnectionGUIController connectionGUIController;

	@FXML
	private Label lblServerMessage;

	@FXML
	private Button parkingSpaceBtn;

	@FXML
	private Button newOrderDateBtn;

	@FXML
	private Button exitBtn1;

	@FXML
	private Button exitBtn;

	@FXML
	private TextField subscriberNumberInput;

	@FXML
	private TextField parkingSpaceOrderNumberInput;

	@FXML
	private TextField parkingSpaceInput;

	@FXML
	private TextField subscriberNumberInput1;

	@FXML
	private TextField orderDateOrderNumberInput;

	@FXML
	private DatePicker newOrderDateInput;

	private String serverMessage;

	@FXML
	void ExitBtn(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientGUI/HomePageGUI.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Connect Page");
			stage.centerOnScreen();
			stage.show();
			System.out.println("moved from Update Window GUI to main GUI");// notation line
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading Connect Page: " + e.getMessage());
		}
	}

	@FXML
	void ParkingSpaceBtn(ActionEvent event) throws Exception {
		String subscriberNumber = this.subscriberNumberInput.getText();
		String orderNumber = this.parkingSpaceOrderNumberInput.getText();
		String parkingSpace = this.parkingSpaceInput.getText();
		if (!subscriberNumber.isEmpty() && orderNumber != null && parkingSpace != null) {
			List<String> updateDetails = List.of(subscriberNumber, orderNumber, parkingSpace);
			if (ClientUI.bParkClient != null) {
				ClientUI.bParkClient.setUpdateController(this);
				ClientUI.bParkClient.requestFromServer(updateDetails);
			} else {
				inputResponse("Not connected to server");
			}
		} else {
			inputResponse("Please fill the fields.");
		}
	}
	
	@FXML
	void NewOrderDateInputBtn(ActionEvent event) throws Exception {
		String subscriberNumber = this.subscriberNumberInput1.getText();
		String orderNumber = this.orderDateOrderNumberInput.getText();
		LocalDate parkingDate = newOrderDateInput.getValue();
		if (parkingDate != null) {
	        System.out.println("Sending to server: " + subscriberNumber+ " " + orderNumber + " " + parkingDate);
	    } else {
	        System.out.println("No date selected.");
	    }
	
		if (!subscriberNumber.isEmpty() && orderNumber != null && parkingDate != null) {
			ArrayList<String> updateDetails = new ArrayList<>();
			updateDetails.add(subscriberNumber);
			updateDetails.add(orderNumber);
			updateDetails.add(parkingDate.toString());

			if (ClientUI.bParkClient != null) {
				ClientUI.bParkClient.setUpdateController(this);
				ClientUI.bParkClient.requestFromServer(updateDetails);
			} else {
				inputResponse("Not connected to server");
			}
		} else {
			inputResponse(
					"Please fill the fields.");
		}
	}
	public void inputResponse(String response) {
		this.serverMessage = response;
		Platform.runLater(() -> {
			this.lblServerMessage.setText(this.serverMessage);
			if ("Subscriber Successfully Updated".equals(this.serverMessage)) {
				this.lblServerMessage.setTextFill(Color.GREEN);
			} else {
				this.lblServerMessage.setTextFill(Color.RED);
			}
		});
	}
}
