package sample;

    import javafx.beans.value.ChangeListener;
    import javafx.beans.value.ObservableValue;
    import javafx.fxml.FXML;
    import javafx.geometry.Point3D;
    import javafx.scene.control.*;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.TextField;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.HBox;
    import javafx.scene.transform.Rotate;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;

    import javax.xml.bind.JAXBContext;
    import javax.xml.bind.JAXBException;
    import javax.xml.bind.Marshaller;
    import javax.xml.bind.Unmarshaller;
    import java.awt.*;
    import java.io.File;
    import java.util.ArrayList;

public class Controller {


    @FXML private Button symButton;
    @FXML private Slider slider;
    @FXML private ImageView imageView;
    @FXML private TextField tagField;
    @FXML private ListView<String> listeFiltre;

    FiltreFactory filtre;
    int hauteur,largeur;
    Image image;
    ImageProjet imageProjet;
    FileChooser fileChooser;
    Desktop desktop;

    public void initialize() {

        hauteur=500;
        largeur=500;

        String url="file:ressources/a.jpg";
        image = new Image(url);
        imageProjet=new ImageProjet();

        imageProjet.setImage(url);

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
                imageProjet.setRotation((int)slider.getValue());
            } });
    }

    @FXML
    protected void grbClicked() {
        filtre=new FiltreGRBFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
        imageProjet.setFiltre(filtre.getNom());
    }

    @FXML
    protected void nbClicked() {
        filtre=new FiltreNoirEtBlancFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
        imageProjet.setFiltre(filtre.getNom());
    }

    @FXML
    protected void sobelClicked()
    {
        filtre=new FiltreSobelFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
        imageProjet.setFiltre(filtre.getNom());
    }

    @FXML
    protected void sepiaClicked() {
        filtre=new FiltreSepiaFactory(image);
        filtre.setFilter(hauteur,largeur,imageView,image);
        imageProjet.setFiltre(filtre.getNom());
    }

    @FXML
    protected void ajouterImageClicked() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            image = new Image(file.toURI().toString());
            imageProjet.setImage(file.toURI().toString());
            imageView.setImage(image);
            imageView.setFitHeight(hauteur);
            imageView.setFitWidth(largeur);
        }
    }

    @FXML
    protected void ajouterXMLClicked() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            imageProjet=fromXML(file.toURI().toString());
            image = new Image(imageProjet.getImage());
            imageView.setFitHeight(hauteur);
            imageView.setFitWidth(largeur);
        }
    }

    @FXML
    protected void ajouterTagClicked()
    {
        String tag= tagField.getText();
        imageProjet.addTag(tag);
        updateTag();
    }

    @FXML
    protected void sauvegarderClicked()
    {
        toXml(imageProjet);
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

    public void toXml(ImageProjet img)
    {
        try {
            String nom=img.getImage();

            String[] result = nom.split("/");
            nom=result[result.length-1];
            nom+=".xml";

            System.out.println("nom:"+nom);
            File file = new File(nom);
            JAXBContext jaxbContext = JAXBContext.newInstance(ImageProjet.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(img, file);
            jaxbMarshaller.marshal(img, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public ImageProjet fromXML(String path)
    {
        try {
            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(ImageProjet.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ImageProjet imageProjet = (ImageProjet) jaxbUnmarshaller.unmarshal(file);
            System.out.println(imageProjet);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return imageProjet;
    }

    private void updateTag()
    {
        listeFiltre.getItems().clear();
        ArrayList<String> list=imageProjet.getListeTag();
        for(int i=0;i<list.size();i++)
        {
            Label label = new Label(list.get(i)+" ");
            listeFiltre.getItems().add(list.get(i));
        }
    }

    public void chiffrerClicked()
    {

    }
}

