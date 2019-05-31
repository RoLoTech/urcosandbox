package com.example.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;


public class ScrollingBackground extends Application {

    private double SCENE_WIDTH = 400;
    private double SCENE_HEIGHT = 800;

    /**
     * Main game loop
     */
    private AnimationTimer gameLoop;

    /**
     * Container for the background image;
     */
    ImageView backgroundImageView;

    /**
     * Scrolling speed of the background
     */
    double backgroundScrollSpeed = 0.5;

    /**
     * Layer for the background
     */
    Pane backgroundLayer;

    @Override
    public void start(Stage primaryStage) {
        try {

            // create root node
            Group root = new Group();

            // create layers
            backgroundLayer = new Pane();

            // add layers to scene root
            root.getChildren().add(backgroundLayer);

            // create scene
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

            // show stage
            primaryStage.setScene(scene);
            primaryStage.show();

            // load game assets
            loadGame();

            // start the game
            startGameLoop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGame() {

        // background
        // --------------------------------
        Image img = new Image("images/canyon.jpg");
        backgroundImageView = new ImageView(img);

        // reposition the map. it is scrolling from bottom of the background to top of the background
        backgroundImageView.relocate(0, -backgroundImageView.getImage().getHeight() + SCENE_HEIGHT);

        // add background to layer
        backgroundLayer.getChildren().add(backgroundImageView);

    }

    private void startGameLoop() {

        // game loop
        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long l) {

                // scroll background
                // ---------------------------
                // calculate new position
                double y = backgroundImageView.getLayoutY() + backgroundScrollSpeed;

                // check bounds. we scroll upwards, so the y position is negative. once it's > 0 we have reached the end of the map and stop scrolling
                if (Double.compare(y, 0) >= 0) {
                    y = 0;
                    Media sound = new Media(new File("demo/src/main/resources/sound/MEUTE - You & Me (Flume Remix).mp3").toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                }

                // move background
                backgroundImageView.setLayoutY(y);


            }

        };

        gameLoop.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}