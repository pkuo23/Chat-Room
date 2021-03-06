/* CHAT ROOM ClientMain.java
 * EE422C Project 7 submission by
 * <Josh Knight>
 * <JTK764>
 * <16740>
 * <Peter Kuo>
 * <PAK654>
 * <16445>
 * Slip days used: <1>
 * Fall 2016
 */

package assignment7;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ClientMain extends Application {
	static Scanner kb;
	final int PORT = 9001;
    static String SERVER_ADDRESS;
    BufferedReader in;
    PrintWriter out;

    public static void main(String[] args) {
    	kb = new Scanner(System.in);
    	System.out.print("What is the server ip?");
    	SERVER_ADDRESS=kb.nextLine();	
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);

        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setOnAction(e -> {
            out.println(textField.getText());
            textField.clear();
        });

        VBox layout = new VBox(5);
        layout.getChildren().addAll(messageArea, textField);

        stage.setScene(new Scene(layout));
        stage.setTitle("Chat Client");

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    @SuppressWarnings("resource")
					Socket socket = new Socket(SERVER_ADDRESS, PORT);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    while (true) {
                        String line = in.readLine();

                        if (line.startsWith("SUBMIT_NAME")) {
                            FutureTask<String> futureTask = new FutureTask<>(new NamePrompt("Choose a screen name:"));
                            Platform.runLater(futureTask);
                            try {
                                out.println(futureTask.get());
                            } catch(InterruptedException | ExecutionException ex) {}
                        } else if (line.startsWith("RESUBMIT_NAME")) {
                            FutureTask<String> futureTask = new FutureTask<>(new NamePrompt("Duplicate name. Try another:"));
                            Platform.runLater(futureTask);
                            try {
                                out.println(futureTask.get());
                            } catch(InterruptedException | ExecutionException ex) {}
                        } else if (line.startsWith("NAME_ACCEPTED")) {
                            textField.setEditable(true);
                            Platform.runLater(() -> textField.requestFocus());
                        } else if (line.startsWith("INFO")) {
                            messageArea.appendText("Users connected: " + line.charAt(4) + '\n' + line.substring(5) + "\n\n");
                        } else if (line.startsWith("CONNECT")) {
                            messageArea.appendText(line.substring(7) + " has connected.\n\n");
                        } else if (line.startsWith("MESSAGE")) {
                            messageArea.appendText(line.substring(8) + '\n');
                        } else if (line.startsWith("DISCONNECT")) {
                            messageArea.appendText(line.substring(10) + " has disconnected.\n\n");
                        }
                    } 
                } catch(IOException ioe) {
                    messageArea.appendText("Server is offline.\nPlease exit.");
                }

                return null;
            }
        };

        Thread severIO = new Thread(task);
        severIO.setDaemon(true);
        severIO.start();

        stage.show();
    }

    class NamePrompt implements Callable<String> {
        String message;

        NamePrompt(String message) {
            this.message = message;
        }

        @Override
        public String call() {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Welcome to Chatter");
            dialog.setHeaderText("Screen name selection");
            dialog.setContentText(message);
            dialog.setGraphic(null);
            return dialog.showAndWait().get();
        }
    }
}
	

