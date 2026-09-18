package org.pod4u.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import org.pod4u.account.Authenticator;

import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.pod4u.account.SpotifyAccount;
import org.pod4u.mood.MoodGenerator;
import org.pod4u.playlist.PlaylistGenerator;
import org.pod4u.serialisation.AudioDeserialiser;
import org.pod4u.serialisation.MoodDeserialiser;

import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Pod4UController {
    @FXML
    public HBox hbxPlaylists;
    @FXML
    public AnchorPane ancPlaylists;
    @FXML
    private Button btnRandom;
    @FXML
    private Button btnSliders;
    @FXML
    private Button btnUpload;
    @FXML
    private Button btnCamera;
    @FXML
    private Button btnLoad;
    @FXML
    private Label welcomeText;
    @FXML
    private TextField loginEmail;
    @FXML
    private TextField loginPassword;
    @FXML
    private TextField registerForename;
    @FXML
    private TextField registerSurname;
    @FXML
    private TextField registerEmail;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private Button btnGo;
    @FXML
    private Button btnConfirm;
    @FXML
    private ComboBox<String> cbxDuration;

    private final Authenticator auth;
    private final MoodGenerator moodGen;
    private PlaylistGenerator playGen;
    private String info;

    public Pod4UController() {
        this.auth = new Authenticator();
        this.moodGen = new MoodGenerator();
        this.playGen = null;
        this.hbxPlaylists = new HBox();
        this.ancPlaylists = new AnchorPane();
    }

    @FXML
    protected void onConnectButtonClick() throws IOException, InterruptedException {
        Stage stage = new Stage();
        showGenerator(stage);
        this.auth.connect();
        setupGenerator(false);
        Stage.getWindows().getFirst().hide();
        stage.show();
    }

    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Pod4U.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 230, 210);
        showStage(new Stage(), scene);
    }

    @FXML
    public void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Pod4U.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 250, 210);
        showStage(new Stage(), scene);
    }

    private void showStage(Stage stage, Scene scene) {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onGoButtonClick(ActionEvent actionEvent) throws Exception {
        if (this.auth.login(loginEmail.getText(), loginPassword.getText()))
            connectSpotify();
    }

    @FXML
    public void onConfirmButtonClick(ActionEvent actionEvent) throws Exception {
        if (this.auth.register(registerForename.getText(), registerSurname.getText(), registerEmail.getText(), registerPassword.getText()))
            connectSpotify();
    }

    public void onTextEdited(KeyEvent keyEvent) {
        if (!registerForename.getText().isBlank() && !registerSurname.getText().isBlank() && !registerEmail.getText().isBlank() && !registerPassword.getText().isBlank())
            btnConfirm.setDisable(false);
        else
            btnConfirm.setDisable(true);
    }

    private Stage showGenerator(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Pod4U.class.getResource("generator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 250, 300);
        showStage(stage, scene);
        return stage;
    }

    private void setupGenerator(boolean generatorReady) {
        try {
            while (!generatorReady) {
                Thread.sleep(2000);
                generatorReady = Authenticator.generatorReady();
            }
        } catch(InterruptedException e) {
            IO.println(e);
        }
    }

    private void connectSpotify() throws IOException, InterruptedException {
        this.auth.connect();
        setupGenerator(false);
        Stage.getWindows().get(1).hide();
        Stage.getWindows().getFirst().hide();
        showGenerator(new Stage()).show();
    }

    @FXML
    private void initialize(Stage stage) throws IOException {
        //Stage.getWindows().get(1).hide();
        //FXMLLoader fxmlLoader = new FXMLLoader(Pod4U.class.getResource("playlist.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Scene scene = new Scene(this.ancPlaylists, 600, 400);
        this.ancPlaylists.setStyle("-fx-background-color: #dddd44; -fx-border-radius: 18px;");
        Label lblPlaylist = new Label();
        lblPlaylist.setText("Your playlist");
        lblPlaylist.setTextFill(Paint.valueOf("#013c03"));
        lblPlaylist.setFont(Font.font("size", 24.0));
        lblPlaylist.setPrefWidth(600.0);
        lblPlaylist.setMinWidth(600.0);
        lblPlaylist.setAlignment(Pos.valueOf("CENTER"));
        lblPlaylist.setTextAlignment(TextAlignment.valueOf("CENTER"));
        HBox hbxHeader = new HBox();
        hbxHeader.setMinWidth(600);
        hbxHeader.setPrefWidth(600);
        hbxHeader.getChildren().add(lblPlaylist);
        this.ancPlaylists.getChildren().add(hbxHeader);
        //this.ancPlaylists.getChildren().add(this.hbxPlaylists);
        //this.hbxPlaylists.setLayoutY(35.0);
        String[] info = this.info.split("\r\n\r\n");
        for (String track : info) {
            HBox hbx = new HBox();
            hbx.setLayoutY(35.0 + (40.0 * this.ancPlaylists.getChildren().size()));
            this.ancPlaylists.getChildren().add(hbx);
            Label lblTitle = new Label(), lblArtists = new Label(), lblDuration = new Label();
            VBox vbxTrack = new VBox(), vbxDuration = new VBox();
            vbxTrack.setPrefWidth(565.0);
            vbxTrack.setMaxWidth(565.0);
            vbxTrack.setPrefHeight(40.0);
            vbxTrack.setMaxHeight(40.0);
            vbxTrack.setPadding(new Insets(0.0, 0.0, 0.0, 8.0));
            vbxDuration.setPrefWidth(100.0);
            vbxDuration.setMaxWidth(100.0);
            vbxDuration.setPrefHeight(40.0);
            vbxDuration.setMaxHeight(40.0);
            hbx.getChildren().add(vbxTrack);
            hbx.getChildren().add(vbxDuration);
            lblTitle.setTextFill(Paint.valueOf("#013c03"));
            lblArtists.setTextFill(Paint.valueOf("#013c03"));
            lblDuration.setTextFill(Paint.valueOf("#013c03"));
            vbxTrack.getChildren().addAll(lblTitle, lblArtists);
            vbxDuration.getChildren().add(lblDuration);
            lblTitle.setText(track.split("\r\n")[0]);
            lblArtists.setText(track.split("\r\n")[1]);
            lblDuration.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(track.split("\r\n")[3])), TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(track.split("\r\n")[3])) % 60));
        }
        //Stage.getWindows().getFirst().hide();
        showStage(stage, scene);
    }

    private byte[] isMatch() {
        final float clarity = this.moodGen.getMood().getClarity(), presence = this.moodGen.getMood().getPresence();
        final boolean[] toneMatch = new boolean[]{clarity>9, clarity<=9 && clarity>6, clarity<=6 && clarity>3, clarity<=3 && clarity>0, clarity==0}, loudnessMatch = new boolean[]{presence<-25, presence>=-25 && presence<-20, presence>=-20 && presence<-15, presence>=-15 && presence<-10, presence>=-10 && presence<-5, presence>=-5};
        byte[] isMatch = new byte[]{-1, -1};
        for (int i=0; i<loudnessMatch.length; i++) {
            if (i<toneMatch.length && toneMatch[i])
                isMatch[0] = (byte)i;
            if (loudnessMatch[i])
                isMatch[1] = (byte)i;
        }
        if (isMatch[0]==-1)
            isMatch[0] = (byte)6;
        return isMatch;
    }

    public void onCameraButtonClick(ActionEvent actionEvent) {
    }

    public void onLoadButtonClick(ActionEvent actionEvent) {
    }

    public void onUploadButtonClick(ActionEvent actionEvent) throws IOException, InterruptedException, SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a photo to upload");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpeg", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(Stage.getWindows().getFirst());
        this.moodGen.uploadPhoto(selectedFile.toPath(), new MoodDeserialiser());
        this.playGen = new PlaylistGenerator(SpotifyAccount.getSavedTracks(), new AudioDeserialiser(), this.moodGen.getMood(), this.auth.getToken());
        String[] options = new String[]{"900000", "1800000", "2700000", "3600000", "4500000", "5400000", "6300000", "7200000", "8100000", "9000000", "9900000", "10800000"};
        this.info = this.playGen.selectAudio(Long.parseLong(options[cbxDuration.getSelectionModel().getSelectedIndex()]), isMatch());
        initialize(new Stage());
        //IO.println(this.info);
    }

    public void onRandomButtonClick(ActionEvent actionEvent) {
    }

    public void onSliderButtonClick(ActionEvent actionEvent) {
    }

    public void onDurationSelect(ActionEvent actionEvent) {
        btnCamera.setDisable(false);
        btnUpload.setDisable(false);
        btnSliders.setDisable(false);
        btnRandom.setDisable(false);
        if (!this.auth.getEmail().isBlank())
            btnLoad.setDisable(false);
    }
}
