package org.pod4u.app;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            Application.launch(Pod4U.class, args);
        } catch(Exception e) {
            System.setProperty("javafx.fxml.debug", "true");
            e.printStackTrace();
            IO.println(e);
        }
    }
}
