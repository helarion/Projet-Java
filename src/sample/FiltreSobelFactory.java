package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * Created by Axel on 14/05/2017.
 */
public class FiltreSobelFactory extends FiltreFactory {

    public FiltreSobelFactory(int hauteur,int largeur)
    {
        super(hauteur,largeur);
    }

    public WritableImage setFilter(int hauteur, int largeur, ImageView imageView, Image image)
    {
        for(int i=0;i<hauteur;i++) {
            for (int j = 0; j <largeur; j++) {
                setColor(image.getPixelReader().getColor(i,j));
                setColor(Color.color(getColor().getRed(),getColor().getGreen(),getColor().getBlue()));
                writer.setColor(i,j,getColor());
            }
        }
        imageView.setImage(getWritable());
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);
        return getWritable();
    }
}
