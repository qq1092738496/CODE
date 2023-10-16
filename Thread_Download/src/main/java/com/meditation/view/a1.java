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
 * @time: 2023-9-13 21:44
 */

public class a1  extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage Stage) throws Exception {
        URL url = getClass().getResource("/aaaaa.fxml");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root, 250, 400);
        Stage.setScene(scene);
        Stage.setTitle("Down");
        //Stage.initStyle(StageStyle.UNIFIED);
        Stage.show();
    }
}
