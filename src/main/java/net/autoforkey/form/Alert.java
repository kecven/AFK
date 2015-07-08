package net.autoforkey.form;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by andrei on 09.07.15.
 */
public class Alert extends Application {
    private static String text;

    @Override
    public void start(Stage primaryStage) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, text);
        alert.show();
    }

    public static void information(String text) {
        Alert.text = text;

        launch(new String[0]);
    }

}
