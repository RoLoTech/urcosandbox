package com.example.demo;

import com.example.demo.entities.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ScrollingTest extends Application {

    private double SCENE_WIDTH = 1000;
    private double SCENE_HEIGHT = 1000;
    private double SCROLLING_SPEED = 10.0;
    private String LEFT = "LEFT";
    private String RIGHT = "RIGHT";
    private String UP = "UP";
    private String DOWN = "DOWN";
    private Pane backgroundLayer;
    private Pane backgroundLayer2;
    private ImageView backgroundImageView;
    private ImageView backgroundImageView2;
    private AnimationTimer gameLoop;
    double xPosition;
    double yPosition;
    private  ArrayList<String> input = new ArrayList<>();



    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Test de fondo");
        Group root = new Group();
        backgroundLayer = new Pane();
        backgroundLayer2=new Pane();

        root.getChildren().add(backgroundLayer);
        root.getChildren().add(backgroundLayer2);
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed(
                e -> {
                    String code = e.getCode().toString();
                    if (!input.contains(code))
                        input.add(code);
                });

        scene.setOnKeyReleased(
                e -> {
                    String code = e.getCode().toString();
                    input.remove(code);
                });


            primaryStage.show();
            loadBackground();
            addCharacter();
            loadGameLoop();

    }

    private void loadBackground() {
        Image img = new Image("images/space2.jpg");
        //Autowire?
        backgroundImageView = new ImageView(img);
        backgroundImageView.relocate(0, 0);
        backgroundLayer.getChildren().add(backgroundImageView);

    }

    private void addCharacter(){
        Image img = new Image ("images/ufo_0.png");
        backgroundImageView2=new ImageView(img);
        backgroundImageView2.relocate(500,200);
        backgroundLayer2.getChildren().add(backgroundImageView2);


    }

    private void loadGameLoop(){

        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                if(input.contains(LEFT)  ) {
                    xPosition = backgroundImageView.getLayoutX() + SCROLLING_SPEED;
                }
                if(input.contains(RIGHT) ) {
                    xPosition = backgroundImageView.getLayoutX() - SCROLLING_SPEED;
                }
                if(input.contains(UP) ) {
                    yPosition = backgroundImageView.getLayoutY() + SCROLLING_SPEED;
                }
                if(input.contains(DOWN)) {
                    yPosition = backgroundImageView.getLayoutY() - SCROLLING_SPEED;
                }
                backgroundImageView.setLayoutX(xPosition);
                backgroundImageView.setLayoutY(yPosition);


            }
        };

        gameLoop.start();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
