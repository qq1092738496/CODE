package com.meditation.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-17 13:27
 */

public class a2 extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage Stage) throws Exception {
        URL url = getClass().getResource("/bbbbb.fxml");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Stage.setScene(scene);
        Stage.setTitle("Down");
       // Stage.initStyle(StageStyle.UTILITY);
        Stage.show();
    }
}
