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
    import javafx.scene.image.WritableImage;
    import javafx.scene.transform.Rotate;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;

    import javax.xml.bind.JAXBContext;
    import javax.xml.bind.JAXBException;
    import javax.xml.bind.Marshaller;
    import javax.xml.bind.Unmarshaller;
    import javax.xml.soap.Text;
    import java.awt.*;
    import java.io.File;
    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.util.ArrayList;
    import java.util.Collections;

public class Controller {

    @FXML private Button symButton;
    @FXML private Slider slider;
    @FXML private ImageView imageView;
    @FXML private TextField tagField;
    @FXML private ListView<String> listeTag;
    @FXML private ListView<String> listeImage;
    @FXML private TextField rechercheTagTextField;
    @FXML private TextField motDePasseTextField;

    FiltreFactory filtre;
    int hauteur,largeur;
    Image image;
    ImageProjet imageProjet;
    FileChooser fileChooser;

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
            imageProjet=null;
            imageProjet=fromXML(file.toURI().toString());
            image = new Image(imageProjet.getImage());
            imageView.setImage(image);
            imageView.setFitHeight(hauteur);
            imageView.setFitWidth(largeur);
            slider.setValue((double)imageProjet.getRotation());
            String nomFiltre=imageProjet.getFiltre();
            System.out.println("filtre:"+nomFiltre);
            if(nomFiltre.equals("GRB")) grbClicked();
            else if(nomFiltre.equals("Sepia")) sepiaClicked();
            else if(nomFiltre.equals("Noir et blanc")) nbClicked();
            else if(nomFiltre.equals("Sobel")) sobelClicked();

            updateTag();
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
    protected void supprimerTagClicked()
    {
        if(listeTag.getSelectionModel().getSelectedItem()!=null) {
            int index=listeTag.getSelectionModel().getSelectedIndex();
            System.out.println("index:"+index);
            imageProjet.getListeTag().remove(index);
            updateTag();
        }
    }

    @FXML
    protected void sauvegarderClicked()
    {
        toXml(imageProjet);
    }

    @FXML
    protected void rechercherClicked()
    {
        listeImage.getItems().clear();
        String recherche=rechercheTagTextField.getText();
        File dir = new File ("./bibliotheque");
        if (dir.isDirectory()) {
            ArrayList<ImageProjet> listImage = new ArrayList<ImageProjet>();
            File[] fileList = dir.listFiles();
            for (File f : fileList) {
                ImageProjet img=fromXML(f.getPath());
                for(int i=0;i<img.getListeTag().size();i++)
                {
                    if(img.getListeTag().get(i).equals(recherche))
                    {
                        listImage.add(img);
                        System.out.println(f);
                        listeImage.getItems().add(f.getPath());
                    }
                }
            }
        }
    }

