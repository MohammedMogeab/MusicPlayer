package com.example.musicplayer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class  HelloController implements Initializable {

    @FXML
    private Label SongLabel;
    @FXML
    private Button PlayButton,PauseButton,ResetButton,PerviousButton,NextButton;
    @FXML
    private ComboBox<String> speedCombobox;
    @FXML
    private Slider valumeSlider;
    @FXML
    private ProgressBar SongProgressBar;

    private  Media media;
    private MediaPlayer mediaPlayer;
    private File dirctory;

    private File [] files;
    private ArrayList<File> songs;
    private int songNumber;
    private int [] speed ={25,50,75,100,125,150,175,200};
    private Timer timer;
    private TimerTask timerTask;
    private  boolean running;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    songs= new ArrayList<File>();
    dirctory = new File("C:\\Users\\Lenovo\\OneDrive\\Desktop\\music");
    files = dirctory.listFiles();
    if(files !=null){
        for (File file:files){
            songs.add(file);
            System.out.println(file);
        }
    }
    media = new Media(songs.get(songNumber).toURI().toString());
    mediaPlayer = new MediaPlayer(media);
    SongLabel.setText(songs.get(songNumber).getName());

        for(int i = 0; i < speed.length; i++) {

            speedCombobox.getItems().add(Integer.toString(speed[i])+"%");
        }
        speedCombobox.setOnAction(this::changespeed);
        valumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(valumeSlider.getValue()*0.01);
            }
        });
        SongProgressBar.setStyle("-fx-accent:#00ff00");


    }


    public void changespeed(ActionEvent event){
        if(speedCombobox.getValue()==null){
            mediaPlayer.setRate(1);
        }
        else {

            mediaPlayer.setRate(Integer.parseInt
                    (speedCombobox.getValue()
                            .substring(0, speedCombobox.getValue()
                                    .length() - 1)) * 0.01);
        }

    }
    public void beginTimer(){
   timer = new Timer();
   timerTask = new TimerTask() {
       @Override
       public void run() {
running =true;
double current = mediaPlayer.getCurrentTime().toSeconds();
double end = media.getDuration().toSeconds();
           System.out.println(current/end);
           SongProgressBar.setProgress(current/end);
           if(current/end==1){
               CancelTimer();

           }
       }
   };
   timer.scheduleAtFixedRate(timerTask,1000,1000);

    }

    public void CancelTimer(){
running =false;
timer.cancel();
    }

    public void PauseMedia(ActionEvent event) {
        CancelTimer();
        mediaPlayer.pause();
    }

    public void ActionSpeed(ActionEvent event) {
    }

    public void NextMedia(ActionEvent event) {
        if(songNumber<songs.size()-1){
            songNumber++;
            mediaPlayer.stop();
            if(running){
                CancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            SongLabel.setText(songs.get(songNumber).getName());
            PlayMedia();
        }
        else {
            songNumber=0;
            mediaPlayer.stop();
            if(running){
                CancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            SongLabel.setText(songs.get(songNumber).getName());
        }
    }

    public void PreviousMedia(ActionEvent event) {
        if(songNumber>0){
            songNumber--;
            mediaPlayer.stop();
            if(running){
                CancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            SongLabel.setText(songs.get(songNumber).getName());
            PlayMedia();
        }
        else {
            songNumber=songs.size()-1;
            mediaPlayer.stop();
            if(running){
                CancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            SongLabel.setText(songs.get(songNumber).getName());
        }
    }
    public void PlayMedia(){

        beginTimer();
        changespeed(null);
        mediaPlayer.setVolume(valumeSlider.getValue()*0.01);
    mediaPlayer.play();
    }
    public void ResetMedia(){
        SongProgressBar.setProgress(0);
    mediaPlayer.seek(Duration.seconds(0));
    }

}