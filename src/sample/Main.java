package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javafx.beans.value.ChangeListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();
    private Image image;
    private ImageView imageView = new ImageView();

    @Override
    public void start(Stage primaryStage) throws Exception{

        image = new Image("file:ressources/a.jpg");
        imageView.setImage(image);

        double hauteur=image.getHeight();
        double largeur=image.getWidth();

        primaryStage.setWidth(700);
        primaryStage.setHeight(700);

        WritableImage image2 = new WritableImage(1000,1000);
        PixelWriter writer=image2.getPixelWriter();


        //System.out.println("hauteur : "+ hauteur +"; largeur : "+ largeur);
        Color couleur;

/*
        for(int i=0;i<500;i++) {
            for (int j = 0; j <500; j++) {
                couleur=image1.getPixelReader().getColor(i,j);
                couleur=Color.color(couleur.getRed(),couleur.getGreen(),couleur.getBlue());
                writer.setColor(i,j,couleur);
            }
        }
*/
        /////////////////////inverseur couleur ///////////////////////////////////
       /*
        for(int i=0;i<500;i++) {
            for (int j = 0; j <500; j++) {
                couleur=image1.getPixelReader().getColor(i,j);
                couleur=Color.color(couleur.getGreen(),couleur.getBlue(),couleur.getRed());
                writer.setColor(i,j,couleur);
            }
        }
        */
        //////////////////Noir et blanc//////////////////////////////
       /*
        for(int i=0;i<500;i++) {
            for (int j = 0; j <500; j++) {
                couleur=image1.getPixelReader().getColor(i,j);
                double gris=(couleur.getBlue()+couleur.getGreen()+couleur.getRed())/3;
                couleur=Color.color(gris,gris,gris);
                writer.setColor(i,j,couleur);
            }
        }
        */
        /////////////////////////////////////////////////////////////
        //////////////////sepia//////////////////////////////
        /*
        for(int i=0;i<500;i++) {
            for (int j = 0; j <500; j++) {
                couleur=image.getPixelReader().getColor(i,j);
                System.out.println("rouge :"+couleur.getRed()+"vert :"+couleur.getGreen()+"bleu :"+couleur.getBlue());
                double sepiaR=(couleur.getRed()*0.393) + (couleur.getGreen()*0.769) + (couleur.getBlue()*0.189);
                double sepiaG=(couleur.getRed()*0.349) + (couleur.getGreen()*0.686) + (couleur.getBlue()*0.168);
                double sepiaB=(couleur.getRed()*0.272) + (couleur.getGreen()*0.534) + (couleur.getBlue()*0.131);
                if(sepiaR>1){
                    sepiaR=1;
                }
                if(sepiaG>1){
                    sepiaG=1;
                }
                if(sepiaB>1){
                    sepiaB=1;
                }
                couleur=Color.color(sepiaR,sepiaG,sepiaB);
                writer.setColor(i,j,couleur);
            }
        }
        */
        /////////////////////////////////////////////////////////////
        ///////////////  Sobel //////////////////////////////////////
        for(int i=0;i<500;i++) {
            for (int j = 0; j <500; j++) {
                couleur=image.getPixelReader().getColor(i,j);
                couleur=Color.color(couleur.getRed(),couleur.getGreen(),couleur.getBlue());
                writer.setColor(i,j,couleur);
            }
        }
        ///////////////////////////////////////////////////////////////
        imageView.setImage(image2);

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Parcourir...");
        final VBox inputLayout = new VBox();

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(360);
        slider.setValue(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(180);
        slider.setMinorTickCount(10);
        slider.setBlockIncrement(10);

        slider.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2)
            {
                imageView.setRotate(slider.getValue());
                /*gc.clearRect(0,0,800,800);
                drawRotatedImage(gc, image, slider.getValue(), 0,   0);*/
            }
        });

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            image = new Image(file.toURI().toString());
                            imageView.setImage(image);
                            //drawRotatedImage(gc, image,  0,   0,   0);
                            inputLayout.getChildren().addAll(imageView);
                        }
                    }
                });

        final Button reflect = new Button("Symétrie");
        reflect.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                double saveZ=imageView.getTranslateZ();
                Point3D saveAxis=imageView.getRotationAxis();
                imageView.setTranslateZ(imageView.getBoundsInLocal().getWidth() / 2.0);
                imageView.setRotationAxis(Rotate.Y_AXIS);
                if ("Reflect".equals(reflect.getText())) {
                    imageView.setRotate(180);
                    reflect.setText("Restore");
                } else {
                    imageView.setRotate(0);
                    reflect.setText("Reflect");
                }
                imageView.setTranslateZ(saveZ);
                imageView.setRotationAxis(saveAxis);
            }
        });

        VBox imagePane = new VBox();
        inputLayout.getChildren().addAll(slider);

        imagePane.getChildren().addAll(imageView);
        inputLayout.getChildren().addAll(openButton);
        inputLayout.getChildren().addAll(reflect);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(imagePane);
        rootGroup.getChildren().addAll(inputLayout);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        primaryStage.setTitle("Test d'affichage d'image");
        primaryStage.setScene(new Scene(rootGroup, 1000, 1000));
        primaryStage.show();
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Main.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
