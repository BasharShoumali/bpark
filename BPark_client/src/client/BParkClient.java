package client;

import common.ChatIF;
import javafx.application.Platform;

import java.io.IOException;
import java.net.InetAddress;

import clientGUI.ConnectionGUIController;
import clientGUI.DisplayGUIController;
import clientGUI.HomePageGUIController;
import clientGUI.UpdateGUIController;
import ocsf.client.AbstractClient;

public class BParkClient extends AbstractClient implements ChatIF {

	private DisplayGUIController displayGUIController;
	
	private ConnectionGUIController connectionGUIController;

	@SuppressWarnings("unused")
	private HomePageGUIController homePageGUIController;

	@SuppressWarnings("unused")
	private UpdateGUIController updateGUIController;

	public static final int DEFAULT_PORT = 5555;

	public static BParkClient client;

	ChatIF clientUI;

	public void setDisplayController(DisplayGUIController displayGUIController) {
		this.displayGUIController = displayGUIController;
	}
	public void setConnectionController(ConnectionGUIController connectionGUIController) {
		this.connectionGUIController = connectionGUIController;
	}

	public void setHomePageController(HomePageGUIController homePageGUIController) {
		this.homePageGUIController = homePageGUIController;
	}

	public void setUpdateController(UpdateGUIController updateGUIController) {
		this.updateGUIController = updateGUIController;
	}

	public void requestFromServer(Object Request) {
	    try {
	      if (isConnected()) {
	        sendToServer(Request);
	      } else {
	        System.exit(1);
	      } 
	    } catch (IOException e) {
	      System.out.println("Error sending request to server: " + e.getMessage());
	    } 
	  }

	public BParkClient(String host, int port) {
	    super(host, port);
	    this.clientUI = this;
	    try {
	      openConnection();
	    } catch (IOException e) {
	      this.homePageGUIController.inputResponse("");
	    } 
	  }

	public void display(String message) {
		System.out.print("> " + message);
	}

	public void disconnectAndNotify() {
        try {
            sendToServer("DISCONNECT_NOTIFICATION"); // Send special message
            closeConnection();
        } catch (IOException e) {
            System.out.println("Error during disconnection: " + e.getMessage());
        }
    }
	 public void quit() {
	        disconnectAndNotify();
	        System.exit(0);
	    }

	public void handleMessageFromServer(Object msg) {
	    if (msg instanceof String) {
	        System.out.println("I am here");
	        if (this.displayGUIController != null) {
	            // Ensure the UI update is on the JavaFX thread
	        	System.out.println("i am there");
	            Platform.runLater(() -> {
	                this.displayGUIController.Display((String) msg);
	            });
	        } else {
	            System.out.println("⚠️ displayGUIController is null");
	        }
	    }
	}

	// Thread-safe singleton with lazy initialization
	public static BParkClient getInstance(String host, int port) throws IOException {
	    if (client == null || !client.isConnected()) {
	        synchronized (BParkClient.class) {
	            if (client == null || !client.isConnected()) {
	                client = new BParkClient(host, port);
	            }
	        }
	    }
	    return client;
	}

	// Static method to create the instance (if not already created)
	public static void createInstance(String host, int port) throws IOException {
		if (client == null) {
			client = new BParkClient(host, port);
		}
	}

}