    @FXML
    protected void selectionnerImageCLicked()
    {
        if(listeImage.getSelectionModel().getSelectedItem()!=null)
        {
            String path=listeImage.getSelectionModel().getSelectedItem().toString();
            imageProjet=null;
            imageProjet=fromXML(path);
            image = new Image(imageProjet.getImage());
            imageView.setImage(image);
            imageView.setFitHeight(hauteur);
            imageView.setFitWidth(largeur);
            slider.setValue((double)imageProjet.getRotation());
            String nomFiltre=imageProjet.getFiltre();
            System.out.println("filtre:"+nomFiltre);
            if(nomFiltre.equals("GRB")) grbClicked();
            else if(nomFiltre.equals("Sepia")) sepiaClicked();
            else if(nomFiltre.equals("Noir et blanc")) nbClicked();
            else if(nomFiltre.equals("Sobel")) sobelClicked();

            updateTag();
        }
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
            nom="bibliotheque/"+result[result.length-1];
            nom+=".xml";

            //System.out.println("nom:"+nom);
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
        ImageProjet r=new ImageProjet();
        try {
            String[] result = path.split("file:");
            if(result.length>1)path=result[1];
            else path=result[0];
            //System.out.println("chemin:"+path);
            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(ImageProjet.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            r = (ImageProjet) jaxbUnmarshaller.unmarshal(file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return r;
    }

    private void updateTag()
    {
        listeTag.getItems().clear();
        ArrayList<String> list=imageProjet.getListeTag();
        for(int i=0;i<list.size();i++)
        {
            Label label = new Label(list.get(i)+" ");
            listeTag.getItems().add(list.get(i));
        }
    }

    public void chiffrerClicked()
    {

        ArrayList pixels = new ArrayList();

        int compteurPixel=0;

        for(int i=0;i<(image.getWidth()*image.getHeight());i++){
            pixels.add(i);
        }


        String motDePasse=motDePasseTextField.getText();
        String motChiffre=Controller.encode(motDePasse);
        byte[] talbeau=motChiffre.getBytes();

        byte[] seed=talbeau;

        WritableImage image2=new WritableImage((int)image.getWidth(),(int)image.getHeight());
        boolean bonPixel=false;
        int compteurBonPixel=0;



        Collections.shuffle(pixels, new SecureRandom(seed));
        for(int i=0;i<pixels.size();i++){
            System.out.println("donnée à l'indice " + i + " = " + pixels.get(i));
        }
        for(int i = 0; i <image.getWidth(); i++) {
            for(int j = 0; j <image.getHeight(); j++) {
                System.out.println("Pixel à trouver : n°"+pixels.get(compteurPixel));
                for(int k = 0; k <image.getWidth(); k++) {
                    for(int l = 0; l <image.getHeight(); l++) {
                        if(compteurBonPixel==(int)pixels.get(compteurPixel)){
                            System.out.println("Pixel trouvé en ["+k+"]["+l+"]");
                            image2.getPixelWriter().setColor(i,j,image.getPixelReader().getColor(k,l));
                            bonPixel=true;
                            break;
                        }else{
                            compteurBonPixel++;
                        }
                    }
                    if(bonPixel==true){
                        break;
                    }
                }
                bonPixel=false;
                compteurBonPixel=0;
                compteurPixel++;
            }
        }

        imageView.setImage(image2);
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);


    }

    public void dechiffrerClicked(){

        ArrayList pixels = new ArrayList();
        int x=0;
        int y=0;

        int compteurPixel=0;

        for(int i=0;i<(image.getWidth()*image.getHeight());i++){
            pixels.add(i);
        }


        String motDePasse=motDePasseTextField.getText();
        String motChiffre=Controller.encode(motDePasse);
        byte[] talbeau=motChiffre.getBytes();

        byte[] seed=talbeau;

        WritableImage image2=new WritableImage((int)image.getWidth(),(int)image.getHeight());
        boolean bonPixel=false;
        int compteurBonPixel=0;



        Collections.shuffle(pixels, new SecureRandom(seed));
        for(int i=0;i<pixels.size();i++){
            System.out.println("donnée à l'indice " + i + " = " + pixels.get(i));
        }
        for(int i = 0; i <image.getWidth(); i++) {
            for(int j = 0; j <image.getHeight(); j++) {
                System.out.println("Pixel à trouver : n°"+pixels.get(compteurPixel));
                for(int k = 0; k <image.getWidth(); k++) {
                    for(int l = 0; l <image.getHeight(); l++) {
                        if(compteurBonPixel==(int)pixels.get(compteurPixel)){
                            System.out.println("Pixel trouvé en ["+i+"]["+j+"]");
                            x=j;
                            y=i;
                            bonPixel=true;
                            break;
                        }else{
                            compteurBonPixel++;
                        }
                    }
                    if(bonPixel==true){
                        break;
                    }

                }
                compteurBonPixel=0;
                image2.getPixelWriter().setColor(i,j,image.getPixelReader().getColor(y,x));
                bonPixel=false;
                compteurPixel++;
            }
        }




        imageView.setImage(image2);
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);
    }




    // tiré de : https://www.developpez.net/forums/d344729/java/general-java/apis/securite/appliquer-fonction-hachage-md5-texte/
    /*
* Encode la chaine passé en paramètre avec l’algorithme MD5
* @param key : la chaine à encoder
* @return la valeur (string) hexadécimale sur 32 bits
*/
    public static String encode (String key)
    {
        byte[] uniqueKey = key.getBytes();
        byte[] hash = null;

//------------------------------------------------------------------------------------------------

        try
        {
// on récupère un objet qui permettra de crypter la chaine
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        }
        catch (NoSuchAlgorithmException e) {throw new Error("no MD5 support in this VM");}

//-------------------------------------------------------------------------------------------------

        StringBuffer hashString = new StringBuffer();
        for (int i = 0; i < hash.length; ++i)
        {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1)
            {
                hashString.append(0);
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else {hashString.append(hex.substring(hex.length() - 2));}
        }
        return hashString.toString();
    }
}

