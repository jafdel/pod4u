package org.pod4u.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class Pod4U extends Application {
    @Override
    public void start(Stage stage) throws IOException, IllegalStateException {
        URL fxmlUrl = Pod4U.class.getResource("home.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        if (fxmlUrl==null)
            throw new IllegalStateException(String.valueOf(fxmlLoader.getLocation()));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
