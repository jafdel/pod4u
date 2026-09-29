module org.pod4u {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires jdk.httpserver;
    requires java.desktop;
    requires java.sql;
    requires static bcrypt;
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires se.michaelthelin.spotify;
    requires org.slf4j;
    requires jdk.net;
    requires com.google.gson;
    requires org.postgresql.jdbc;
    requires org.newsclub.net.unix;

    opens org.pod4u.app to javafx.fxml, javafx.controls;
    //opens org.pod4u.account to bcrypt;
    opens org.pod4u.serialisation to tools.jackson.databind;
    opens org.pod4u.audio to tools.jackson.databind;
    opens org.pod4u.mood to tools.jackson.databind;
    opens org.pod4u.playlist to tools.jackson.databind, se.michaelthelin.spotify, org.postgresql.jdbc, org.newsclub.net.unix;

    exports org.pod4u.app;
}