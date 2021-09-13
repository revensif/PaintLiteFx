package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    public Color color;
    public String modeUsed = "Paint";
    public double length;
    Rectangle rect = new Rectangle();
    Circle circle = new Circle();

    @Override
    public void start(Stage primaryStage) {

        ColorPicker colorpicker = new ColorPicker();
        colorpicker.setOnAction(event -> {
            color = colorpicker.getValue();
        });

        Slider sizeSlider = new Slider();
        sizeSlider.setMin(0);
        sizeSlider.setMax(200);
        sizeSlider.setValue(10);
        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMinorTickCount(10);
        sizeSlider.setMajorTickUnit(100);
        sizeSlider.setBlockIncrement(5);

        Button paintBtn = new Button("Paint");
        Button circleBtn = new Button("Circle");
        Button circleFullBtn = new Button("Filled Circle");
        Button rectBtn = new Button("Rectangle");
        Button rectFullBtn = new Button("Filled Rectangle");
        Button eraserBtn = new Button("Eraser");
        Button saveBtn = new Button("Save");
        Button openBtn = new Button("Open");
        Button infBtn = new Button("Information");

        paintBtn.setOnAction(event -> {
            modeUsed = "Paint";
        });
        circleBtn.setOnAction(event -> {
            modeUsed = "Circle";
        });
        circleFullBtn.setOnAction(event -> {
            modeUsed = "CircleFull";
        });
        rectBtn.setOnAction(event -> {
            modeUsed = "Rectangle";
        });
        rectFullBtn.setOnAction(event -> {
            modeUsed = "RectangleFull";
        });
        eraserBtn.setOnAction(event -> {
            modeUsed = "Eraser";
        });

        ToolBar tlBar = new ToolBar(colorpicker, sizeSlider, paintBtn, circleBtn, circleFullBtn, rectBtn, rectFullBtn, eraserBtn, saveBtn, openBtn, infBtn);
        tlBar.setPrefHeight(40);
        tlBar.setPrefWidth(900);
        tlBar.setStyle("-fx-background-color: #DCDCDC");


        Canvas canvas = new Canvas(1600, 900);
        GraphicsContext backGr = canvas.getGraphicsContext2D();
        backGr.setFill(Color.WHITE);
        backGr.fillRect(0, 0, 1600, 900);

        canvas.setOnMousePressed(event -> {
            switch (modeUsed) {
                case "Paint" -> {
                    backGr.setFill(colorpicker.getValue());
                    length = sizeSlider.getValue();
                    backGr.fillRect(event.getX() - length / 2, event.getY() - length / 2, length, length);
                }
                case "Circle" -> {
                    backGr.setStroke(colorpicker.getValue());
                    circle.setCenterX(event.getX());
                    circle.setCenterY(event.getY());
                }
                case "CircleFull" -> {
                    backGr.setFill(colorpicker.getValue());
                    circle.setCenterX(event.getX());
                    circle.setCenterY(event.getY());
                }
                case "Rectangle" -> {
                    backGr.setStroke(colorpicker.getValue());
                    rect.setX(event.getX());
                    rect.setY(event.getY());
                }
                case "RectangleFull" -> {
                    backGr.setFill(colorpicker.getValue());
                    rect.setX(event.getX());
                    rect.setY(event.getY());
                }
                case "Eraser" -> {
                    backGr.setFill(Color.WHITE);
                    length = sizeSlider.getValue();
                    backGr.fillRect(event.getX() - length / 2, event.getY() - length / 2, length, length);
                }
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (modeUsed.equals("Paint")) {
                backGr.fillRect(event.getX() - length / 2, event.getY() - length / 2, length, length);
            } else if (modeUsed.equals("Eraser")) {
                backGr.fillRect(event.getX() - length / 2, event.getY() - length / 2, length, length);
            }
        });

        canvas.setOnMouseReleased(event -> {
            switch (modeUsed) {
                case "Rectangle" -> {
                    rect.setWidth(Math.abs(event.getX() - rect.getX()));
                    rect.setHeight(Math.abs(event.getY() - rect.getY()));
                    if (rect.getX() > event.getX()) {
                        rect.setX(event.getX());
                    }
                    if (rect.getY() > event.getY()) {
                        rect.setY(event.getY());
                    }
                    backGr.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                }
                case "RectangleFull" -> {
                    rect.setWidth(Math.abs(event.getX() - rect.getX()));
                    rect.setHeight(Math.abs(event.getY() - rect.getY()));
                    if (rect.getX() > event.getX()) {
                        rect.setX(event.getX());
                    }
                    if (rect.getY() > event.getY()) {
                        rect.setY(event.getY());
                    }
                    backGr.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                }
                case "Circle" -> {
                    circle.setRadius(Math.sqrt(Math.pow(circle.getCenterX() - event.getX(), 2) + Math.pow(circle.getCenterY() - event.getY(), 2)));
                    backGr.strokeOval(event.getX() - circle.getRadius(), event.getY() - circle.getRadius(), circle.getRadius() * 2, circle.getRadius() * 2);
                }
                case "CircleFull" -> {
                    circle.setRadius(Math.sqrt(Math.pow(circle.getCenterX() - event.getX(), 2) + Math.pow(circle.getCenterY() - event.getY(), 2)));
                    backGr.fillOval(event.getX() - circle.getRadius(), event.getY() - circle.getRadius(), circle.getRadius() * 2, circle.getRadius() * 2);
                }
            }
        });

        saveBtn.setOnAction(event -> {
            FileChooser saveFile = new FileChooser();
            saveFile.setTitle("Сохранить файл");
            File file = saveFile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(1600, 900);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("Ошибка!");
                }
            }
        });

        openBtn.setOnAction(event -> {
            FileChooser openFile = new FileChooser();
            openFile.setTitle("Открыть файл");
            File file = openFile.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);
                    backGr.drawImage(img, 0, 0);
                } catch (IOException ex) {
                    System.out.println("Ошибка!");
                }
            }
        });

        infBtn.setOnAction(event -> {
            Label labelInfo = new Label("""
                      Сделал Портнов Е. М., гр.3530901/00001\r
                    \r
                             "Версия программы - beta 1.0");
                    """);
            Group rootAbout = new Group();
            rootAbout.getChildren().add(labelInfo);

            Scene secondScene = new Scene(rootAbout, 235, 60);

            Stage newWindow = new Stage();

            newWindow.setTitle("Информация");
            newWindow.setScene(secondScene);
            newWindow.setX(600);
            newWindow.setY(290);
            newWindow.show();
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(tlBar);
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);

        primaryStage.getIcons().add(new Image("file:src/Resources/paint.png"));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Paint Lite");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}