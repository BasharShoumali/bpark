package clientGUI;

import client.ClientUI;
import java.util.ArrayList;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DisplayGUIController {
	@FXML
	private Button exitBtn;

	@FXML
	private Button searchBtn;

	@FXML
	private TextField inputTxt;

	@FXML
	private TableView<Order> OrdersTable;

	@FXML
	private TableColumn<Order, Integer> parkingSpace;

	@FXML
	private TableColumn<Order, Integer> orderNumber;

	@FXML
	private TableColumn<Order, String> orderDate;

	@FXML
	private TableColumn<Order, Integer> confirmationCode;

	@FXML
	private TableColumn<Order, Integer> subscriberID;

	@FXML
	private TableColumn<Order, String> dateOfPlacingAnOrder;

	private ObservableList<Order> OrderData = FXCollections.observableArrayList();
	@FXML
	private Label lblServerMessage;
	
	private String serverMessage;

	@FXML
	public void initialize() {
		this.parkingSpace.setCellValueFactory(new PropertyValueFactory<>("parking_space"));
		this.orderNumber.setCellValueFactory(new PropertyValueFactory<>("order_number"));
		this.orderDate.setCellValueFactory(new PropertyValueFactory<>("order_date"));
		this.confirmationCode.setCellValueFactory(new PropertyValueFactory<>("confirmation_code"));
		this.subscriberID.setCellValueFactory(new PropertyValueFactory<>("subscriberNumber"));
		this.dateOfPlacingAnOrder.setCellValueFactory(new PropertyValueFactory<>("date_of_placing_an_order"));
		this.OrdersTable.setItems(this.OrderData);
		if (ClientUI.bParkClient != null) {
			ClientUI.bParkClient.setDisplayController(this);
			ClientUI.bParkClient.requestFromServer("Display");
		}
	}

	public void setSubscriberData(ArrayList<Order> order) {
		this.OrderData.setAll(order);
	}

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
			System.out.println("Moved from Display Subscriber Orders GUI to Main GUI"); // Debug line
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading Connect Page: " + e.getMessage());
		}
	}

	@FXML
	void SearchBtn() {
		
		String subscriberNumber = this.inputTxt.getText();
		String[] subscriberArray = new String[1];

		if(subscriberNumber.isEmpty()) {
			subscriberArray[0] = "All";
			inputResponse("Showing All Orders!");

		}
		else {
			subscriberArray[0] = subscriberNumber;
		}

		if (!subscriberArray[0].isEmpty()) {
			if (ClientUI.bParkClient != null) {
				ClientUI.bParkClient.setDisplayController(this);
				ClientUI.bParkClient.requestFromServer(subscriberArray);
			} else {
				inputResponse("Not connected to server");
			}
		} else

		{
			System.out.println(subscriberNumber);
			inputResponse("Please fill the fields.");
		}
		// System.out.println("No match found for Subscription Number: " + Find_id);
	}

	public void Display(String msg) {
	    System.out.println(msg.toString());

	    Platform.runLater(() -> {
	        this.OrderData.clear();

	        // Trim square brackets
	        String newmsg = msg.substring(1, msg.length() - 1); // remove [ and ]
	        String[] entries = newmsg.split("\\}, \\{"); // split into each map

	        for (String entry : entries) {
	            try {
	                // Clean up each entry
	                entry = entry.replaceAll("^\\{", "").replaceAll("\\}$", "");
	                String[] pairs = entry.split(", ");
	                
	                // Initialize values
	                int parkingSpace = 0, orderNumber = 0, confirmationCode = 0, subscriberNumber = 0;
	                String orderDate = "", dateOfPlacingAnOrder = "";

	                for (String pair : pairs) {
	                    String[] kv = pair.split("=", 2);
	                    if (kv.length != 2) continue;

	                    String key = kv[0].trim();
	                    String value = kv[1].trim();

	                    switch (key) {
	                        case "parking_space": parkingSpace = Integer.parseInt(value); break;
	                        case "order_number": orderNumber = Integer.parseInt(value); break;
	                        case "confirmation_code": confirmationCode = Integer.parseInt(value); break;
	                        case "subscriberNumber": subscriberNumber = Integer.parseInt(value); break;
	                        case "order_date": orderDate = value; break;
	                        case "date_of_placing_an_order": dateOfPlacingAnOrder = value; break;
	                    }
	                }

	                Order newOrder = new Order(parkingSpace, orderNumber, orderDate,
	                        confirmationCode, subscriberNumber, dateOfPlacingAnOrder);

	                this.OrderData.add(newOrder);

	            } catch (Exception e) {
	                System.out.println("❌ Error parsing entry: " + entry);
	                e.printStackTrace();
	            }
	        }
	    });
	}





	public void inputResponse(String response) {
		this.serverMessage = response;
		Platform.runLater(() -> {
			this.lblServerMessage.setText(this.serverMessage);
			if ("Order Successfully Updated".equals(this.serverMessage)) {
				this.lblServerMessage.setTextFill(Color.GREEN);
			} else {
				this.lblServerMessage.setTextFill(Color.RED);
			}
		});
	}
	
	public String getServerMessage() {
		return serverMessage;
	}

	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}
}
