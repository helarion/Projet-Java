package sample;

    import javafx.beans.value.ChangeListener;
    import javafx.beans.value.ObservableValue;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.geometry.Point3D;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.Slider;
    import javafx.scene.control.TextField;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.Text;
    import javafx.scene.transform.Rotate;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;

    import java.awt.*;
    import java.io.File;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.logging.Level;
    import java.util.logging.Logger;

public class Controller {

    @FXML private Button openButton;
    @FXML private Button grbButton;
    @FXML private Button nbButton;
    @FXML private Button sepiaButton;
    @FXML private Button sobelButton;
    @FXML private Button ajouterTagButton;
    @FXML private Button symButton;
    @FXML private Slider slider;
    @FXML private ImageView imageView;
    @FXML private HBox inputLayout;
    @FXML private HBox filtresLayout;
    @FXML private Label labelTag;
    @FXML private TextField tagField;

    FiltreFactory filtre;
    int hauteur,largeur;
    Image image;
    ImageProjet imageProjet;
    FileChooser fileChooser;
    Desktop desktop;

    public void initialize() {

        hauteur=500;
        largeur=500;

        image = new Image("file:ressources/a.jpg");
        imageProjet=new ImageProjet();

        imageProjet.setImage(image);

        imageView.setImage(image);

        imageView.setImage(image);
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);

        fileChooser = new FileChooser();

        slider.setMin(0);
        slider.setMax(360);
        slider.setValue(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(180);
        slider.setMinorTickCount(10);
        slider.setBlockIncrement(10);

        slider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                imageView.setRotate(slider.getValue());
                /*gc.clearRect(0,0,800,800);
                drawRotatedImage(gc, image, slider.getValue(), 0,   0);*/
            } });
    }

    @FXML
    protected void grbClicked() {
        filtre=new FiltreGRBFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
        imageProjet.addFiltre(filtre);
    }

    @FXML
    protected void nbClicked() {
        filtre=new FiltreNoirEtBlancFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
        imageProjet.addFiltre(filtre);
        imageProjet.toXml();
    }

    @FXML
    protected void sepiaClicked() {
        filtre=new FiltreSepiaFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
        imageProjet.addFiltre(filtre);
    }

    @FXML
    protected void ajouterClicked() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setFitHeight(hauteur);
            imageView.setFitWidth(largeur);
            //drawRotatedImage(gc, image,  0,   0,   0);
            inputLayout.getChildren().addAll(imageView);
        }
    }

    @FXML
    protected void sobelClicked()
    {
        filtre=new FiltreSobelFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
    }

    @FXML
    protected void ajouterTagClicked()
    {
        String tag= tagField.getText();
        imageProjet.addTag(tag);
        updateTag();
    }

    @FXML
    protected void symClicked() {
        double saveZ=imageView.getTranslateZ();
        Point3D saveAxis=imageView.getRotationAxis();
        imageView.setTranslateZ(imageView.getBoundsInLocal().getWidth() / 2.0);
        imageView.setRotationAxis(Rotate.Y_AXIS);
        if ("Reflect".equals(symButton.getText())) {
            imageView.setRotate(180);
            symButton.setText("Restore");
        } else {
            imageView.setRotate(0);
            symButton.setText("Reflect");
        }
        imageView.setTranslateZ(saveZ);
        imageView.setRotationAxis(saveAxis);
    }


    private void updateTag()
    {
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
}

