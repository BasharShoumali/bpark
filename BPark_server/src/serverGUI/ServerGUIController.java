package serverGUI;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import ocsf.server.ConnectionToClient;
import server.BParkServer;
import server.ServerUI;

public class ServerGUIController implements Initializable {
	private static ServerGUIController instance;

	@FXML
	private Button connectBtn;

	@FXML
	private Button disconnectBtn;

	@FXML
	private Button closeBtn;

	@FXML
	private TextField portTextFiled;

	@FXML
	private TextField dbNameTextField;

	@FXML
	private TextField dbUserTextField;

	@FXML
	private TextField dbPasswordTextField;

	@FXML
	private Label Server_Configuration;

	@FXML
	private Label ip_label;

	@FXML
	private Label port_label;

	@FXML
	private Label DB_label;

	@FXML
	private Label DBuser_label;

	@FXML
	private Label DBpassword_label;

	@FXML
	private Label serverStatusLabel;

	@FXML
	private Label serverIPTextField;

	@FXML
	private TableView<ClientConnInfo> clientTable;

	@FXML
	private TableColumn<ClientConnInfo, String> hostNameColumn;

	@FXML
	private TableColumn<ClientConnInfo, String> ipAddressColumn;

	@FXML
	private TableColumn<ClientConnInfo, String> statusColumn;

	private static ObservableList<ClientConnInfo> clientData;

	public ServerGUIController() {
		this.connectBtn = null;
		this.disconnectBtn = null;
		this.closeBtn = null;	
		instance = this;

	}

	public static ServerGUIController getInstance() {
	    if (instance == null) {
	        System.out.println("ServerGUIController instance is null!");
	    }
	    return instance;
	}


	public void initialize(URL url, ResourceBundle resourceBundle) {
		this.portTextFiled.setText("5555");
		this.dbNameTextField.setText("bpark");
		this.dbUserTextField.setText("root");
		this.dbPasswordTextField.setText("227427Bashar@");
		try {
			String ipAddress = InetAddress.getLocalHost().getHostAddress();
			//String hostName = InetAddress.getLocalHost().getHostName();
			this.serverIPTextField.setText(ipAddress);
			this.hostNameColumn.setCellValueFactory(new PropertyValueFactory<>("hostName"));
			this.ipAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
			this.statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
			clientData = FXCollections.observableArrayList();
			// Create and add a new connection info object
			//ClientConnInfo conn = new ClientConnInfo(hostName, ipAddress, "Connected");
		    //clientData.add(conn);
			this.clientTable.setItems(clientData);
		} catch (Exception e) {
			this.serverIPTextField.setText("Unable to get IP address");
		}
	}

	public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.<Parent>load(getClass().getResource("serverGUI.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	public void ConnectBtn(@SuppressWarnings("exports") ActionEvent event) throws UnknownHostException {
	    int portNumber;
	    String dbName = this.dbNameTextField.getText();
	    String dbUser = this.dbUserTextField.getText();
	    String dbPassword = this.dbPasswordTextField.getText();
	    try {
	        portNumber = Integer.parseInt(this.portTextFiled.getText());
	    } catch (NumberFormatException e) {
	        this.serverStatusLabel.setText("Invalid port number");
	        this.serverStatusLabel.setTextFill(Color.RED);
	        this.serverStatusLabel.setVisible(true);
	        return;
	    }
	
	    
	    BParkServer serverInstance = new BParkServer(portNumber, dbName, dbUser, dbPassword);
	    serverInstance.setClientDetailsCallback(this::updateClientDetails);
	    ServerUI.runServer(portNumber, serverInstance);
	    
	    this.serverStatusLabel.setText("Server is listening for connections on port " + portNumber);
	    this.serverStatusLabel.setTextFill(Color.GREEN);
	    this.serverStatusLabel.setVisible(true);
	    
	    disableButton(this.connectBtn, true);
	    disableButton(this.disconnectBtn, false);
	    
	    // Refresh the table to show changes
	    this.clientTable.refresh();
	}

	@FXML
	public void DisconnectBtn(@SuppressWarnings("exports") ActionEvent event) throws UnknownHostException {
	    ServerUI.stopServer();
	    this.serverStatusLabel.setText("Server disconnected");
	    this.serverStatusLabel.setTextFill(Color.RED);
	    this.serverStatusLabel.setVisible(true);

	    // Disable connect button and enable disconnect button
	    disableButton(this.connectBtn, false);
	    disableButton(this.disconnectBtn, true);

	    // Update all connected clients to "disconnected"
	    Platform.runLater(() -> {
	        for (ClientConnInfo client : clientData) {
	            if (client.getStatus().equalsIgnoreCase("connected")) {
	                client.setStatus("Disconnected");
	            }
	        }
	        this.clientTable.refresh();  // Refresh the table to reflect all changes
	    });
	}


	@FXML
	public void CloseBtn(@SuppressWarnings("exports") ActionEvent event) throws Exception {
		System.exit(0);
	}

	private void disableButton(Button button, boolean disable) {
		button.setDisable(disable);
		button.setOpacity(disable ? 0.5D : 1.0D);
	}

	public void updateClientDetails(String clientHostName, String clientIpAddress) {
	    Platform.runLater(() -> {
	        boolean exists = false;
	        for (ClientConnInfo client : clientData) {
	            if (client.getIpAddress().equals(clientIpAddress)) {
	                client.setStatus("Connected");
	                exists = true;
	                break;
	            }
	        }
	        if (!exists) {
	            clientData.add(new ClientConnInfo(clientHostName, clientIpAddress, "Connected"));
	        }
	        clientTable.refresh();
	    });
	}


	public void updateClientDisconnectionDetails(ConnectionToClient client) {
	    String ip = client.getInetAddress().getHostAddress();
	    
	    // Find and update the client in the table
	    boolean found = false;
	    for (ClientConnInfo c : clientData) {
	        if (c.getIpAddress().equals(ip)) {
	            c.setStatus("Disconnected");
	            found = true;
	            break;
	        }
	    }
	    
	    // If not found, add new entry (shouldn't happen but failsafe)
	    if (!found) {
	        String host = client.getInetAddress().getHostName();
	        clientData.add(new ClientConnInfo(host, ip, "Disconnected"));
	    }
	    
	    // Refresh the table
	    clientTable.refresh();
	}

	public void addOrUpdateClient(String hostName, String ipAddress, String status) {
        boolean exists = false;
        for (ClientConnInfo client : clientData) {
            if (client.getIpAddress().equals(ipAddress)) {
                client.setStatus(status);
                exists = true;
                break;
            }
        }
        
        if (!exists) {
            clientData.add(new ClientConnInfo(hostName, ipAddress, status));
        }
    }

}
