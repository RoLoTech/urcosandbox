package com.example.demo;

import com.example.demo.entities.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;

public class FlorChaser extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("Collect the Money Bags, don't touch the bombs!");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);


        Canvas canvas = new Canvas(1000, 1000);
        root.getChildren().add(canvas);

        ArrayList<String> input = new ArrayList<>();

        theScene.setOnKeyPressed(
                e -> {
                    String code = e.getCode().toString();
                    if (!input.contains(code))
                        input.add(code);
                });

        theScene.setOnKeyReleased(
                e -> {
                    String code = e.getCode().toString();
                    input.remove(code);
                });


//        restartButton.setOnMouseClicked(
//                e -> {
//                    FlorChaser app = new FlorChaser();
//                    app.start(theStage);
//                });


        GraphicsContext gc = canvas.getGraphicsContext2D();

        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);


        Sprite character = new Sprite();
        character.setImage("images/character.png");
        character.setPosition(500, 0);


        ArrayList<Sprite> collectibleList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Sprite collectible = new Sprite();
            collectible.setImage("images/collectible.png");
            double px = 700 * Math.random() + 50;
            double py = 700 * Math.random() + 50;
            collectible.setPosition(px, py);
            collectibleList.add(collectible);
        }

        ArrayList<Sprite> dangerList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Sprite danger = new Sprite();
            danger.setImage("images/danger.png");
            double px = 700 * Math.random() + 20;
            double py = 700 * Math.random() + 70;
            if (px != character.getPositionX() || py != character.getPositionY()) danger.setPosition(px, py);
            else {
                px = px + 10;
                py = py + 10;
            }
            danger.setPosition(px, py);
            dangerList.add(danger);
        }

        LongValue lastNanoTime = new LongValue(System.nanoTime());

        IntValue score = new IntValue(0);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                // game logic

                character.setVelocity(0, 0);
                if (input.contains("LEFT") && character.getPositionX() >= 0 && !character.isLoss())
                    character.addVelocity(-300, 0);
                if (input.contains("RIGHT") && character.getPositionX() <= 900 && !character.isLoss())
                    character.addVelocity(300, 0);
                if (input.contains("UP") && character.getPositionY() >= 0 && !character.isLoss())
                    character.addVelocity(0, -300);
                if (input.contains("DOWN") && character.getPositionY() <= 680 && !character.isLoss())
                    character.addVelocity(0, 300);

                character.update(elapsedTime);

                // collision detection

                Iterator<Sprite> collectibleIter = collectibleList.iterator();
                while (collectibleIter.hasNext()) {
                    Sprite moneybag = collectibleIter.next();
                    if (character.intersects(moneybag)) {
                        collectibleIter.remove();
                        score.setValue(score.getValue() + 1);
                    }
                }

                Iterator<Sprite> dangerIter = dangerList.iterator();
                while (dangerIter.hasNext()) {
                    Sprite currentDanger = dangerIter.next();
                    if (character.intersects(currentDanger)) {
                        character.setLoss(true);
                    }
                }

                // render
                gc.clearRect(0, 0, 1000, 1000);
                character.render(gc);


                for (Sprite collectibleObject : collectibleList)
                    collectibleObject.render(gc);

                for (Sprite dangerObject : dangerList)
                    dangerObject.render(gc);

                if (score.getValue() == 7) {
                    Image winTrigger = new Image("images/winTrigger.png");
                    gc.drawImage(winTrigger, 0, -100);
                }
                if (character.isLoss()) {
                    Image lossTrigger = new Image("images/lossTrigger.png");
                    gc.drawImage(lossTrigger, 0, 0);
                }


                String pointsText = "Cash: $" + (100 * score.getValue());
                gc.fillText(pointsText, 360, 36);
                gc.strokeText(pointsText, 360, 36);


            }
        }.start();

        theStage.show();

    }
}
