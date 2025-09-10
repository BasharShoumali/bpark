package client;

import clientGUI.ConnectionGUIController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {
  ConnectionGUIController connectionGUIController;
  
  public static BParkClient bParkClient;
  
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {
      this.connectionGUIController = new ConnectionGUIController();
      this.connectionGUIController.start(primaryStage);
  }
}
