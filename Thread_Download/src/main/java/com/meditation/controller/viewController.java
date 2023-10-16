package com.meditation.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * @description:
 * @author: Andy
 * @time: 2023-9-17 13:16
 */

public class viewController {
    public void ss(){
        try {
            Stage Stage = new Stage();
            URL url = getClass().getResource("/bbbbb.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            Stage.setScene(scene);
            Stage.setTitle("Down");
            // Stage.initStyle(StageStyle.UTILITY);
            Stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void xx(){

    }
}
