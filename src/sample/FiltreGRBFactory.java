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
public class FiltreGRBFactory extends FiltreFactory {

    public FiltreGRBFactory(Image image)
    {
        super(image);
        setNom("GRB");
    }

    public WritableImage setFilter(int hauteur, int largeur, ImageView imageView, Image image)
    {
        for(int i=0;i<image.getWidth();i++) {
            for (int j = 0; j <image.getHeight(); j++) {
                setColor(image.getPixelReader().getColor(i,j));
                setColor(Color.color(getColor().getGreen(),getColor().getBlue(),getColor().getRed()));
                writer.setColor(i,j,getColor());
            }
        }
        imageView.setImage(getWritable());
        imageView.setFitHeight(hauteur);
        imageView.setFitWidth(largeur);
        return getWritable();
    }
}
