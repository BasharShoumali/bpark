package server;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import serverGUI.ServerGUIController;

public class ServerUI extends Application {
	public static final int DEFAULT_PORT = 5555;

	private static BParkServer server;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
		ServerGUIController aFrame = new ServerGUIController();
		aFrame.start(primaryStage);
	}

	public static void runServer(int port, BParkServer serverInstance) {
	    if (server != null && server.isListening()) {
	      System.out.println("Server is already running");
	      return;
	    } 
	    server = serverInstance;
	    try {
	      server.listen();
	    } catch (Exception ex) {
	      System.out.println("ERROR - Could not listen for clients!");
	    } 
	  }

	

	public static void stopServer() {
	    if (server != null && server.isListening())
	      try {
	        server.close();
	      } catch (IOException e) {
	        System.out.println("Error closing the server: " + e.getMessage());
	      }  
	  }
}
