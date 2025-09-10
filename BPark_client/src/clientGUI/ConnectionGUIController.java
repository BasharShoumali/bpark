package clientGUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.BParkClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConnectionGUIController implements Initializable {
	private String server_connection_data;

	@FXML
	private Button connectBtn;

	@FXML
	private Label txtId;

	@FXML
	private Button exitBtn;

	@FXML
	private TextField inputField;

	public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.<Parent>load(getClass().getResource("/clientGUI/connectionGUI.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Client Connect");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	@FXML
	  void ExitBtn(ActionEvent event) throws Exception {
		if (ClientUI.bParkClient != null && ClientUI.bParkClient.isConnected()) {
	        ClientUI.bParkClient.quit(); // This will now notify server

	    } else {
	        System.exit(0);
	    }
	  }
	
	@FXML
	void ConnectBtn(ActionEvent event) {
	    this.server_connection_data = inputField.getText();
	    System.out.println(server_connection_data);
	    try {
	        if (ClientUI.bParkClient != null && ClientUI.bParkClient.isConnected()) {
	            // Already connected, no need to reconnect
	            navigateToHomePage(event);
	            return;
	        }
	        
	        ClientUI.bParkClient = BParkClient.getInstance(this.server_connection_data, 5555);
	        navigateToHomePage(event);
	        ClientUI.bParkClient.requestFromServer("Connect");
	    } catch (IOException e) {
	        showConnectionError();
	    }
	}

	private void navigateToHomePage(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientGUI/HomePageGUI.fxml"));
	    Pane root = loader.load();
	    ((Node)event.getSource()).getScene().getWindow().hide();
	    Stage primaryStage = new Stage();
	    primaryStage.setTitle("Client Home Page");
	    primaryStage.setScene(new Scene(root));
	    primaryStage.show();
	}

	private void showConnectionError() {
	    inputField.setStyle("-fx-text-fill: red;");
	    inputField.setText("❌ Connection Failed");
	    System.err.println("Connection Error");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 this.inputField.setText("localhost");		
	}
	
	
}
