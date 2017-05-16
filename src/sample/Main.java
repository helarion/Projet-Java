package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javafx.beans.value.ChangeListener;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();
    private Image image;
    private ImageProjet imageProjet = new ImageProjet();

    private ImageView imageView = new ImageView();
    private FiltreFactory filtre;

    private HBox filtresLayout = new HBox();

    int hauteur=500;
    int largeur=500;

    @Override
    public void start(Stage primaryStage) throws Exception{

        image = new Image("file:ressources/a.jpg");
        imageProjet.setImage(image);
        imageView.setImage(image);

        primaryStage.setWidth(700);
        primaryStage.setHeight(700);

       // WritableImage writable = new WritableImage(largeur,hauteur);
        //PixelWriter writer=writable.getPixelWriter();

        imageView.setImage(image);

        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Parcourir...");
        final Button inversButton = new Button ("Inverser couleurs");
        final Button nbButton = new Button ("Noir et blanc");
        final Button sepiaButton = new Button ("Sepia");
        final Button sobelButton = new Button ("Sobel");
        Label labelTag = new Label("Tag:");
        TextField tagField = new TextField();
        final Button ajouterTagButton = new Button("Ajouter");

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(360);
        slider.setValue(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(180);
        slider.setMinorTickCount(10);
        slider.setBlockIncrement(10);

        VBox mainLayout = new VBox();
        VBox imageLayout = new VBox();
        VBox inputLayout = new VBox();
        HBox effectsLayout = new HBox();
        Pane rootGroup = new VBox(12);

        inversButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        filtre=new FiltreGRBFactory(hauteur,largeur);
                        filtre.setFilter(hauteur,largeur,imageView,image);
                        imageProjet.addFiltre(filtre);
                    }
                });

        nbButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        filtre=new FiltreNoirEtBlancFactory(hauteur,largeur);
                        filtre.setFilter(hauteur,largeur,imageView,image);
                        imageProjet.addFiltre(filtre);
                        imageProjet.toXml();
                    }
                });

        sepiaButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        filtre=new FiltreSepiaFactory(hauteur,largeur);
                        filtre.setFilter(hauteur,largeur,imageView,image);
                        imageProjet.addFiltre(filtre);
                    }
                });

        sobelButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        filtre=new FiltreSobelFactory(hauteur,largeur);
                        filtre.setFilter(hauteur,largeur,imageView,image);
                    }
                });

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
                            imageView.setFitHeight(hauteur);
                            imageView.setFitWidth(largeur);
                            //drawRotatedImage(gc, image,  0,   0,   0);
                            inputLayout.getChildren().addAll(imageView);
                        }
                    }
                });

        ajouterTagButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        String tag= tagField.getText();
                        imageProjet.addTag(tag);
                        updateTag();
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

        imageLayout.getChildren().addAll(imageView);

        inputLayout.getChildren().addAll(openButton);

        effectsLayout.getChildren().addAll(nbButton);
        effectsLayout.getChildren().addAll(sepiaButton);
        effectsLayout.getChildren().addAll(inversButton);
        effectsLayout.getChildren().addAll(sobelButton);
        effectsLayout.getChildren().addAll(reflect);

        inputLayout.getChildren().addAll(effectsLayout);
        inputLayout.getChildren().addAll(slider);
        inputLayout.getChildren().add(labelTag);
        inputLayout.getChildren().add(tagField);
        inputLayout.getChildren().addAll(ajouterTagButton);
        inputLayout.getChildren().addAll(filtresLayout);

        mainLayout.getChildren().addAll(imageLayout);
        mainLayout.getChildren().addAll(inputLayout);

        rootGroup.getChildren().addAll(imageLayout);
        rootGroup.getChildren().addAll(inputLayout);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        primaryStage.setTitle("Projet Java");
        primaryStage.setScene(new Scene(rootGroup, 1000, 1000));
        primaryStage.show();
    }

    private void updateTag() {
        filtresLayout.getChildren().clear();
        ArrayList<String> list=imageProjet.getListeTag();
        for(int i=0;i<list.size();i++)
        {
            Label label = new Label(list.get(i)+" ");
            filtresLayout.getChildren().add(label);
        }
        imageProjet.toXml();
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
