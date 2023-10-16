package com.meditation.core;/**
 * @description:
 * @author: Andy
 * @time: 2023-9-10 17:03
 */

import com.meditation.pojo.downInfo;
import javafx.application.Application;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class aa extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    ScheduledService<Double> service;

    @Override
    public void start(Stage Stage) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(200, 1);
        progressBar.setProgress(0.00);

        Button button = new Button("下载");

        service = new ScheduledService<Double>() {
            @Override
            protected Task<Double> createTask() {
                Task<Double> task = new Task<Double>() {
                    @Override
                    protected Double call() throws Exception {
                        downInfo info = Infos.infos[0];
                        long fileLength = info.fileLength;
                        AtomicLong downSize = info.downSize;
                        double v = downSize.doubleValue() / fileLength;
                        System.out.println(fileLength);
                        System.out.println(downSize.doubleValue());
                        System.out.println(v);
                        return v;
                    }

                    @Override
                    protected void updateValue(Double value) {
                        progressBar.setProgress(value);
                        if (value >= 1) {
                            service.cancel();
                        }
                    }
                };
                return task;
            }
        };
        service.setDelay(Duration.millis(0));
        service.setPeriod(Duration.millis(1000));


        String url = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.16/QQ9.7.16.29187.exe";
        List<Header> handlers = new ArrayList<Header>();
        handlers.add(new BasicHeader("User-Agent", "netdisk"));
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                service.start();
                new Thread(() -> {
                    downloadFile downloadFile = new downloadFile(handlers);
                    downloadFile.poolDownload(url);
                    downloadFile.poolShutdown();
                }).start();

            }
        });
        VBox vBox = new VBox(progressBar, button);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(vBox);
        Scene scene = new Scene(anchorPane, 960, 600);
        Stage.setScene(scene);
        // Stage.initStyle(StageStyle.UNIFIED);
        Stage.setTitle("Down");
        Stage.show();
    }
}
