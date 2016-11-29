import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class setupController {

    @FXML
    private TextField inputServer;

    @FXML
    private Button buttonEnter;

    @FXML
    private TextField inputR;
    
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	buttonEnter.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	ChatClient.serverIP = inputServer.getText();
            	String[] dickbutts = inputR.getText().split(",");
            	for (String s : dickbutts){
            		ChatClient.chatters.add(s.trim());
            	}
            	ChatClient.stage.close();
            	try {
					start(new Stage());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
    }
    
    
    public void start(Stage primaryStage) throws Exception {
        try {
        	ChatClient.stage = primaryStage;
        	primaryStage.setMinHeight(160);
        	primaryStage.setMinWidth(640);
            Pane page = (Pane) FXMLLoader.load(ChatClient.class.getResource("chatwindow.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("chat session");
            primaryStage.show();
        } catch (Exception ex) {
          System.out.println("ERROR: FILE NOT FOUND");
          throw ex;
        }
    }
}


