package assignment7;


import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Application;

import javafx.scene.control.TextArea;


import javafx.scene.layout.VBox;


import java.awt.*;
import java.awt.event.*;

public class ChatClient extends Application implements Initializable{
	public BufferedReader reader;
	public PrintWriter writer;
	public static Stage stage;
	public static String serverIP;
	public static List<String> chatters = new java.util.ArrayList<String>();

	public void run() throws Exception {
		//initView();
		setUpNetworking();
	}

	private void initView() {
    	try {
        	stage = new Stage();
        	stage.setMinHeight(160);
        	stage.setMinWidth(640);
            Pane page = (Pane) FXMLLoader.load(ChatClient.class.getResource("chatwindow.fxml"));
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.setTitle("chat session");
            stage.show();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();}
		

	}

	public void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}

    @FXML
    public static TextField input;

    @FXML
    public static VBox userlist;

    @FXML
    private Button buttonSend;

    @FXML
    public static TextArea chatbox;
    
    public void initialize(URL arg0, ResourceBundle arg1) {
    	buttonSend.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
    writer.println(input.getText());
    writer.flush();
    chatbox.setText(input.getText());
    input.clear();
            }});

}

	public static void main(String[] args) {
		try {
			
			new ChatClient().run();
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					
						chatbox.appendText(message + "\n");
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
        	stage = primaryStage;
        	primaryStage.setMinHeight(160);
        	primaryStage.setMinWidth(640);
            Pane page = (Pane) FXMLLoader.load(ChatClient.class.getResource("setup.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Server Setup");
            primaryStage.show();
        } catch (Exception ex) {
          System.out.println("ERROR: FILE NOT FOUND");
          throw ex;
        }
    }
    public void start2(Stage primaryStage) throws Exception {
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


	
	

